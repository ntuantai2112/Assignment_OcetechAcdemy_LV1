package com.globits.da.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationException {

    private int code;
    private String message;
    private EmployeeCodeException employeeCodeException;

    public ValidationException(EmployeeCodeException employeeCodeException) {
        this.employeeCodeException = employeeCodeException;
    }

    public ValidationException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
