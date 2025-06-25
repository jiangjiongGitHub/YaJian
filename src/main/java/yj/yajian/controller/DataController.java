package yj.yajian.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import yj.yajian.po.DemoPO;
import yj.yajian.service.FileDatabaseService;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    private final FileDatabaseService dbService;

    public DataController(FileDatabaseService dbService) {
        this.dbService = dbService;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 1000)
    public void autoSave() {
        dbService.put(generate(), generate());
        System.out.println("Executing put at: " + new Date());
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
            // 放入一个List<DemoPO>对象，测试json序列化和反序列化
            DemoPO product1 = DemoPO.builder()
                    .id(1L)
                    .name("iPhone XR")
                    .price(new BigDecimal("2999.99"))
                    .category(DemoPO.Category.ELECTRONICS)
                    .tags(Arrays.asList(" 电子", "旗舰", "5G"))
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .expireDate(LocalDate.of(2025, 7, 10))
                    .active(true)
                    .build();
            dbService.put("list", JSONObject.toJSONString(Arrays.asList(product1)));
        }
        dbService.put(key, value);
        return true;
    }

    // http://127.0.0.1:18888/data/get?key=123
    @GetMapping("/get")
    public Object getData(@RequestParam String key) {
        {
            // 放入一个List<DemoPO>对象，测试json序列化和反序列化
            List<DemoPO> list = JSONObject.parseObject(dbService.get("list"), new TypeReference<List<DemoPO>>() {
            });
            System.out.println(list);
        }
        return dbService.get(key);
    }

    // http://127.0.0.1:18888/data/all
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        return dbService.getAll();
    }
}
