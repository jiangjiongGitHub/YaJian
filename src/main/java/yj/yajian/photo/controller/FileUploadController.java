package yj.yajian.photo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/photo")
public class FileUploadController {

    // 上传文件存储目录
    @Value("${app.upload.folder}") // ${app.upload.folder:src/main/resources/static/uploads}
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

    // http://127.0.0.1:18888/photo/index
    @GetMapping("/index")
    public String index(Model model) throws IOException {
        checkUploadFolder();

        // 获取已上传文件列表
        List<String> files = new ArrayList<>();
        Files.walk(Paths.get(UPLOADED_FOLDER))
                .filter(Files::isRegularFile)
                .forEach(file -> files.add(file.getFileName().toString()));

        List<FileEntity> fileEntitys = new ArrayList<>();
        // 循环files转换为FileEntity实体添加到fileEntitys
        for (String file : files) {
            FileEntity fileEntity = JSONObject.parseObject(dbService.get(file) == null ? "{}" : dbService.get(file), FileEntity.class);
            if (fileEntity.getName() == null) {
                fileEntity.setName(file);
            }
            fileEntitys.add(fileEntity);
        }
        model.addAttribute("fileEntitys", fileEntitys);
        return "/photo/upload";
    }

    @GetMapping("/indexSearch")
    public String indexSearch(@RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate,
                              @RequestParam(required = false) String tagFilter,
                              Model model) throws IOException {
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
            FileEntity fileEntity = JSONObject.parseObject(dbService.get(file) == null ? "{}" : dbService.get(file), FileEntity.class);
            if (fileEntity.getName() == null) {
                fileEntity.setName(file);
            }
            fileEntitys.add(fileEntity);
        }

        // 示例：将字符串格式日期转为 LocalDate 进行比较
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = StringUtils.isEmpty(startDate) ? null : LocalDate.parse(startDate, formatter);
        LocalDate end = StringUtils.isEmpty(endDate) ? null : LocalDate.parse(endDate, formatter);

        List<FileEntity> collect = fileEntitys.stream()
                .filter(f -> f.getTags() != null)
                .filter(f -> StringUtils.isEmpty(tagFilter) || f.getTags().contains(tagFilter))
                .filter(f -> start == null || end == null || isWithinDateRange(f, start, end)) // 自定义方法判断是否在时间范围内
                .collect(Collectors.toList());
        model.addAttribute("fileEntitys", collect);
        return "/photo/upload";
    }

    private boolean isWithinDateRange(FileEntity f, LocalDate start, LocalDate end) {
        try {
            FileTime fileTime = Files.getLastModifiedTime(Paths.get(UPLOADED_FOLDER + File.separator + f.getName()));
            LocalDate fileDate = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return fileDate.isAfter(start) && fileDate.isBefore(end);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 请求URL：http://127.0.0.1:18888/photo/upload
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "请选择一个文件上传");
            return "redirect:/photo/index";
        }

        try {
            checkUploadFolder();

            // 保存文件
            String name = uploadRenameSave(file);

            redirectAttributes.addFlashAttribute("message", "文件上传成功：" + name);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "文件上传失败");
        }

        return "redirect:/photo/index";
    }

    private String uploadRenameSave(MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 获取文件字节和临时路径
        byte[] bytes = file.getBytes();
        Path tempPath = Files.createTempFile("upload-", ".tmp"); // 创建临时文件用于探测属性
        Files.write(tempPath, bytes); // 写入内容以便获取属性

        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(tempPath, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取创建时间或回退到当前时间
        FileTime creationTime = attrs != null ? attrs.creationTime() : FileTime.fromMillis(System.currentTimeMillis());

        // 时间格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
        String formattedTime = sdf.format(new Date(creationTime.toMillis()));

        // 构建新文件名（保留扩展名）
        String fileExt = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 新文件名：年月日时分秒 + 扩展名
        String newFileName = formattedTime + fileExt.toLowerCase();
        Path path = Paths.get(UPLOADED_FOLDER + File.separator + newFileName);

        // 真正写入最终文件
        Files.write(path, bytes);

        // 删除临时文件
        Files.deleteIfExists(tempPath);

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
    public String addTag(@RequestParam("fileName") String fileName,
                         @RequestParam("tag") String tag) {
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

        return "redirect:/photo/index";
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

        // 如果文件名符合规范，就不修改：规范为 yyyyMMdd.HHmmss.SSS.jpg 或 yyyyMMdd.HHmmss.SSS.XXX.jpg 除后缀名外，都是数字或小数点 其中 yyyyMMdd.HHmmss.SSS 为时间，XXX为随机数字，可以是1到3位，后缀名一般是常用图片格式
        if (oldFileName.matches("^\\d{8}\\.\\d{6}\\.\\d{3}(\\.\\d{1,3})?\\.(?i)(jpg|jpeg|png|gif|bmp|webp)$")) {
            response.put("success", false);
            response.put("message", "文件名符合规范：" + oldFileName);
            return ResponseEntity.ok(response);
        }


        File oldFile = new File(UPLOADED_FOLDER + File.separator + oldFileName);
        if (oldFile.exists()) {
            // 把名称改为 yyyyMMdd.HHmmss.SSS 格式，取文件创建日期，如果日期不存在，则取当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
            String newFileName = sdf.format(new Date(oldFile.lastModified())) + oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();
            File newFile = new File(UPLOADED_FOLDER + File.separator + newFileName);

            if (newFile.exists()) {
                response.put("success", false);
                response.put("message", "文件已存在：" + newFileName);

                // 修改文件名为当前文件名后面加上三位随机数字名称，注意后缀名
                String newFileName2 = sdf.format(new Date(oldFile.lastModified())) + "." + (int) (Math.random() * 1000) + oldFileName.substring(oldFileName.lastIndexOf(".")).toLowerCase();
                File newFile2 = new File(UPLOADED_FOLDER + File.separator + newFileName2);
                if (oldFile.renameTo(newFile2)) {
                    response.put("success", true);
                    response.put("message", response.get("message") + "，文件名已更改为：" + newFileName2);
                } else {
                    response.put("success", false);
                    response.put("message", response.get("message") + "，无法更改文件名为：" + newFileName2);
                }
            } else {
                if (oldFile.renameTo(newFile)) {
                    response.put("success", true);
                    response.put("message", "文件名已更改为：" + newFileName);
                } else {
                    response.put("success", false);
                    response.put("message", "无法更改文件名为：" + newFileName);
                }
            }
        } else {
            response.put("success", false);
            response.put("message", "文件不存在：" + oldFileName);
        }

        return ResponseEntity.ok(response);
    }
}
