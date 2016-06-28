package love.image.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * 
 * @author roc
 * 
 *         AES128 算法，加密模式为ECB，填充模式为 pkcs7（实际就是pkcs5）
 * 
 *
 */
public class AESUtil {
	private static final String AES = "AES";
	private static final String CRYPT_KEY = "sdzgjbz121";
	private static final String ENCODING = "UTF-8";

	public static SecretKeySpec getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance(AES);
			// 防止linux下 随机生成key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(strKey.getBytes());
			_generator.init(128, secureRandom);
			SecretKey secretKey = _generator.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
			return key;
		} catch (Exception e) {
			throw new RuntimeException("初始化密钥出现异常 ");
		}
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			Cipher cipher = Cipher.getInstance(AES);// 创建密码器
			byte[] byteContent = content.getBytes(ENCODING);
			cipher.init(Cipher.ENCRYPT_MODE, getKey(password));// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {
		try {
			Cipher cipher = Cipher.getInstance(AES);// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, getKey(password));// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 加密字符串
	 * 
	 * @param content
	 *            需要加密的内容
	 * @return
	 */
	public static String encryptStr(String content) {
		byte[] encryptResult = encrypt(content, CRYPT_KEY);
		return parseByte2HexStr(encryptResult);
	}

	/**
	 * 解密字符串
	 * 
	 * @param content
	 *            需要解密的内容
	 * @return
	 */
	public static String decryptStr(String content) {
		byte[] decryptFrom = parseHexStr2Byte(content);
		return new String(decrypt(decryptFrom, CRYPT_KEY));
	}

	public static void main(String[] args) {
		String content = "info";
		System.out.println("加密前：" + content);
		System.out.println("加密后：" + encryptStr(content));
		System.out.println("解密后：" + decryptStr(encryptStr(content)));
		for (int i = 1; i <= 200; i++) {
			System.out.println("JDJ" + i + "/" + "MMJ" + i);
		}
	}
}
