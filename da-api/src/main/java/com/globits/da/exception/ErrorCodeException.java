package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCodeException {
    SUCCESS_CODE(1000, "Success fully!"),
    ERROR_CODE(500, "Error ,please try again!"),
    PROVINCE_NAME_EXISTS(1001, "The province already exists!"),
    DISTRICT_NAME_EXISTS(1002, "The district already exists!"),
    COMMUNE_NAME_EXISTS(1003, "The commune already exists!"),
    PROVINCE_NOT_FOUND(404, "The province not found!"),
    DISTRICT_NOT_FOUND(404, "The district not found!"),

    COMMUNE_NOT_FOUND(404, "The commune not found!"),
    USER_NOT_FOUND(404, "The user not found!"),
    UNAUTHENTICATED(1005, "Unauthenticated!"),
    EMPLOYEE_ID_NOT_NULL(1008, "Employee ID is required!"),
    EMPLOYEE_NOT_FOUND(1012, "The province not found!"),
    PROVINCE_ID_NOT_NULL(1009, "Province ID is required!"),


    CERTIFICATE_ALREADY_EXITS(1010, "The certificate already exists in this province and is still valid!"),
    CERTIFICATE_MAX(1006, "Employees must not have more than 3 valid diplomas of the same type!"),
    CERTIFICATE_NOT_FOUND(404, "The certificate not found!"),
    CERTIFICATE_VALID_FROM_NULL(1022, "The starting value of the certificate cannot be empty!"),
    CERTIFICATE_VALID_UTIL_NULL(1024, "The end value of the certificate cannot be empty!"),
    CERTIFICATE_STATUS_NULL(1026, "Certificate status cannot be null!"),
    CERTIFICATE_STATUS_VALID(1028, "The status of the certificate is 0 and 1!"),
    CERTIFICATE_NOT_NULL(1020, "The certificate not null!");

    private int code;
    private String message;

}
