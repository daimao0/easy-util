package com.easy.api;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页响应
 *
 * @author: daimao
 * @date: 2023-01-04 2:38 PM
 */
@Data
@NoArgsConstructor
public class PageResponse<T> {
    /**
     * 响应码
     */
    private long code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回值
     */
    private Page<T> data;

    PageResponse(long code, String message, Page<T> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> PageResponse<T> success(Page<T> data) {
        return new PageResponse<>(CommonResponseCode.SUCCESS.getCode(), CommonResponseCode.SUCCESS.getMessage(), data);
    }


    public static <T> PageResponse<T> success(Page<T> data, String message) {
        return new PageResponse<>(CommonResponseCode.SUCCESS.getCode(), message, data);
    }


    public static <T> PageResponse<T> failed(String message) {
        return new PageResponse<>(CommonResponseCode.FAILED.getCode(), message, null);
    }

    public static <T> PageResponse<T> failed(CommonResponseCode commonResponseCode) {
        return new PageResponse<>(commonResponseCode.getCode(), commonResponseCode.getMessage(), null);
    }

    public static <T> PageResponse<T> unauthorized(String message) {
        return new PageResponse<>(CommonResponseCode.UNAUTHORIZED.getCode(), message, null);
    }


    public static <T> PageResponse<T> forbidden(String message) {
        return new PageResponse<>(CommonResponseCode.FORBIDDEN.getCode(), message, null);
    }

}
