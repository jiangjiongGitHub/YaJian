package yj.yajian.collection;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// 控制器
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    @GetMapping
    public List<CollectionItem> getAllCollections() {
        // 返回所有收藏
        return null;
    }

    @GetMapping("/{id}")
    public CollectionItem getCollectionById(@PathVariable Long id) {
        // 返回指定ID的收藏
        return null;
    }

    @PostMapping
    public CollectionItem createCollection(@RequestBody CollectionItem item) {
        // 创建新收藏
        return null;
    }

    @PutMapping("/{id}")
    public CollectionItem updateCollection(@PathVariable Long id, @RequestBody CollectionItem item) {
        // 更新收藏（主要用于文本编辑）
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteCollection(@PathVariable Long id) {
        // 删除收藏
    }
}
