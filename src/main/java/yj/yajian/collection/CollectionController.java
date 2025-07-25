package yj.yajian.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import yj.yajian.db.service.FileDatabaseService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// 控制器
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private static final String perfix = "collection-";

    @Resource
    private FileDatabaseService dbService;

    @GetMapping
    public List<CollectionItem> getAllCollections(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "100") int size) {
        // 返回所有收藏，支持分页
        List<CollectionItem> allItems = dbService.getAll().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(perfix))
                .map(entry -> JSONObject.parseObject(entry.getValue().toString(), CollectionItem.class))
                .collect(Collectors.toList());

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allItems.size());

        if (fromIndex > allItems.size()) {
            return new ArrayList<>();
        }

        // if (true) {
        //     CollectionItem c = new CollectionItem();
        //     c.setId(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
        //     c.setType("文章");
        //     c.setTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //     dbService.put(perfix + c.getId(), JSONObject.toJSONString(c));
        // }

        return allItems.subList(fromIndex, toIndex);
    }

    @GetMapping("/{id}")
    public CollectionItem getCollectionById(@PathVariable Long id) {
        // 返回指定ID的收藏
        String s = dbService.get(perfix + id);
        if (StringUtils.isEmpty(s)) {
            return new CollectionItem();
        }
        return JSON.parseObject(s, CollectionItem.class);
    }

    @PostMapping
    public CollectionItem createCollection(@RequestBody CollectionItem item) {
        // 创建新收藏
        item.setId(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
        dbService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        return item;
    }

    @PutMapping("/{id}")
    public CollectionItem updateCollection(@PathVariable Long id, @RequestBody CollectionItem item) {
        // 更新收藏（主要用于文本编辑）
        item.setId(id);
        dbService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        return item;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCollection(@PathVariable Long id) {
        // 删除收藏
        dbService.remove(perfix + id);
        return ResponseEntity.ok(id);
    }
}
