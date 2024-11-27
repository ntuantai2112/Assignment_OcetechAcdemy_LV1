package com.globits.da.dto;

import javax.persistence.Column;

public class EmployeeDto {

    private String code;
    private String name;
    private String email;
    private String phone;
    private int age;

    public EmployeeDto() {
    }

    public EmployeeDto(String code, String name, String email, String phone, int age) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
