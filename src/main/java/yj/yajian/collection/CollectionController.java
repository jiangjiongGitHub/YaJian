package yj.yajian.collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import yj.yajian.db.service.FileDatabaseService;
import yj.yajian.dbtemp.TempFileDatabaseService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// 控制器
@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private static final String perfix = "collection-";

    @Resource
    private FileDatabaseService fileDatabaseService;

    @Resource
    private TempFileDatabaseService tempFileDatabaseService;

    // 上传文件存储目录 ${app.upload.folder:./uploads}
    @Value("${app.upload.folder}")
    private String UPLOAD_FOLDER;

    @GetMapping
    public List<CollectionItem> getAllCollections(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "100") int size) {
        // 返回所有收藏，支持分页
        List<CollectionItem> allItems = fileDatabaseService.getAll().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(perfix))
                .map(entry -> JSONObject.parseObject(entry.getValue().toString(), CollectionItem.class))
                .sorted((item1, item2) -> Long.compare(item2.getId(), item1.getId())) // 按ID倒序排序
                .collect(Collectors.toList());

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allItems.size());

        if (fromIndex >= allItems.size()) {
            return new ArrayList<>();
        }

        return allItems.subList(fromIndex, toIndex);
    }

    @GetMapping("/{id}")
    public CollectionItem getCollectionById(@PathVariable Long id) {
        // 返回指定ID的收藏
        String s = fileDatabaseService.get(perfix + id);
        if (StringUtils.isEmpty(s)) {
            return new CollectionItem();
        }
        return JSON.parseObject(s, CollectionItem.class);
    }

    // 添加支持文件上传的创建方法
    @PostMapping("/with-file")
    public ResponseEntity<CollectionItem> createCollectionWithFile(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("type") String type,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        CollectionItem item = new CollectionItem();
        item.setTitle(title);
        item.setContent(content);
        item.setType(type);
        item.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 处理文件上传
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = saveFile(file);
                // 将文件路径添加到内容中
                item.setContent(content + "\n" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(500).build();
            }
        }

        item.setId(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
        fileDatabaseService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        fileDatabaseService.autoSave();
        tempSave(item);
        return ResponseEntity.ok(item);
    }

    // 添加支持文件上传的更新方法
    @PutMapping("/with-file/{id}")
    public ResponseEntity<CollectionItem> updateCollectionWithFile(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("type") String type,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        CollectionItem item = new CollectionItem();
        item.setId(id);
        item.setTitle(title);
        item.setContent(content);
        item.setType(type);
        item.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 处理文件上传
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = saveFile(file);
                // 将文件路径添加到内容中
                item.setContent(content + "\n" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(500).build();
            }
        }

        fileDatabaseService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        fileDatabaseService.autoSave();
        tempSave(item);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCollection(@PathVariable Long id) {
        // 删除收藏
        fileDatabaseService.remove(perfix + id);
        return ResponseEntity.ok(id);
    }

    // 文件保存方法
    private String saveFile(MultipartFile file) throws IOException {
        // 创建上传目录
        Path uploadPath = Paths.get(UPLOAD_FOLDER);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = "collection_upload-" + System.currentTimeMillis() + extension;

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());
        // 根据 UPLOADED_FOLDER + "/" + fileName 构造html img标签
        String filePathName = UPLOAD_FOLDER.replace(".", "") + "/" + fileName;
        // style="max-width: 60%; height: auto; max-height: 300px; margin: 10px; border: 1px solid #ddd; border-radius: 4px; object-fit: contain;"
        return "<img src=\"" + filePathName + "\" alt=\"" + fileName + "\" class=\"img-collection\">";
    }

    @PutMapping("/{id}/pin")
    public ResponseEntity<CollectionItem> pinCollection(@PathVariable Long id) {
        // 获取原始收藏项
        String s = fileDatabaseService.get(perfix + id);
        if (StringUtils.isEmpty(s)) {
            return ResponseEntity.notFound().build();
        }

        CollectionItem item = JSON.parseObject(s, CollectionItem.class);

        // 生成新的ID（当前时间戳），实现置顶效果
        Long newId = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        item.setId(newId);

        // 更新时间
        item.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 保存更新后的项
        fileDatabaseService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        fileDatabaseService.autoSave();

        return ResponseEntity.ok(item);
    }

    private synchronized void tempSave(CollectionItem item) {
        Set<String> keys = new HashSet<>();
        keys.add(TempFileDatabaseService.key);
        item.setKeys(keys);

        tempFileDatabaseService.loadDataFromFile();
        tempFileDatabaseService.put(perfix + item.getId(), JSONObject.toJSONString(item));
        tempFileDatabaseService.saveToFile();
    }

    @GetMapping("/count")
    public int getCollectionCount() {
        // 返回收藏总数
        return (int) fileDatabaseService.getAll().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(perfix))
                .count();
    }

}
