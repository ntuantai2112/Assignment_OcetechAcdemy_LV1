package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum EmployeeCodeException {

    //+) Kiểm tra code để code không được trùng
    //	+) Sử dụng tream() để bỏ khoảng trắng ở 2 đầu
    //	+) Bắt độ dài tổi thiểu và tối đa của code
    //	+) Bắt code phải nhập vào không được null

    SUCCESS(200,"Success fully!"),
    EMPLOYEE_CODE_ALREADY_EXISTS(101,"The employee already exists"),
    EMPLOYEE_CODE_MIN_LENGTH(102,"EMPLOYEE CODE MINIMUM 6 CHARACTERS"),
    EMPLOYEE_CODE_MAX_LENGTH(103,"EMPLOYEE CODE MAXIMUM 10 CHARACTERS"),
    EMPLOYEE_CODE_NOT_NULL(104,"EMPLOYEE CODE NOT NULL"),
    EMPLOYEE_NAME_NOT_NULL(105,"EMPLOYEE NAME CAN NOT BE LEFT BLANK"),
    EMPLOYEE_EMAIL_FORMAT(106,"INVALID EMAIL FORMAT"),
    EMPLOYEE_PHONE_FORMAT(107,"INVALID PHONE NUMBER"),
    EMPLOYEE_AGE_VALUE(108,"INVALID AGE VALUE"),
    EMPLOYEE_EMAIL_NOT_NULL(109,"EMPLOYEE EMAIL CAN NOT BE LEFT BLANK"),
    EMPLOYEE_PHONE_NOT_NULL(110,"EMPLOYEE PHONE NOT BE LEFT BLANK");


    private int code;
    private String message;


}
