package com.jb.f1.load.util;/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: OSUtils.java
 * History：
 * <author>			<time>			<version>			<desc>
 * sunpeng 		2017/9/26			V1.0 		        工具类
 */

public class OSUtils {
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
	
    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
