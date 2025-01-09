package com.globits.da.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private int id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private Integer age;
//    private ProvinceResponse province;
    private String provinceName;
    private String districtName;
    private String communeName;


}
