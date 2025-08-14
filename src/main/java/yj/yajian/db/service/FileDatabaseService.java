package yj.yajian.db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * springboot项目，使用静态Map对象临时作为一个数据库，因环境限制不能使用mysql、redis等服务，
 * 设定一个目录存放数据库文本文件，
 * 项目启动时加载数据库目录最新的数据库文本文件，
 * 项目运行期间，每隔一段时间序列化Map，保存数据库文本文件到数据库目录，以时间为后缀并精确到毫秒。
 */
@Slf4j
@Service
public class FileDatabaseService {

    // 内存数据库
    private static final Map<String, String> DATABASE = new ConcurrentHashMap<>();

    // 数据存储目录 ${database.directory:./data}
    @Value("${database.directory}")
    private String dataDirectory;

    // 序列化对象 Jackson vs Fastjson
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 线程安全处理
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss");

    // 异步线程池保存
    private final ExecutorService writerExecutor = Executors.newSingleThreadExecutor();

    /**
     * 启动时加载最新数据文件
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        try {
            File latestFile = findLatestDataFile();
            if (latestFile != null) {
                log.info("Loading database from: " + latestFile.getAbsolutePath());
                loadDataFromFile(latestFile);
                log.info("Loaded database from: " + latestFile.getAbsolutePath());
            } else {
                log.info("No existing data files found. Starting with empty database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * 定期保存数据（每分钟）
     */
    @Scheduled(initialDelay = 60000, fixedDelay = 600000) // 60秒 fixedRate 改为 fixedDelay
    public void autoSave() {
        log.info("Executing autoSave at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 添加日志输出
        writerExecutor.submit(this::saveToFile);
        writerExecutor.submit(this::deleteOldFiles);
    }

    private void deleteOldFiles() {
        Path dirPath = Paths.get(dataDirectory);
        if (Files.exists(dirPath)) {
            try (Stream<Path> paths = Files.list(dirPath)) {
                paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".json"))
                        .sorted(Comparator.comparing((Path p) ->
                                p.getFileName().toString()
                        ).reversed())
                        .skip(20) // 跳过最新的文件
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                                log.info("Deleted file: " + p);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭时保存数据
     */
    @PreDestroy
    public void onShutdown() {
        if (!DATABASE.isEmpty()) {
            saveToFile();
        }
        log.info("Database saved during shutdown");
    }

    // 核心数据库操作方法
    public void put(String key, String value) {
        DATABASE.put(key, value);
    }

    public String get(String key) {
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
                    .max(Comparator.comparing(p -> p.getFileName().toString()));
            return latestPath.map(Path::toFile).orElse(null);
        }
    }

    private void loadDataFromFile(File file) {
        try {
            if (!file.exists() || file.length() == 0) {
                DATABASE.clear();
                return;
            }

            byte[] bytes = Files.readAllBytes(file.toPath());
            Map<?, ?> loaded = objectMapper.readValue(bytes, Map.class);
            Map<String, String> temp = new HashMap<>();
            loaded.forEach((k, v) -> temp.put(k.toString(), v.toString()));
            DATABASE.clear();
            DATABASE.putAll(temp);
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

            String fileName = "db-" + formatter.format(LocalDateTime.now()) + ".json";
            Path filePath = dirPath.resolve(fileName);

            // 启用 Map Key 排序
            objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

            // 写入文件（自动按 Key 排序）
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(filePath.toFile(), DATABASE);

            log.info("Database saved to: " + filePath);

            // 把文件写到databak目录一份
            try {
                Path bakPath = Paths.get("./databak");
                if (!Files.exists(bakPath)) {
                    Files.createDirectories(bakPath);
                }
                // 使用 FileWriter 直接写入，避免创建临时文件。
                Files.copy(filePath, Paths.get("./databak/db-" + new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + ".json"),
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES);
                log.info("Database saved to: " + "./databak/db-" + new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + ".json");
                // 打印文件大小
                long fileSize = Files.size(filePath);
                log.info("File size: " + fileSize + " bytes");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving database: " + e.getMessage());
        }
    }
}
