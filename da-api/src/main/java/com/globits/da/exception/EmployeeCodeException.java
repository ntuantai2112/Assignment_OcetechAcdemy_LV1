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

    SUCCESS(200, "Success fully!"),
    EMPLOYEE_CODE_ALREADY_EXISTS(101, "The employee code already exists"),
    EMPLOYEE_CODE_MIN_LENGTH(102, "EMPLOYEE CODE MINIMUM 6 CHARACTERS"),
    EMPLOYEE_CODE_MAX_LENGTH(103, "EMPLOYEE CODE MAXIMUM 10 CHARACTERS"),
    EMPLOYEE_CODE_NOT_NULL(104, "EMPLOYEE CODE NOT NULL"),
    EMPLOYEE_CODE_NOT_SPACE(120, "EMPLOYEE Code must not contain spaces or special characters"),
    EMPLOYEE_NAME_NOT_NULL(105, "EMPLOYEE NAME CAN NOT BE LEFT BLANK"),
    EMPLOYEE_EMAIL_FORMAT(106, "INVALID EMAIL FORMAT"),
    EMPLOYEE_PHONE_FORMAT(107, "Phone number must contain only numbers and be at most 11 digits"),
    EMPLOYEE_AGE_VALUE(108, "Age must be a positive number!"),
    EMPLOYEE_AGE_NOT_NULL(108, "Age must be a positive number!"),
    EMPLOYEE_AGE_MIN(126, "Employee age must be at least 18 years old!"),
    EMPLOYEE_AGE_MAX(128, "Employee age must not exceed 60 years old!"),
    EMPLOYEE_EMAIL_NOT_NULL(109, "EMPLOYEE EMAIL CAN NOT BE LEFT BLANK"),
    EMPLOYEE_PHONE_NOT_NULL(110, "EMPLOYEE PHONE NOT BE LEFT BLANK"),
    EMPLOYEE_NOT_NULL(114, "EMPLOYEE NOT BE LEFT BLANK"),
    EMPLOYEE_NOT_FOUND(113, "THE EMPLOYEE NOT FOUND!"),
    EMPLOYEES_NOT_FOUND(116, "No employees found!"),
    EMPLOYEES_NOT_IMPLEMENT_EXPORT(122, "No employees found for export!"),


    PROVINCE_NOT_FOUND(130, "The province not found!"),
    PROVINCE_NOT_NULL(133, "Province ID is required!"),
    DISTRICT_NOT_FOUND(131, "The district not found!"),
    DISTRICT_NOT_NULL(134, "District ID is required!"),
    DISTRICT_NOT_PROVINCE(138, "District does not belong to the given Province!"),
    COMMUNE_NOT_FOUND(132, "The commune not found!"),
    COMMUNE_NOT_NULL(135, "Commune ID is required!"),
    COMMUNE_NOT_DISTRICT(136, "Commune does not belong to the given District!"),


    INVALID_KEY(115, "INVALID KEY!"),
    INVALID_EMPLOYEE_IMPORT(124, "INVALID IMPORT EXCEL EMPLOYEES!"),
    INVALID_FILE_EXCEL(116, "Failed to parse Excel file!"),
    INVALID_MIN_AGE(119, "Minimum age is 18!"),
    EMPLOYEE_INVALID_MAX_AGE(120, "Maximum age is 60!"),
    IO_EXCEPTION(118, "IO_EXCEPTION!"),
    DISTRICT_OR_PROVINCE_NOT_FOUND(112, "Invalid district or province hierarchy!");


    private int code;
    private String message;


}
