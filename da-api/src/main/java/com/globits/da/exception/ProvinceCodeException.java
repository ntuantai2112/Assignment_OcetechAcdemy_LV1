package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ProvinceCodeException {

    //+) Kiểm tra code để code không được trùng
    //	+) Sử dụng tream() để bỏ khoảng trắng ở 2 đầu
    //	+) Bắt độ dài tổi thiểu và tối đa của code
    //	+) Bắt code phải nhập vào không được null

    SUCCESS(200, "Success fully!"),
    PROVINCE_CODE_ALREADY_EXISTS(101, "The PROVINCE already exists"),

    PROVINCE_NOT_NULL(114, "PROVINCE NOT BE LEFT BLANK"),
    PROVINCES_NOT_FOUND(116, "No matching results were found!"),
    COMMUNE_NOT_FOUND(111, "COMMUNE NOT FOUND!"),
    DISTRICT_OR_PROVINCE_NOT_FOUND(112, "Invalid district or province hierarchy!");


    private int code;
    private String message;


}
