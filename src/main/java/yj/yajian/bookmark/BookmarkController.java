package yj.yajian.bookmark;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import yj.yajian.db.service.FileDatabaseService;
import yj.yajian.dbtemp.TempFileDatabaseService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private static final String perfix = "bookmark-";

    @Resource
    private FileDatabaseService fileDatabaseService;

    @Resource
    private TempFileDatabaseService tempFileDatabaseService;

    private void initBookmark() {
        // 清空数据库
        fileDatabaseService.getAll().forEach((key, value) -> {
            if (key.startsWith(perfix)) {
                fileDatabaseService.remove(key);
            }
        });
        // 从resources/bookmark.md中获取书签列表
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
                continue;
            // 获取标题和URL
            String title = parts[1].trim();
            String url = parts[2].trim();
            // 获取访问次数
            int count = Integer.parseInt(StringUtils.isEmpty(parts[3].trim()) ? "0" : parts[3].trim());
            // 构建实体类保存
            Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), title, url, count);
            while (fileDatabaseService.get(perfix + bookmark.getId()) != null) {
                bookmark.setId(bookmark.getId() + 1);
            }
            fileDatabaseService.put(perfix + bookmark.getId(), JSONObject.toJSONString(bookmark));
        }
    }

    // http://127.0.0.1:18888/api/bookmarks/init
    @GetMapping("/init")
    public String initBookMark(String date) {
        // 初始化书签列表
        if (new SimpleDateFormat("yyyyMMdd").format(new Date()).equals(date)) {
            initBookmark();
        }
        return "success";
    }

    @GetMapping
    public List<Bookmark> getBookmarks() {
        List<Bookmark> allWithPerfix = getAllWithPerfix(perfix);
        if (allWithPerfix.isEmpty()) {
            allWithPerfix.add(addPhotoBookmark());
            allWithPerfix.add(addCollectionBookmark());
        }
        return allWithPerfix;
    }

    private Bookmark addPhotoBookmark() {
        Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), "照片管理", "http://127.0.0.1:18888/photo/upload.html", 0);
        while (fileDatabaseService.get(perfix + bookmark.getId()) != null) {
            bookmark.setId(bookmark.getId() + 1);
        }
        fileDatabaseService.put(perfix + bookmark.getId(), JSONObject.toJSONString(bookmark));
        return bookmark;
    }

    private Bookmark addCollectionBookmark() {
        Bookmark bookmark = new Bookmark(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())), "收藏管理", "http://127.0.0.1:18888/collection/index.html", 0);
        while (fileDatabaseService.get(perfix + bookmark.getId()) != null) {
            bookmark.setId(bookmark.getId() + 1);
        }
        fileDatabaseService.put(perfix + bookmark.getId(), JSONObject.toJSONString(bookmark));
        return bookmark;
    }

    // 更新书签点击次数
    @PostMapping("/{id}/count")
    public ResponseEntity<Bookmark> updateBookmarkCount(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Bookmark bookmark = JSONObject.parseObject(fileDatabaseService.get(perfix + id), Bookmark.class);
        if (bookmark == null) {
            return ResponseEntity.notFound().build();
        }
        // 获取入参点击次数
        Integer newCount = payload.get("count");
        if (newCount == null) {
            return ResponseEntity.badRequest().build();
        }
        // 判断前端参数是否正确
        if (newCount == bookmark.getCount() + 1) {
            bookmark.setCount(newCount);
        } else {
            bookmark.setCount(bookmark.getCount() + 1);
        }
        fileDatabaseService.put(perfix + id, JSONObject.toJSONString(bookmark));
        return ResponseEntity.ok(bookmark);
    }

    private List<Bookmark> getAllWithPerfix(String perfix) {
        // 获取所有书签
        List<Bookmark> list = new ArrayList<>();
        fileDatabaseService.getAll().forEach((key, value) -> {
            if (key.startsWith(perfix)) {
                Bookmark bookmark = JSONObject.parseObject(value.toString(), Bookmark.class);
                list.add(bookmark);
            }
        });
        return list;
    }

    // 新增书签
    @PostMapping
    public ResponseEntity<Bookmark> createBookmark(@RequestBody Bookmark bookmark) {
        if (bookmark.getId() == null) {
            bookmark.setId(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
        }
        bookmark.setCount(0);
        fileDatabaseService.put(perfix + bookmark.getId(), JSONObject.toJSONString(bookmark));
        fileDatabaseService.autoSave();
        tempSave(bookmark);
        return ResponseEntity.ok(bookmark);
    }

    // 修改书签
    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> updateBookmark(@PathVariable Long id, @RequestBody Bookmark updated) {
        Bookmark existing = JSONObject.parseObject(fileDatabaseService.get(perfix + id), Bookmark.class);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setTitle(updated.getTitle());
        existing.setUrl(updated.getUrl());
        fileDatabaseService.put(perfix + id, JSONObject.toJSONString(existing));
        fileDatabaseService.autoSave();
        tempSave(existing);
        return ResponseEntity.ok(existing);
    }

    // 删除书签
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long id) {
        if (fileDatabaseService.get(perfix + id) == null) {
            return ResponseEntity.notFound().build();
        }
        fileDatabaseService.remove(perfix + id);
        return ResponseEntity.ok().build();
    }

    private synchronized void tempSave(Bookmark item) {
        Set<String> keys = new HashSet<>();
        keys.add(TempFileDatabaseService.key);
        item.setKeys(keys);

        tempFileDatabaseService.loadDataFromFile();
        tempFileDatabaseService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        tempFileDatabaseService.saveToFile();
    }

}

