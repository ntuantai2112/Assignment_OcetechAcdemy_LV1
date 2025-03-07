package com.globits.da.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {

    int rowIndex;
    @NotBlank(message = "EMPLOYEE_NOT_NULL")
    String code;
    String name;
    String email;
    String phone;
    int age;
    int provinceId;
    int districtId;
    int communeId;
    String communeName;

}
