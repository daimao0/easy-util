package com.easy.util;


import java.util.*;

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
            String cellStr = row.get(i).toString();
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
            if (sb.length()!=0) {
                sb.append(item);
                continue;
            }
            //如果不存在跨界
            result.add(item);
        }
        return result;
    }
}
