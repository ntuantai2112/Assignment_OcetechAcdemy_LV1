package com.globits.da.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyApiDto {

    private String code;
    private String name;
    private int age;
    private MultipartFile file;

    public MyApiDto(String name, int age, MultipartFile file) {
        this.name = name;
        this.age = age;
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
