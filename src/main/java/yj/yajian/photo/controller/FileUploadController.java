package yj.yajian.photo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;
import yj.yajian.db.service.FileDatabaseService;
import yj.yajian.photo.entity.FileEntity;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/photo")
public class FileUploadController {

    // 上传文件存储目录 ${app.upload.folder:./uploads}
    @Value("${app.upload.folder}")
    private String UPLOADED_FOLDER;

    @Resource
    private FileDatabaseService dbService;

    private void checkUploadFolder() {
        // 创建上传目录(如果不存在)
        Path uploadPath = Paths.get(UPLOADED_FOLDER);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/files")
    @ResponseBody
    public Map<String, Object> getFiles(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String tagFilter) throws IOException {

        // 打印参数
        log.info("startDate: {}, endDate: {}, tagFilter: {}", startDate, endDate, tagFilter);

        checkUploadFolder();

        // 获取已上传文件列表
        List<String> files = new ArrayList<>();
        Files.walk(Paths.get(UPLOADED_FOLDER))
                .filter(Files::isRegularFile)
                .forEach(file -> files.add(file.getFileName().toString()));

        List<FileEntity> fileEntitys = new ArrayList<>();
        // 循环files转换为FileEntity实体添加到fileEntitys
        for (String file : files) {
            FileEntity fileEntity = JSONObject.parseObject(
                    dbService.get(file) == null ? "{}" : dbService.get(file),
                    FileEntity.class);
            if (fileEntity.getName() == null) {
                fileEntity.setName(file);
            }
            fileEntitys.add(fileEntity);
        }

        // 示例：将字符串格式日期转为 LocalDate 进行比较
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = StringUtils.isEmpty(startDate) ? null : LocalDate.parse(startDate, formatter);
        LocalDate end = StringUtils.isEmpty(endDate) ? null : LocalDate.parse(endDate, formatter);
        LocalDateTime startOfDay = start != null ? start.atStartOfDay() : null;
        LocalDateTime endOfDay = end != null ? end.atTime(23, 59, 59, 999_999_999) : null;

        // 支持多个标签筛选
        Set<String> tagFilters = new HashSet<>();
        if (!StringUtils.isEmpty(tagFilter)) {
            tagFilters.addAll(Arrays.asList(tagFilter.split(",")));
        }

        List<FileEntity> collect = fileEntitys.stream()
                .filter(f -> StringUtils.isEmpty(tagFilter) ||
                        (f.getTags() != null && f.getTags().stream().anyMatch(tagFilters::contains)))
                .filter(f -> start == null || end == null || isWithinDateRange(f, startOfDay, endOfDay))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("fileEntitys", collect);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("tagFilter", tagFilter);

        return response;
    }

    @Scheduled(initialDelay = 45000, fixedDelay = 60000) // 删除脏数据
    public void autoSave() {
        deleteUnuseData();
        log.info("Executing delete unuse data at: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @GetMapping("/tags")
    @ResponseBody
    public Map<String, List<String>> getTags() {
        // 从dbService获取所有数据，遍历获取标签
        Map<String, Object> all = dbService.getAll();
        if (all == null) {
            return new HashMap<>();
        }
        List<String> tags = all.values().stream()
                // 判断是json，是字符串的过滤
                .filter(value -> value instanceof String && value.toString().startsWith("{"))
                .map(value -> JSONObject.parseObject(value.toString(), FileEntity.class).getTags())
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        Map<String, List<String>> response = new HashMap<>();
        response.put("tags", tags);
        return response;
    }

    private boolean isWithinDateRange(FileEntity f, LocalDateTime start, LocalDateTime end) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(Paths.get(UPLOADED_FOLDER + File.separator + f.getName()));
            LocalDateTime fileDate = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return fileDate.isAfter(start) && fileDate.isBefore(end);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 请求URL：http://127.0.0.1:18888/photo/upload
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> singleFileUpload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择一个文件上传");
            return response;
        }

        try {
            checkUploadFolder();

            // 保存文件
            String name = uploadSave(file);

            response.put("success", true);
            response.put("message", "文件上传成功：" + name);
            response.put("fileName", name);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
        }

        return response;
    }

    private String uploadSave(MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 获取文件字节和临时路径
        byte[] bytes = file.getBytes();

        // 时间格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");

        // 构建新文件名（保留扩展名）
        String fileExt = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 新文件名：年月日时分秒 + 扩展名
        int i = 0;
        String newFileName = sdf.format(new Date()) + String.format("%05d", ++i) + fileExt.toLowerCase();
        Path path = Paths.get(UPLOADED_FOLDER + File.separator + newFileName);
        while (Files.exists(path)) {
            newFileName = sdf.format(new Date()) + String.format("%05d", ++i) + fileExt.toLowerCase();
            path = Paths.get(UPLOADED_FOLDER + File.separator + newFileName);
        }

        // 真正写入最终文件
        Files.write(path, bytes);

        saveDB(newFileName);
        return newFileName;
    }

    private void saveDB(String newFileName) {
        FileEntity build = FileEntity.builder().name(newFileName).build();
        List<String> tags = Arrays.asList(newFileName.substring(0, 4));
        build.setTags(tags);
        dbService.put(newFileName, JSONObject.toJSONString(build));
    }

    @PostMapping("/addTag")
    @ResponseBody
    public Map<String, Object> addTag(@RequestBody Map<String, String> payload) {
        String fileName = payload.get("fileName");
        String tag = payload.get("tag");

        // 获取现有 FileEntity
        String json = dbService.get(fileName);
        FileEntity fileEntity = JSONObject.parseObject(json, FileEntity.class);

        if (fileEntity == null) {
            fileEntity = FileEntity.builder().name(fileName).tags(new ArrayList<>()).build();
        }

        if (fileEntity.getTags() == null) {
            fileEntity.setTags(new ArrayList<>());
        }

        // 添加新标签
        if (!fileEntity.getTags().contains(tag)) {
            fileEntity.getTags().add(tag);
        }

        // 保存回数据库
        dbService.put(fileName, JSONObject.toJSONString(fileEntity));

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "操作成功");
        return result;
    }

