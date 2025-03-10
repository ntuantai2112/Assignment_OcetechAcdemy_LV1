package com.globits.da.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "Province ID is required")
    int provinceId;
    @NotNull(message = "District ID is required")
    int districtId;
    @NotNull(message = "Commune ID is required")
    int communeId;
    String communeName;

}
