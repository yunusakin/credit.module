package com.yunusakin.credit.module.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class BaseApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
}
