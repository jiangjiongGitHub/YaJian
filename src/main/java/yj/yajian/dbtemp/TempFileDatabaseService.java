package yj.yajian.dbtemp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yj.yajian.bookmark.Bookmark;
import yj.yajian.collection.CollectionItem;
import yj.yajian.db.service.FileDatabaseService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据：keep-alive map {key_yyyymmddhhmmss, now_time},{key_yyyymmddhhmmss, now_time}
 * 启动：生成唯一key key_yyyymmddhhmmss
 * 修改、保存数据：正常操作同时，写入到临时文件 map {bookmark-xxx,data,tag},{collection-xxx,data,tag}，并标记已有 key_yyyymmddhhmmss
 * 定时读取临时文件：若系统没有，写入数据库，并标记新增已有 key_yyyymmddhhmmss
 * 如果 keep-alive 所有成员都包含，则删除该条临时数据
 */
@Slf4j
@Service
public class TempFileDatabaseService {

    // 项目启动：生成唯一key key_yyyymmddhhmmss
    public static final String key = "key_" + new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss__SSS").format(new Date());

    // 内存数据库
    private static final Map<String, String> TEMPDATABASE = new ConcurrentHashMap<>();

    // 数据存储目录 ${database.directory:./data}
    @Value("${database.directory}")
    private String dataDirectory;

    // 序列化对象 Jackson vs Fastjson
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 核心数据库操作方法
    public void put(String key, String value) {
        TEMPDATABASE.put(key, value);
    }

    public String get(String key) {
        return TEMPDATABASE.get(key);
    }

    public void remove(String key) {
        TEMPDATABASE.remove(key);
    }

    public Map<String, Object> getAll() {
        return new HashMap<>(TEMPDATABASE);
    }


    public synchronized Map<String, String> loadDataFromFile() {
        try {
            Path dirPath = Paths.get(dataDirectory + "temp");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "db-temp.json";
            Path filePath = dirPath.resolve(fileName);
            File file = filePath.toFile();
            if (!file.exists() || file.length() == 0) {
                TEMPDATABASE.clear();
                return new HashMap<>();
            }

            byte[] bytes = Files.readAllBytes(file.toPath());
            Map<?, ?> loaded = objectMapper.readValue(bytes, Map.class);
            Map<String, String> temp = new HashMap<>();
            loaded.forEach((k, v) -> temp.put(k.toString(), v.toString()));
            TEMPDATABASE.clear();
            TEMPDATABASE.putAll(temp);
            return TEMPDATABASE;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public synchronized void saveToFile() {
        try {
            Path dirPath = Paths.get(dataDirectory + "temp");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String fileName = "db-temp.json";
            Path filePath = dirPath.resolve(fileName);

            // 启用 Map Key 排序
            objectMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

            // 写入文件（自动按 Key 排序）
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(filePath.toFile(), TEMPDATABASE);

            log.info("TempDatabase saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving TempDatabase: " + e.getMessage());
        }
    }

    @Resource
    private FileDatabaseService dbService;

    @Scheduled(initialDelay = 15000, fixedDelay = 60000) // 更新心跳
    public void autoKeepAlive() {
        log.info("Executing keep alive at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 添加日志输出
        loadDataFromFile();
        String mapStr = TEMPDATABASE.get("keep-alive");
        Map<String, String> map = JSONObject.parseObject(mapStr, Map.class);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() + 300000)));
        TEMPDATABASE.put("keep-alive", JSONObject.toJSONString(map));
        saveToFile();
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 60000) // 同步：收藏、书签
    public synchronized void autoRead() {
        log.info("Executing sync temp data at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 添加日志输出
        loadDataFromFile();
        String mapStr = TEMPDATABASE.get("keep-alive");
        Map<String, String> map = JSONObject.parseObject(mapStr, Map.class);
        if (map == null) {
            map = new HashMap<>();
        }
        Set<String> keysAlive = new HashSet<>();
        map.entrySet().forEach(entry -> {
            log.info("key() = {}, value() = {}", entry.getKey(), entry.getValue());
            if (entry.getValue().compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) > 0) {
                keysAlive.add(entry.getKey());
            }
        });
        log.info("key = {}, keysAlive = {}", key, keysAlive);


        // 遍历 TEMPDATABASE 所有数据
        Set<String> keysToRemove = new HashSet<>();
        TEMPDATABASE.entrySet().forEach(entry -> {
            log.info(entry.getKey() + ", " + entry.getValue());
            if (entry.getKey().startsWith("collection-")) {
                CollectionItem itemTemp = JSON.parseObject(entry.getValue(), CollectionItem.class);
                CollectionItem item = JSON.parseObject(dbService.get(entry.getKey()), CollectionItem.class);
                if (item == null || itemTemp.getTime().compareTo(item.getTime()) > 0) {
                    dbService.put(entry.getKey(), JSONObject.toJSONString(itemTemp));
                } else {
                    log.info("数据已更新完毕：{}", entry.getKey());
                }
                Set<String> keys = itemTemp.getKeys();
                keys.add(key);
                itemTemp.setKeys(keys);
                TEMPDATABASE.put(entry.getKey(), JSONObject.toJSONString(itemTemp));

                // 如果 itemTemp.getKeys() 包含所有的 keysAlive，则 TEMPDATABASE 删除掉数据
                if (itemTemp.getKeys().containsAll(keysAlive)) {
                    keysToRemove.add(entry.getKey());
                }
            }
            if (entry.getKey().startsWith("bookmark-")) {
                Bookmark itemTemp = JSON.parseObject(entry.getValue(), Bookmark.class);
                Bookmark item = JSON.parseObject(dbService.get(entry.getKey()), Bookmark.class);
                if (item == null || !(itemTemp.getTitle() + itemTemp.getUrl()).equals(item.getTitle() + item.getUrl())) {
                    dbService.put(entry.getKey(), JSONObject.toJSONString(itemTemp));
                } else {
                    log.info("数据已更新完毕：{}", entry.getKey());
                }
                Set<String> keys = itemTemp.getKeys();
                keys.add(key);
                itemTemp.setKeys(keys);
                TEMPDATABASE.put(entry.getKey(), JSONObject.toJSONString(itemTemp));

                // 如果 itemTemp.getKeys() 包含所有的 keysAlive，则 TEMPDATABASE 删除掉数据
                if (itemTemp.getKeys().containsAll(keysAlive)) {
                    keysToRemove.add(entry.getKey());
                }
            }
        });
        keysToRemove.forEach(f -> {
            remove(f);
            log.info("temp file remove {}", f);
        });
        if (!TEMPDATABASE.isEmpty()) {
            Set<String> strings = TEMPDATABASE.keySet();
            // 判断 strings 里除了 "keep-alive" 是否还有别的 key，用循环计算数量
            int count = 0;
            for (String s : strings) {
                if (!s.equals("keep-alive")) {
                    count++;
                }
            }
            if (!keysToRemove.isEmpty() || count > 0) {
                saveToFile();
            }
        }
    }

}