    @PostMapping("/removeTag")
    @ResponseBody
    public Map<String, Object> removeTag(@RequestBody Map<String, String> payload) {
        String fileName = payload.get("fileName");
        String tag = payload.get("tag");

        // 获取现有 FileEntity
        String json = dbService.get(fileName);
        FileEntity fileEntity = JSONObject.parseObject(json, FileEntity.class);

        if (fileEntity == null) {
            fileEntity = FileEntity.builder().name(fileName).tags(new ArrayList<>()).build();
        }

        if (fileEntity.getTags() == null) {
            fileEntity.setTags(new ArrayList<>());
        }

        // 移除标签
        fileEntity.getTags().remove(tag);

        // 保存回数据库
        dbService.put(fileName, JSONObject.toJSONString(fileEntity));

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "操作成功");
        return result;
    }

    @PostMapping("/renameFile")
    public ResponseEntity<Map<String, Object>> renameFile(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String oldFileName = payload.get("oldFileName");
        rename(response, oldFileName);

        return ResponseEntity.ok(response);
    }

    private void rename(Map<String, Object> response, String oldFileName) {
        File oldFile = new File(UPLOADED_FOLDER + File.separator + oldFileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");

        // 如果文件名符合规范，就不修改：规范为 yyyyMMdd.HHmmss.SSS.jpg 或 yyyyMMdd.HHmmss.SSS.XXX.jpg 除后缀名外，都是数字或小数点 其中 yyyyMMdd.HHmmss.SSS 为时间，XXX为随机数字，可以是1到3位，后缀名一般是常用图片格式
        if (oldFileName.matches("^\\d{8}\\.\\d{6}\\.\\d{8}\\.(?i)(jpg|jpeg|png|gif|bmp|ico|svg|webp)$")
                && oldFileName.substring(0, Math.min(oldFileName.length(), 19)).equals(sdf.format(new Date(oldFile.lastModified())))) {
            response.put("success", false);
            response.put("message", "文件名符合规范：" + oldFileName);
            return;
        }

        if (oldFileName.startsWith("collection_upload-")) {
            response.put("success", false);
            response.put("message", "文件名符合规范：" + oldFileName);
            return;
        }

        // 获取 oldFileName 的数据库数据
        String json = dbService.get(oldFileName);
        FileEntity fileEntity = JSONObject.parseObject(json, FileEntity.class);
        if (fileEntity == null) {
            fileEntity = FileEntity.builder().name(oldFileName).build();
        }

        if (oldFile.exists()) {
            int i = 0;
            // 把名称改为 yyyyMMdd.HHmmss.SSS 格式，取文件创建日期，如果日期不存在，则取当前日期
            String newFileName = sdf.format(new Date(oldFile.lastModified())) + String.format("%05d", ++i) + oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();
            File newFile = new File(UPLOADED_FOLDER + File.separator + newFileName);

            while (newFile.exists()) {
                newFileName = sdf.format(new Date(oldFile.lastModified())) + String.format("%05d", ++i) + oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();
                newFile = new File(UPLOADED_FOLDER + File.separator + newFileName);
            }

            oldFile.renameTo(newFile);

            // 保存 newFileName 的数据库数据
            fileEntity.setName(newFileName);
            dbService.put(newFileName, JSONObject.toJSONString(fileEntity));

            response.put("success", true);
            response.put("message", "文件名已更改为：" + newFileName);
        } else {
            response.put("success", false);
            response.put("message", "文件不存在：" + oldFileName);
        }
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public Map<String, Object> deleteFile(@RequestBody Map<String, String> payload) {
        Map<String, Object> result = new HashMap<>();
        String fileName = payload.get("fileName");

        File fileToDelete = new File(UPLOADED_FOLDER + File.separator + fileName);
        if (!fileToDelete.exists()) {
            result.put("success", false);
            result.put("message", "文件不存在：" + fileName);
            return result;
        }

        try {
            // 删除文件
            Files.delete(fileToDelete.toPath());

            // 从数据库中移除记录
            dbService.remove(fileName);

            result.put("success", true);
            result.put("message", "文件删除成功：" + fileName);
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "文件删除失败：" + e.getMessage());
        }

        return result;
    }

    private void deleteUnuseData() {
        // 遍历 UPLOADED_FOLDER 文件夹下文件，已知文件名作为 dbService map 的 key, dbService 有很多多余数据，如果 key 不在 UPLOADED_FOLDER 里的文件列表中，则删除
        Map<String, Object> all = dbService.getAll();
        try {
            checkUploadFolder();

            Set<String> existingFiles = Files.walk(Paths.get(UPLOADED_FOLDER))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());

            all.forEach((k, v) -> {
                if (!existingFiles.contains(k)) {
                    if (!k.startsWith("DATE")
                            && !k.startsWith("bookmark-")
                            && !k.startsWith("collection-")
                            && !k.startsWith("collection_upload-")
                            && !k.startsWith("keep-alive")) {
                        dbService.remove(k);
                        log.info("RM = {}", k);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 请求URL：http://127.0.0.1:18888/photo/batchRename
    @PostMapping("/batchRename")
    @ResponseBody
    public Map<String, Object> batchRename() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "成功：");

            // 获取文件列表
            List<String> fileList = new ArrayList<>();
            Files.walk(Paths.get(UPLOADED_FOLDER))
                    .filter(Files::isRegularFile)
                    .forEach(file -> fileList.add(file.getFileName().toString()));
            fileList.forEach(fileName -> {
                Map<String, Object> m = new HashMap<>();
                rename(m, fileName);
                log.info("修改文件名：{}", m);
                result.put("message", result.get("message") + "\n" + m.get("message") + "; ");
            });
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "失败：" + e.getMessage());
        }

        return result;
    }

}
