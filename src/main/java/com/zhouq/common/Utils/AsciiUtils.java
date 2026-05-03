package com.zhouq.common.Utils;

/**
 * <p>
 *
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/9/17 11:03
 */

public class AsciiUtils {

    /**
     * 将16进制 ASCII转成字符串
     *
     * @param hexValue
     * @return
     */
    public static String asciiToString(String hexValue) {
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < hexValue.length(); i += 2) {
            sbu.append((char) Integer.parseInt(hexValue.substring(i, i + 2), 16));
        }
        return sbu.toString();
    }

    /**
     * 将字符串转成ASCII
     *
     * @param strValue
     * @return
     */
    public static String stringToAscii(String strValue) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = strValue.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]);
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }
}

