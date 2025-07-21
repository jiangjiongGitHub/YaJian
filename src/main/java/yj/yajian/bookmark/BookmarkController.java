package yj.yajian.bookmark;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import yj.yajian.db.service.FileDatabaseService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Resource
    private FileDatabaseService dbService;

    private void initBookmark() {
        // 清空数据库
        dbService.getAll().forEach((key, value) -> {
            if (key.startsWith("bookmark-")) {
                dbService.remove(key);
            }
        });

        // 从resources/bookmark.md中获取书签列表，并写入数据库
        ClassPathResource resource = new ClassPathResource("bookmark.md");
        byte[] bytes = null;
        try {
            bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String content = new String(bytes, StandardCharsets.UTF_8);

        String[] lines = content.split("\\r?\\n");
        // 跳过表头和分隔线（前两行）
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty())
                continue;

            // 解析表格行格式：| 标题 | URL | 次数 |
            String[] parts = line.split("\\|");
            if (parts.length < 4)
                continue;  // 确保有4个部分（首尾空列+3数据列）

            String title = parts[1].trim();
            String url = parts[2].trim();
            int count = Integer.parseInt(StringUtils.isEmpty(parts[3].trim()) ? "0" : parts[3].trim());

            Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), title, url, count);
            while (dbService.get("bookmark-" + bookmark.getId()) != null) {
                bookmark.setId(bookmark.getId() + 1);
            }
            dbService.put("bookmark-" + bookmark.getId(), JSONObject.toJSONString(bookmark));
        }

        // 修复书签列表
        fixBookmark();
    }

    // http://127.0.0.1:18888/api/bookmarks/init
    @GetMapping("/init")
    public void init() {
        // 初始化书签列表
        initBookmark();
    }

    @GetMapping
    public List<Bookmark> getBookmarks() {
        List<Bookmark> bookmarklist = new ArrayList<>();
        String bookmarks = dbService.get("bookmarklist");
        if (bookmarks != null) {
            List<Long> idList = JSONObject.parseObject(bookmarks, new TypeReference<List<Long>>() {
            });
            for (Long id : idList) {
                bookmarklist.add(JSONObject.parseObject(dbService.get("bookmark-" + id), Bookmark.class));
            }
        }

        if (bookmarklist.isEmpty()) {
            addBookmark();
        }

        return bookmarklist;
    }

    private void addBookmark() {
        Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), "百度", "https://www.baidu.com", 0);
        dbService.put("bookmark-" + bookmark.getId(), JSONObject.toJSONString(bookmark));

        String bookmarklist = dbService.get("bookmarklist");
        List<Long> bookmarks;
        if (bookmarklist == null) {
            bookmarks = new ArrayList<>();
            bookmarks.add(bookmark.getId());
        } else {
            bookmarks = JSONObject.parseObject(bookmarklist, new TypeReference<List<Long>>() {
            });
            bookmarks.add(bookmark.getId());
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
        if (newCount == bookmark.getCount() + 1) {
            bookmark.setCount(newCount);
        } else {
            bookmark.setCount(bookmark.getCount() + 1);
        }

        dbService.put("bookmark-" + id, JSONObject.toJSONString(bookmark));
        return ResponseEntity.ok(bookmark);
    }

    private void fixBookmark() {
        // 获取所有书签
        List<Long> idList = new ArrayList<>();
        dbService.getAll().forEach((key, value) -> {
            if (key.startsWith("bookmark-")) {
                Bookmark bookmark = JSONObject.parseObject(value.toString(), Bookmark.class);
                idList.add(bookmark.getId());
            }
        });
        dbService.put("bookmarklist", JSONObject.toJSONString(idList));
    }

}

