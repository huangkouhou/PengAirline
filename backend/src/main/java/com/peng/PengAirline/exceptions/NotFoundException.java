package com.peng.PengAirline.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String ex){
        super(ex);//调用父类构造方法(将传入的错误信息字符串ex，传递给RuntimeException的构造方法进行保存。)
    }
}
