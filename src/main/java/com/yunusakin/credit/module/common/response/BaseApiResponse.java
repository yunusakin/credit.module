package com.yunusakin.credit.module.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseApiResponse<T> {
    @JsonProperty("status")
    private HttpStatus status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;
}
