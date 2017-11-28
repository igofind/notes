package com.jb.mains.util;
/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: StringPad
 * History：
 * <author>			<time>			<version>			<desc>
 * sunpeng 		2017/9/6			V1.0 		字符串处理工具类
 */

public class StringPad {

    /**
     * 字符串填充-右侧填充
     *
     * @param src 原字符串
     * @param ch  填充的字符
     * @param len 填充后的长度
     * @return 填充后的字符串
     */
    public static String padRight(String src, char ch, int len) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }
        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * 字符串填充-左侧填充
     *
     * @param src 原字符串
     * @param ch  填充的字符
     * @param len 填充后的长度
     * @return 填充后的字符串
     */
    public static String padLeft(String src, char ch, int len) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }
}
