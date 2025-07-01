package yj.yajian.photo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yj.yajian.db.service.FileDatabaseService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/photo")
public class FileUploadController {

    // @Value("${app.upload.folder}")
    // private String uploadFolder;

    @Resource
    private FileDatabaseService dbService;

    // 上传文件存储目录
    private static String UPLOADED_FOLDER = "src/main/resources/static/uploads/";

    private void checkUploadFolder() {
        // 创建上传目录(如果不存在)
        Path uploadPath = Paths.get(UPLOADED_FOLDER);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {}
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

        model.addAttribute("files", files);
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
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

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
}
