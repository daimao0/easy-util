package com.easy.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yanchenyang17958
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Page<T> {
    /**
     * 返回值
     */
    private List<T> data;

    /**
     * 当前页
     */
    private Long page;
    /**
     * 页面大小
     */
    private Long size;
    /**
     * 总量
     */
    private Long total;


    public static <T> Page<T> toPage(Long page, Long size, Long total, List<T> data) {
        Page<T> twoWheelOrderInfoPage = new Page<>();
        twoWheelOrderInfoPage.setPage(page);
        twoWheelOrderInfoPage.setSize(size);
        twoWheelOrderInfoPage.setTotal(total);
        twoWheelOrderInfoPage.setData(data);
        return twoWheelOrderInfoPage;
    }
}