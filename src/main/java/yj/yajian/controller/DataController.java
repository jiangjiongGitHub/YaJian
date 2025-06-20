package yj.yajian.controller;

import org.springframework.web.bind.annotation.*;
import yj.yajian.service.FileDatabaseService;

import java.util.Map;

@RestController
@RequestMapping("/data")
public class DataController {

    private final FileDatabaseService dbService;

    public DataController(FileDatabaseService dbService) {
        this.dbService = dbService;
    }

    // http://127.0.0.1:18888/data/put?key=123&value=456
    @GetMapping("/put")
    public Boolean saveData(@RequestParam String key, @RequestParam String value) {
        dbService.put(key, value);
        return true;
    }

    // http://127.0.0.1:18888/data/get?key=123
    @GetMapping("/get")
    public Object getData(@RequestParam String key) {
        return dbService.get(key);
    }

    // http://127.0.0.1:18888/data/all
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        return dbService.getAll();
    }
}
