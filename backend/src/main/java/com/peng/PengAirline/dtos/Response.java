package com.peng.PengAirline.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)//表示当该对象被序列化成 JSON 时，值为 null 的字段不会出现在输出中。
public class Response<T>{

    private int statusCode;
    private String message;
    private T data;//<T> 表示 data 的类型是可变的

}


