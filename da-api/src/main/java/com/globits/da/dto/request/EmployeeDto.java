package com.globits.da.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EmployeeDto {

    @NotBlank(message = "EMPLOYEE_NOT_NULL")
    private String code;
    private String name;
    private String email;
    private String phone;
    private int age;
    private int provinceId;
    private int districtId;
    private int communeId;
    private String communeName;

}
