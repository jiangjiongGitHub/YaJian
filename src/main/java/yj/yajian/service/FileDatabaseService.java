package yj.yajian.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * springboot项目，使用静态Map对象临时作为一个数据库，因环境限制不能使用mysql、redis等服务，
 * 设定一个目录存放数据库文本文件，
 * 项目启动时加载数据库目录最新的数据库文本文件，
 * 项目运行期间，每隔一段时间序列化Map，保存数据库文本文件到数据库目录，以时间为后缀并精确到毫秒。
 */
@Service
public class FileDatabaseService {

    // 内存数据库
    private static final Map<String, Object> DATABASE = new ConcurrentHashMap<>();

    // 数据存储目录
    @Value("${database.directory:./data}")
    private String dataDirectory;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 启动时加载最新数据文件
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        try {
            File latestFile = findLatestDataFile();
            if (latestFile != null) {
                loadDataFromFile(latestFile);
                System.out.println("Loaded database from: " + latestFile.getAbsolutePath());
            } else {
                System.out.println("No existing data files found. Starting with empty database.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * 定期保存数据（每分钟）
     */
    @Scheduled(fixedRate = 60000) // 60秒
    public void autoSave() {
        saveToFile();
    }

    /**
     * 关闭时保存数据
     */
    @PreDestroy
    public void onShutdown() {
        saveToFile();
        System.out.println("Database saved during shutdown");
    }

    // 核心数据库操作方法
    public void put(String key, Object value) {
        DATABASE.put(key, value);
    }

    public Object get(String key) {
        return DATABASE.get(key);
    }

    public void remove(String key) {
        DATABASE.remove(key);
    }

    public Map<String, Object> getAll() {
        return new HashMap<>(DATABASE);
    }

    // 私有方法实现
    private File findLatestDataFile() throws IOException {
        Path dirPath = Paths.get(dataDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            return null;
        }

        try (Stream<Path> paths = Files.list(dirPath)) {
            Optional<Path> latestPath = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()));

            return latestPath.map(Path::toFile).orElse(null);
        }
    }

    private void loadDataFromFile(File file) {
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            Map<?, ?> loaded = objectMapper.readValue(bytes, Map.class);
            DATABASE.clear();
            loaded.forEach((k, v) -> DATABASE.put(k.toString(), v));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database file", e);
        }
    }

    private synchronized void saveToFile() {
        try {
            Path dirPath = Paths.get(dataDirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "db-" + dateFormat.format(new Date()) + ".json";
            Path filePath = dirPath.resolve(fileName);

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(filePath.toFile(), DATABASE);

            System.out.println("Database saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }
}
