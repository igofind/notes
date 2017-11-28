/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: FileUtil.java
 * <author>         <date>          <version>           <desc>
 * sunpeng          2017/11/28         V1.0            TODO
 */
package com.sun.file;

import java.io.*;

public class FileUtil {

    private static final int SIZE = 1024 * 8;

    /**
     * 以字节流的方式读取到字符串。
     *
     * @param is 输入流
     * @return 字符串
     */
    public static String readBytesToString(InputStream is) {
        return new String(readToBytes(is));
    }

    /**
     * 以字节流的方式读取到字符串。
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String readBytesToString(InputStream is, String charsetName) {
        try {
            return new String(readToBytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以字符流的方式读取到字符串。
     *
     * @param file 文件
     * @return 字符串
     */
    public static String readCharsToString(File file) {
        try {
            return readCharsToString(new FileInputStream(file), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以字符流的方式读取到字符串。
     *
     * @param file    文件
     * @param charset 编码格式
     * @return 字符串
     */
    public static String readCharsToString(File file, String charset) {
        try {
            return readCharsToString(new FileInputStream(file), charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以字符流的方式读取到字符串。默认编码
     *
     * @param is 输入流
     * @return 字符串
     */
    public static String readCharsToString(InputStream is) {
        return new String(readToChars(is, null));
    }

    /**
     * 以字符流的方式读取到字符串。
     *
     * @param is          输入流
     * @param charsetName 编码
     * @return 字符串
     */
    public static String readCharsToString(InputStream is, String charsetName) {
        return new String(readToChars(is, charsetName));
    }

    /**
     * 以字节流的方式读取到字符串。
     *
     * @param file 文件
     * @return 字节数组
     */
    public static byte[] readToBytes(File file) {
        try {
            return readToBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以字节流的方式读取到字符串。
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] readToBytes(InputStream is) {
        byte[] bytes = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(is);

            byte[] cbuf = new byte[SIZE];
            int len;
            ByteArrayOutputStream outWriter = new ByteArrayOutputStream();
            while ((len = bis.read(cbuf)) != -1) {
                outWriter.write(cbuf, 0, len);
            }
            outWriter.flush();

            bis.close();
            is.close();

            bytes = outWriter.toByteArray();
            outWriter.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 以字符流的方式读取到字符串。
     *
     * @param file        文件
     * @param charsetName 编码
     * @return 字符数组
     */
    public static char[] readToChars(File file, String charsetName) {
        try {
            return readToChars(new FileInputStream(file), charsetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 以字符流的方式读取到字符串。
     *
     * @param is          输入流
     * @param charsetName 编码
     * @return 字符数组
     */
    public static char[] readToChars(InputStream is, String charsetName) {
        char[] chars = null;
        try {
            InputStreamReader isr = null;
            if (charsetName == null) {
                isr = new InputStreamReader(is);
            } else {
                isr = new InputStreamReader(is, charsetName);
            }
            BufferedReader br = new BufferedReader(isr);
            char[] cbuf = new char[SIZE];
            int len;
            CharArrayWriter outWriter = new CharArrayWriter();
            while ((len = br.read(cbuf)) != -1) {
                outWriter.write(cbuf, 0, len);
            }
            outWriter.flush();

            br.close();
            isr.close();
            is.close();

            chars = outWriter.toCharArray();
            outWriter.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chars;
    }

    /**
     * 通过字节输出流输出bytes
     *
     * @param os   输出流
     * @param text 字节数组
     */
    public static void writeForBytes(OutputStream os, byte[] text) {
        writeForBytes(os, text, 0, text.length);
    }

    /**
     * 通过字节输出流输出bytes
     *
     * @param os     输出流
     * @param text   字节数组
     * @param off    数组起始下标
     * @param lenght 长度
     */
    public static void writeForBytes(OutputStream os, byte[] text, int off, int lenght) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(os);
            bos.write(text, off, lenght);

            bos.flush();
            bos.close();
            os.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过字符输出流输出chars
     *
     * @param os          输出流
     * @param text        字节数组
     * @param off         数组起始下标
     * @param lenght      长度
     * @param charsetName 编码方式
     */
    public static void writeForChars(OutputStream os, char[] text, int off, int lenght, String charsetName) {
        try {
            OutputStreamWriter osw = null;

            if (charsetName == null) {
                osw = new OutputStreamWriter(os);
            } else {
                osw = new OutputStreamWriter(os, charsetName);
            }
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(text, off, lenght);

            bw.flush();
            bw.close();
            osw.close();
            os.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过字符输出流输出chars
     *
     * @param os          输出流
     * @param text        字节数组
     * @param charsetName 编码方式
     */
    public static void writeForChars(OutputStream os, char[] text, String charsetName) {
        writeForChars(os, text, 0, text.length, charsetName);
    }

    /**
     * 将字符串以默认编码写入文件
     *
     * @param file 文件
     * @param text 字符串
     */
    public static void writeForString(File file, boolean append, String text) {
        writeForString(file, append, text, 0, text.length(), null);
    }

    /**
     * 将字符串写入文件
     *
     * @param file        文件
     * @param append      是否追加
     * @param text        字符串
     * @param off         起始下标
     * @param lenght      长度
     * @param charsetName 编码名称
     */
    public static void writeForString(File file, boolean append, String text, int off, int lenght, String charsetName) {
        try {
            writeForString(new FileOutputStream(file, append), text, off, lenght, charsetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串写入文件（默认覆盖）
     *
     * @param file        文件
     * @param append      是否追加
     * @param text        字符串
     * @param charsetName 编码名称
     */
    public static void writeForString(File file, boolean append, String text, String charsetName) {
        writeForString(file, append, text, 0, text.length(), charsetName);
    }

    /**
     * 将字符串以默认编码写入文件
     *
     * @param file 文件
     * @param text 字符串
     */
    public static void writeForString(File file, String text) {
        writeForString(file, false, text, 0, text.length(), null);
    }

    /**
     * 将字符串写入文件（默认覆盖）
     *
     * @param file        文件
     * @param text        字符串
     * @param charsetName 编码名称
     */
    public static void writeForString(File file, String text, String charsetName) {
        writeForString(file, false, text, 0, text.length(), charsetName);
    }

    /**
     * 字符输出流输出字符串
     *
     * @param os          输出流
     * @param text        字符串
     * @param off         起始下标
     * @param lenght      长度
     * @param charsetName 编码
     */
    public static void writeForString(OutputStream os, String text, int off, int lenght, String charsetName) {
        try {
            OutputStreamWriter osw = null;

            if (charsetName == null) {
                osw = new OutputStreamWriter(os);
            } else {
                osw = new OutputStreamWriter(os, charsetName);
            }
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(text, off, lenght);

            bw.flush();
            bw.close();
            osw.close();
            os.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符输出流输出字符串
     *
     * @param os          输出流
     * @param text        字符串
     * @param charsetName 编码
     */
    public static void writeForString(OutputStream os, String text, String charsetName) {
        writeForString(os, text, 0, text.length(), charsetName);
    }

    private FileUtil() {
    }

}