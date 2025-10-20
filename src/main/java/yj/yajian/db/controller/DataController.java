package yj.yajian.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import yj.yajian.tool.entity.DemoEntity;
import yj.yajian.db.service.FileDatabaseService;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/data")
public class DataController {

    private final FileDatabaseService fileDatabaseService;

    public DataController(FileDatabaseService dbService) {
        this.fileDatabaseService = dbService;
    }

    private static final ThreadLocal<SecureRandom> RANDOM = ThreadLocal.withInitial(SecureRandom::new);
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static String generate() {
        SecureRandom rnd = RANDOM.get();
        char[] buffer = new char[rnd.nextInt(7) + 2];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = CHARS[rnd.nextInt(CHARS.length)];
        }
        return new String(buffer);
    }

    // http://127.0.0.1:18888/data/put?key=123&value=456
    @GetMapping("/put")
    public Boolean saveData(@RequestParam String key, @RequestParam String value) {
        {
            // 放入一个List<DemoEntity>对象，测试json序列化和反序列化
            DemoEntity product1 = DemoEntity.builder()
                    .id(1L)
                    .name("iPhone XR")
                    .price(new BigDecimal("2999.99"))
                    .category(DemoEntity.Category.ELECTRONICS)
                    .tags(Arrays.asList("ELECTRONICS", "PHONE", "5G"))
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .expireDate(LocalDate.now().minusDays(2))
                    .active(true)
                    .build();
            DemoEntity product2 = DemoEntity.builder()
                    .id(2L)
                    .name("iPhone XS")
                    .price(new BigDecimal("3999.99"))
                    .category(DemoEntity.Category.ELECTRONICS)
                    .tags(Arrays.asList("ELECTRONICS", "PHONE", "5G"))
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .expireDate(LocalDate.now().minusDays(2))
                    .active(true)
                    .build();
            fileDatabaseService.put("list", JSONObject.toJSONString(Arrays.asList(product1, product2)));
        }
        fileDatabaseService.put(key, value);
        return true;
    }

    // http://127.0.0.1:18888/data/get?key=123
    @GetMapping("/get")
    public Object getData(@RequestParam String key) {
        {
            // 放入一个List<DemoEntity>对象，测试json序列化和反序列化
            List<DemoEntity> list = JSONObject.parseObject(fileDatabaseService.get("list"), new TypeReference<List<DemoEntity>>() {
            });
        }
        return fileDatabaseService.get(key);
    }

    // http://127.0.0.1:18888/data/all
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        return fileDatabaseService.getAll();
    }

    // 根据key删除元素
    // URL: http://127.0.0.1:18888/data/remove?key=123
    @GetMapping("/remove")
    public Boolean remove(@RequestParam String key) {
        fileDatabaseService.remove(key);
        return true;
    }
}
