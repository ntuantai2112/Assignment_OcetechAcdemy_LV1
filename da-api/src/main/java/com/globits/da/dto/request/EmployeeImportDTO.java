package com.globits.da.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeImportDTO {
    private Integer rowIndex; // Dòng số mấy trong file Excel

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mã nhân viên không được để trống")
    private String code;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @Positive(message = "Tuổi phải là số dương")
    private Integer age;

    @NotBlank(message = "Tỉnh không được để trống")
    private String province;

    @NotBlank(message = "Huyện không được để trống")
    private String district;

    @NotBlank(message = "Xã không được để trống")
    private String commune;
}
