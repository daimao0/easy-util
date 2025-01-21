package com.easy.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import lombok.Getter;

import java.util.*;

/**
 *
 * @author daimao
 * @date 2024/11/27
 */
public class NoModeDataListener extends AnalysisEventListener<Map<String, String>> {

    /**
     * -- GETTER --
     * 获得表格数据
     */
    @Getter
    private final List<Map<String, Object>> dataList = new ArrayList<>(100);
    private Map<Integer, String> head = null;

    @Override
    public void invoke(Map<String, String> data, AnalysisContext context) {
        //列头名称重复次数
        Map<String, Integer> headCount = new HashMap<>();
        // 这里可以获取到每行的数据，data是一个Map，key是列名，value是单元格值
        Map<String, Object> rowMap = new LinkedHashMap<>();

        //遍历当前row
        head.forEach((k,v)->{
            String cell = data.get(k) == null ? "" : data.get(k);
            char ch = (char) ('A' + k);
            String headValue = head.get(k) == null ? ch + "1" : head.get(k);
            if (headCount.get(headValue) == null) {
                headCount.put(headValue, 0);
            } else {
                headCount.put(headValue, headCount.get(headValue) + 1);
            }
            Integer count = headCount.get(headValue);
            if (count >= 1) {
                headValue = String.format("%s(%d)", headValue, count);
            }
            rowMap.put(headValue, cell);
            rowMap.remove(k);
        });
        dataList.add(rowMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成后的回调，可以在这里做后续处理
    }

    /**
     * 这里会一行行的返回头
     *
     * @param headMap 表头
     * @param context S上下文
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        this.head = ConverterUtils.convertToStringMap(headMap, context);
    }

}