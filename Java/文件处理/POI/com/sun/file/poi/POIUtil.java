package com.jb.mains.util;/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: com.sun.file.poi.POIUtil.java
 * <author>         <date>          <version>           <desc>
 * sunpeng          2017/11/28         V1.0            POI工具类
 */

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POIUtil {

    /**
     * 工作表内单元格取值
     *
     * @param sheet 要取值的工作表
     * @return Map
     */
    public static Map readSheetToMap(Sheet sheet) {
        Map map = new HashMap();
        // 遍历所有行
        for (Row row : sheet) {
            // 遍历所有列
            for (Cell cell : row) {
                // 判断单元格的类型
                CellReference cellRef = new CellReference(row.getRowNum(), cell
                        .getColumnIndex());
                map.put(cellRef.formatAsString(), getCellValue(cell));
            }
        }
        return map;
    }

    /**
     * 按行取工作表内前5000行的数据，包含表头。
     *
     * @param sheet 要取值的工作表
     * @return Map
     */
    public static List<Object[]> readSheetToList(Sheet sheet) {

        return readSheetToListWithLimit(sheet, 5000);
    }

    /**
     * 按行取工作表内所有行的数据，包含表头。
     *
     * @param sheet   要取值的工作表
     * @param rowSize 读取的行数
     * @return Map
     */
    public static List<Object[]> readSheetToListWithLimit(Sheet sheet, int rowSize) {

        // 记录所有行的数据
        List<Object[]> rows = new ArrayList<Object[]>();

        // 列数
        int colCnt = 0;

        // 行数
        int excelRowCnt = sheet.getLastRowNum() + 1;
        if (excelRowCnt > rowSize) {
            excelRowCnt = rowSize;
        }

        // 遍历所有行
        for (int i = 0; i < excelRowCnt; i++) {
            // 记录每一行的数据
            List<Object> list = new ArrayList<Object>();

            Row row = sheet.getRow(i);
            if (i == 0) {
                // 列数
                colCnt = row.getLastCellNum();
            }
            // 遍历所有列
            for (int j = 0; j < colCnt; j++) {
                list.add(getCellValue(row.getCell(j)));
            }
            rows.add(list.toArray());
        }
        return rows;
    }

    /**
     * 获取单元格的值
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            // 字符串
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString().trim();
                break;
            // 数字
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            // boolean
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            // 方程式
            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            // 错误的值
            case Cell.CELL_TYPE_ERROR:
                // 处理数据时，如果为空的话，判断为非法值
                value = null;
                break;
            // 空值
            default:
                value = null;
        }
        return value;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        InputStream is = new FileInputStream("C:\\Users\\Administrator\\Downloads\\电厂导出及导入模板.xlsx");
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        List<Object[]> list = POIUtil.readSheetToList(sheet);
        for (Object[] arr : list) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            for (Object o : arr) {
                System.out.println(o);
            }
        }
    }
}
