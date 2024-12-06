package com.globits.da.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDto {

    String code;
    String name;
    String email;
    String phone;
    int age;


}
