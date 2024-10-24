package com.arnugroho.be_dss.model.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class DefaultPageResponse<T> {
    private int status;
    private String message;
    private T data;
    private Long total;
    private Boolean success;
//    private Long totalItems;
    private int page;
    private int pageSize;


    public DefaultPageResponse(int status, String message, T data, int page, int pageSize, Long total, Boolean success) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.success = success;
    }

    public static <T> DefaultPageResponse<T> ok(T data, int page, int pageSize, Long totalItems) {
        return new DefaultPageResponse(HttpStatus.OK.value(), "SUCCESS", data, page, pageSize, totalItems, Boolean.TRUE);
    }

    public static <T> DefaultPageResponse<T> noauth(String message){
        return new DefaultPageResponse(HttpStatus.UNAUTHORIZED.value(), message, null, 0, 0, 0L, Boolean.FALSE);
    }

    public static <T> DefaultPageResponse<T> redirectMoved(T data, int page, int pageSize, Long totalItems) {
        return new DefaultPageResponse(HttpStatus.MOVED_PERMANENTLY.value(), "SUCCESS", data, page, pageSize, totalItems, Boolean.TRUE);
    }
}
