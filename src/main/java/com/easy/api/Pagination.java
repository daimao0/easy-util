package com.easy.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * @author: daimao
 * @date: 2024-10-21 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Pagination {

    /**
     * 当前页
     */
    private Long page;
    /**
     * 页面大小
     */
    private Long size;

    public Long getPage() {
        return page == null || page <= 0 ? 1 : page;
    }

    public Long getSize() {
        return size == null || size <= 0 ? 10 : size;
    }
}
