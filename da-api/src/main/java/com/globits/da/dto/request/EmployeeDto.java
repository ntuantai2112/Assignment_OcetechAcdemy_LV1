package com.globits.da.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {

    int rowIndex;
    String code;
    String name;
    @Email
    String email;
    String phone;
    int age;
    int provinceId;
    int districtId;
    int communeId;
    String provinceName;
    String districtName;
    String communeName;

}
