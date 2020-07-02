package com.zxj.gtauth.tool;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 加密算法 AES 对称算法
 * 对称加密算法也就是加密和解密用相同的密钥，具体的加密流程如下图：
 */
public class Aes {

    //私钥
    private static String key = "1234567812345678";
    //初始向量
    private static String iv = "1234567812345678";
    //加密算法 需要和 php js 语言保持一致
    private static String method = "AES/CBC/NoPadding";
    /**
     * 加密 String iv = "1234567812345678"; method = "AES/CBC/NoPadding"; key = "1234567812345678";
     * param 参数 String 类型 或者 {"name":"cdd"}
     * 线上环境统一以json 格式的字符串为主
     */
    public static String encrypt(String param) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(method);

            int blockSize = cipher.getBlockSize();

//            byte[] dataBytes = param.getBytes("utf-8");
            byte[] dataBytes = param.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new sun.misc.BASE64Encoder().encode(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(key.getBytes()));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
////            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//            Cipher cipher = Cipher.getInstance(method);
//            byte[] byteContent = param.getBytes("utf-8");
//            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
//            byte[] result = cipher.doFinal(byteContent);
//            return result.toString(); // 加密

    /**
     * 解密
     * param 已加密的参数
     */
    public static String desEncrypt(String param) throws Exception {
        try
        {
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(param);
            Cipher cipher = Cipher.getInstance(method);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            System.out.println(originalString);
            return  originalString.trim();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
