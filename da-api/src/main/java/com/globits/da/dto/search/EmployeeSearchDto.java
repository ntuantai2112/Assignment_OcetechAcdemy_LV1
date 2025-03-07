package com.globits.da.dto.search;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchDto {

    //    @NotBlank(message = "Please enter your employee code")
    private String code;
    //    @NotBlank(message = "Please enter employee name")
    private String name;
    //    @NotNull(message = "Please enter your email")
    private String email;
    //    @NotNull(message = "Please enter your phone")
    private String phone;
    @Min(value = 18, message = "Minimum age is 18")
    private Integer minAge;

    @Max(value = 60, message = "Maximum age is 60")
    private Integer maxAge;


}
