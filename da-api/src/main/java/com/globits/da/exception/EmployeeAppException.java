package com.globits.da.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeAppException extends RuntimeException{


    private EmployeeCodeException employeeCode;

    public EmployeeAppException(EmployeeCodeException employeeCode) {
        super(employeeCode.getMessage());
        this.employeeCode = employeeCode;
    }
}
