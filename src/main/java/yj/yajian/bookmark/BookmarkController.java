package yj.yajian.bookmark;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yj.yajian.db.service.FileDatabaseService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Resource
    private FileDatabaseService dbService;

    @GetMapping
    public List<Bookmark> getBookmarks() {
        // 新增一个书签
        addBookmark();

        String bookmarks = dbService.get("bookmarklist");
        return JSONObject.parseObject(bookmarks, new TypeReference<List<Bookmark>>() {
        });
    }

    private void addBookmark() {
        Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), "百度", "https://www.baidu.com", 0);
        dbService.put("bookmark-" + bookmark.getId(), JSONObject.toJSONString(bookmark));
        String bookmarklist = dbService.get("bookmarklist");
        List<Bookmark> bookmarks;
        if (bookmarklist == null) {
            bookmarks = new ArrayList<>();
            bookmarks.add(bookmark);
        } else {
            bookmarks = JSONObject.parseObject(bookmarklist, new TypeReference<List<Bookmark>>() {
            });
            bookmarks.add(bookmark);
        }
        dbService.put("bookmarklist", JSONObject.toJSONString(bookmarks));
    }

    // 更新书签点击次数
    @PostMapping("/{id}/count")
    public ResponseEntity<Bookmark> updateBookmarkCount(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Bookmark bookmark = JSONObject.parseObject(dbService.get("bookmark-" + id), Bookmark.class);
        if (bookmark == null) {
            return ResponseEntity.notFound().build();
        }

        Integer newCount = payload.get("count");
        if (newCount == null) {
            return ResponseEntity.badRequest().build();
        }

        bookmark.setCount(newCount);
        return ResponseEntity.ok(bookmark);
    }

}

