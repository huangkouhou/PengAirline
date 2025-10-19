package com.peng.PengAirline.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String ex){
        super(ex);
    }
}
