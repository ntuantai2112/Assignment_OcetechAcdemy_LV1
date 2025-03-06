package com.globits.da.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MyApiDTO {

    String code;
    String name;
    int age;
//    private MultipartFile file;

//    public MyApiDTO(String code, String name, int age) {
//        this.code = code;
//        this.name = name;
//        this.age = age;
//    }

//    public MyApiDTO(String name, int age, MultipartFile file) {
//        this.name = name;
//        this.age = age;
//        this.file = file;
//    }

}
