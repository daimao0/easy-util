package com.easy.field;

import com.easy.util.DateUtils;
import com.easy.util.StrUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

/**
 * 常规字段类型
 *
 * @author daimao
 * @date 2024-12-18 14:50
 */
@AllArgsConstructor
@Getter
public enum CommonFieldType {

    NUMBER("number", "数字类型"),
    STRING("string", "字符串类型"),
    DATE("date", "日期类型");

    private final String code;

    private final String desc;

    public static CommonFieldType get(String code) {
        for (CommonFieldType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }


    /**
     * 将值解析对常规字段类型
     *
     * @param value 值
     * @return 常规字段类型
     */
    public static CommonFieldType parse(Object value) {
        if (value == null) {
            // 或者选择一个默认值，比如 STRING 或者抛出异常
            return null;
        }
        // 检查数字类型
        if (value instanceof Number) {
            return CommonFieldType.NUMBER;
        }
        String valStr = value.toString();
        if (StrUtils.isNumber(valStr)) {
            return CommonFieldType.NUMBER;
        }
        //检查日期类型
        Date date = DateUtils.toDate(valStr);
        if (date!=null){
            return CommonFieldType.DATE;
        }
        // 默认情况下返回字符串类型
        return CommonFieldType.STRING;
    }

}
