package com.codefarme.imchat.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Result<T> implements Serializable {

    private Integer code;//0代表请求成功 1,代表请求失败
    private String message;
    private T data = null;


}
