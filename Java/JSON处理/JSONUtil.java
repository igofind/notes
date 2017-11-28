/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: JSONUtil.java
 * <author>         <date>          <version>           <desc>
 * sunpeng          2017/11/22         V1.0            TODO
 */
package com.jb.f1.grid.util;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONUtil {

    public static Object getNodeValue(JsonNode node) {

        if (node == null || node.isNull()) {
            return null;
        }

        if (node.isTextual()) {
            return node.textValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else if (node.isNumber()) {
            return node.numberValue();
        } else {
            return node;
        }
    }

    public static String getNodeText(JsonNode node) {

        if (node == null || node.isNull()) {
            return null;
        }

        if (!node.isTextual()) {
            return "";
        }
        return node.textValue();
    }
}
