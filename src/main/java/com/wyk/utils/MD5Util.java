package com.lvmama.tnt.cps.utils;

import com.lvmama.tnt.cps.utils.distributor.Base64Utils;
import com.lvmama.tnt.cps.utils.distributor.SecurityUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 加密工具类
 */
public class MD5Util {

    public static String KEY = "MD5";
    private static int BUFF_SIZE = 1024;

    /**
     * md5 加密方法
     * @param s
     * @return
     */

    public final static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',	'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] digest(byte[] bs) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(KEY);
            md5.update(bs);
            return md5.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] digest(String plain) {
        return digest(SecurityUtils.toBytes(plain));
    }

    public static String digestAsHex(byte[] bs) {
        return Hex.encodeHexString(digest(bs));
    }

    public static String digestAsBase64(byte[] bs) {
        return Base64Utils.encodeAsString(digest(bs));
    }

    public static String digestAsBase64(InputStream input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(KEY);
            byte[] buff = new byte[BUFF_SIZE];
            int readBytes = 0;
            while ((readBytes = input.read(buff)) > 0) {
                md5.update(buff, 0, readBytes);
            }
            return Base64Utils.encodeAsString(md5.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    public static String digestAsBase64(File file) {
        if (!file.exists())
            return null;
        try {
            return digestAsBase64(new FileInputStream(file));
        } catch (FileNotFoundException localFileNotFoundException) {
        }
        return null;
    }

    public static String digestAsBase64(String plain) {
        return Base64Utils.encodeAsString(digest(SecurityUtils.toBytes(plain)));
    }


}
