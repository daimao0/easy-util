package com.easy.util;


import com.easy.constant.StrConstant;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 操作csv文件；
 * 为了解决字符串中包含分隔符，导致解析失败所以设置了边界符，
 *
 * @author: daimao
 * @date: 2024-10-10 19:34
 */
@SuppressWarnings("ALL")
public class CsvUtils {

    private CsvUtils() {
    }

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ",";
    /**
     * 单元格边界
     */
    private static final String BORDER = "&$";

    /**
     * 生成CSV字符
     *
     * @param content csv文本内容
     * @return CSV
     */
    public static List<Map<String, Object>> parseCsv(String content) {
        if (StrUtils.isBlank(content)) {
            return Collections.emptyList();
        }
        String[] arr = content.split("\n");
        //先生成表头
        String head = arr[0];
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            String line = arr[i];
            Map<String, Object> item = parseLine(head, line);
            rows.add(item);
        }
        return rows;
    }

    /**
     * 生成CSV字符
     *
     * @param data 数据
     * @return CSV
     */
    public static String csvStr(List<Map<String, Object>> data) {
        if (CollUtils.isEmpty(data)) {
            return null;
        }
        //先生成表头
        String headStr = CsvUtils.toHeadStr(data.get(0));
        //文件内容
        StringBuilder content = new StringBuilder();
        content.append(headStr).append("\n");
        for (Map<String, Object> datum : data) {
            String lineStr = CsvUtils.toLineStr(datum);
            content.append(lineStr).append("\n");
        }
        return content.toString();
    }

    /**
     * 将一行元素生成表头
     *
     * @param row 一行数据
     * @return 表头
     */
    public static String toHeadStr(Map<String, Object> row) {
        return toLineStr(new ArrayList<>(row.keySet()));
    }

    /**
     * 将一行元素生成字符串
     *
     * @param row 一行数据
     * @return 字符串
     */
    public static String toLineStr(Map<String, Object> row) {
        List<Object> lines = new ArrayList<>();
        row.forEach((k, v) -> lines.add(v));
        return toLineStr(lines);
    }

    /**
     * 将一行元素生成字符串
     *
     * @param row 一行数据
     * @return 字符串
     */
    public static String toLineStr(List<Object> row) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row.size(); i++) {
            Object cell = row.get(i);
            String cellStr = cell == null ? StrConstant.BLANK : cell.toString();
            if (cellStr.contains(SEPARATOR)) {
                sb.append(BORDER).append(cellStr).append(BORDER);
            } else {
                sb.append(cellStr);
            }
            if (i < row.size() - 1) {
                sb.append(SEPARATOR);
            }
        }
        return sb.toString();
    }

    /**
     * 解析row
     *
     * @param head   表头
     * @param rowStr 行字符
     * @return 解析行
     */
    public static Map<String, Object> parseLine(String head, String rowStr) {
        List<Object> headArr = splitLine(head);
        List<Object> rowArr = splitLine(rowStr);
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < headArr.size(); i++) {
            result.put(headArr.get(i).toString(), rowArr.get(i));
        }
        return result;
    }

    public static List<Object> splitLine(String row) {
        String[] arr = row.split(SEPARATOR);
        List<Object> result = new ArrayList<>();
        //跨界元素 (cell中存在分割符)当sb有值时表示跨界拼接没有结束
        StringBuilder sb = new StringBuilder();
        for (String item : arr) {
            //跨界的开始
            if (item.length() > 2 && BORDER.equals(item.substring(0, BORDER.length()))) {
                sb.append(item.substring(BORDER.length()));
                continue;
            }
            //跨界的结束
            if (item.length() > 2 && BORDER.equals(item.substring(item.length() - BORDER.length()))) {
                sb.append(item, 0, item.length() - BORDER.length());
                result.add(sb.toString());
                sb = new StringBuilder();
                continue;
            }
            //如果存在跨界
            if (sb.length() != 0) {
                sb.append(item);
                continue;
            }
            //如果不存在跨界
            result.add(item);
        }
        return result;
    }

    /**
     * 生成CSV字符
     *
     * @param data      数据
     * @param spearator 分隔符
     * @return CSV字符
     */
    public static String genCsvStr(List<Map<String, Object>> data, String spearator) {
        if (CollUtil.isEmpty(data)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        //生成headStr
        List<String> head = new ArrayList<>(data.get(0).keySet());
        for (int i = 0; i < head.size(); i++) {
            sb.append(head.get(i));
            if (i < head.size() - 1) {
                sb.append(spearator);
            }
        }
        sb.append("\n");
        //formdata
        data.forEach(item -> {
            int size = item.size();
            AtomicInteger i = new AtomicInteger();
            item.forEach((k, v) -> {
                sb.append(v);
                if (i.getAndIncrement() < size - 1) {
                    sb.append(spearator);
                }
            });
            sb.append("\n");
        });
        return sb.toString();
    }

    /**
     * 解析CSV
     *
     * @param content   csv内容
     * @param spearator 分隔符
     * @return 解析后的数据
     */
    public static List<Map<String, Object>> parseCsv(String content, String spearator) {
        if (StrUtils.isBlank(content)){
            return Collections.emptyList();
        }
        String[] lineArr = content.split("\n");
        if (lineArr.length == 0) {
            return Collections.emptyList();
        }
        List<Map<String,Object>> data = new ArrayList<>(lineArr.length-1);
        String head = lineArr[0];
        String[] keys = head.split(spearator);
        for (int i = 1; i < lineArr.length; i++) {
            String line = lineArr[i];
            String[] cell = line.split(spearator);
            Map<String, Object> lineMap = new HashMap<>();
            for (int k = 0; k < cell.length; k++) {
                lineMap.put(keys[k],cell[k]);
            }
            data.add(lineMap);
        }
        return data;
    }
}
