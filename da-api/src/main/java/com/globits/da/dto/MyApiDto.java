package com.globits.da.dto;

import org.springframework.web.multipart.MultipartFile;

public class MyApiDto {

    private String code;
    private String name;
    private int age;
    private MultipartFile file;


    public MyApiDto() {
    }

    public MyApiDto(String code, String name, Integer age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public MyApiDto(String name, int age, MultipartFile file) {
        this.name = name;
        this.age = age;
        this.file = file;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return " MyApiDto {" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age + '}';
    }
}
