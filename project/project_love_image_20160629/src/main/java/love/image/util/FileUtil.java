package love.image.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

public class FileUtil {

	private final static Log logger = LogFactory.getLog(FileUtil.class);

	public static String getUUID(String fileName) {
		return UUID.randomUUID().toString().replaceAll("-", "")
				+ fileName.substring(fileName.lastIndexOf("."));
	}

	public static String uploading(JSONObject para, File file,
			String fileFileName, HttpServletRequest req)
			throws FileNotFoundException, IOException {
		String pathname = req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + req.getContextPath() + File.separator;

		String path = req.getSession().getServletContext()
				.getRealPath(File.separator);
		String uid = String.valueOf(para.get("uid"));
		// 头像存储绝对路径
		String dirs = "";
		String userPhotoPath = "";
		// 可以对拿到的文件名进行后缀名的校验(暂无)
		if (null != uid && !"".equals(uid)) {
			userPhotoPath = "userphotos" + File.separator + para.get("uid");
			dirs = path + userPhotoPath;
		}
		InputStream is = new FileInputStream(file);
		File descFile = new File(dirs); // 服务器存储图片路径
		if (!descFile.exists()) {
			descFile.mkdirs();
		}
		String UUIDName = FileUtil.getUUID(fileFileName);
		OutputStream os = new FileOutputStream(dirs + File.separator + UUIDName);
		byte[] buffer = new byte[1024];
		int length = 0;
		while (-1 != (length = (is.read(buffer)))) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();
		// 保存文件路径
		String user_photo_path = (pathname + userPhotoPath + File.separator + UUIDName)
				.replace("\\", "/");
		System.out.println(user_photo_path);
		return user_photo_path;
	}

	/**
	 * 覆盖上传
	 * 
	 * @param userPhotoPath
	 * @param file
	 * @param filename
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static String uploading(String userPhotoPath, InputStream file,
			String filename, HttpServletRequest req) throws Exception {
		String path = req.getSession().getServletContext()
				.getRealPath(File.separator);

		String dirs = "";
		// 可以对拿到的文件名进行后缀名的校验(暂无)
		dirs = path + userPhotoPath;
		if (file != null) {
			InputStream is = file;
			File descFile = new File(dirs); // 服务器存储图片路径
			if (!descFile.exists()) {
				descFile.mkdirs();
			}
			System.out.println("descFile:" + descFile);
			String UUIDName = filename;
			OutputStream os = new FileOutputStream(dirs + File.separator
					+ UUIDName);
			byte[] buffer = new byte[1024];
			int length = 0;
			while (-1 != (length = (is.read(buffer)))) {
				os.write(buffer, 0, length);
			}
			if (is != null)
				is.close();
			if (os != null)
				os.close();
			// 保存文件路径
			// System.out.println(req.getContextPath());
			String user_photo_path = (req.getContextPath() + File.separator
					+ userPhotoPath + File.separator + UUIDName).replace("\\",
					"/");
			logger.info("保存路径-->" + user_photo_path);
			return user_photo_path;
		}
		return null;
	}

	/**
	 * 不覆盖上传
	 * 
	 * @param userPhotoPath
	 * @param file
	 * @param filename
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static String uploadingStepImg(String userPhotoPath,
			InputStream file, String filename, HttpServletRequest req)
			throws Exception {
		// String pathname = req.getScheme() + "://"
		// + req.getServerName() + ":"
		// + req.getServerPort()
		// + req.getContextPath()
		// + File.separator;
		// System.out.println(req.getScheme());
		// System.out.println(req.getServerName());
		// System.out.println(req.getServerPort());
		// System.out.println(req.getContextPath());

		String path = req.getSession().getServletContext()
				.getRealPath(File.separator);

		String dirs = "";
		// 可以对拿到的文件名进行后缀名的校验(暂无)
		dirs = path + userPhotoPath;
		if (file != null) {
			InputStream is = file;
			File descFile = new File(dirs); // 服务器存储图片路径
			if (!descFile.exists()) {
				descFile.mkdirs();
			}
			String UUIDName = FileUtil.getUUID(filename);
			OutputStream os = new FileOutputStream(dirs + File.separator
					+ UUIDName);
			byte[] buffer = new byte[1024];
			int length = 0;
			while (-1 != (length = (is.read(buffer)))) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
			// 保存文件路径
			String user_photo_path = (req.getContextPath() + File.separator
					+ userPhotoPath + File.separator + UUIDName).replace("\\",
					"/");
			logger.info("保存路径-->" + user_photo_path);
			return user_photo_path;
		}
		return null;
	}

}
