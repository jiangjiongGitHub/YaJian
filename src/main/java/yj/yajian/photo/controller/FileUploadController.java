package yj.yajian.photo.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        model.addAttribute("files", fileEntitys);
        return "/photo/upload";
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
            uploadRenameSave(file);

            redirectAttributes.addFlashAttribute("message",
                    "文件上传成功: '" + file.getOriginalFilename() + "'");

            String photo = dbService.get("photo");
            dbService.put("photo", (photo == null ? "" : photo) + "," + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "文件上传失败");
        }

        return "redirect:/photo/index";
    }

    private void uploadRenameSave(MultipartFile file) throws IOException {
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
    }

    private void saveDB(String newFileName) {
        FileEntity build = FileEntity.builder().name(newFileName).build();
        List<String> tags = Arrays.asList(newFileName.substring(0, 4));
        build.setTags(tags);
        dbService.put(newFileName, JSONObject.toJSONString(build));
    }
}
