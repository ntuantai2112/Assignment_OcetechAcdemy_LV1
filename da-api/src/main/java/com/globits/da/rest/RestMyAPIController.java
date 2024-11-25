package com.globits.da.rest;


import com.globits.da.dto.MyApiDto;
import com.globits.da.service.MyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestMyAPIController {

    @Autowired
    MyApiService myApiService;

    //    Câu 4: 1. Tạo REST GET api MyFirstApi với source code vừa clone
    // 2. Trả về 1 String MyFirstApi
    @GetMapping("/my-api")
    public String getMyFirstApi() {
        return "Rest My First API";
    }

    //    Câu 5: Call Service MyFirstApiService từ Rest Controller gọi method trả về string MyFirstApiService
    @GetMapping("/my-api-service")
    public String getMyFirstApiService() {
        return myApiService.myFirstApi();
    }

    //  Post APi với 1 đối tượng
    // Câu 6: Sử dụng DTO vừa tạo POST api MyFirstApi  với @RequestBody
    @PostMapping("/my-api")
    public @ResponseBody
    String getMyFirstApiDTO(@RequestBody MyApiDto myApiDto) {
        return myApiService.myFirstApiDTO(myApiDto);
    }


    //    Post API với form-data Postman with @RequestBody
    // Câu 7,8: Thực hiện postman POST api MyFirstApi với formdata
    @PostMapping("/my-api-form-data")
    public ResponseEntity<MyApiDto> getMyApiFormData(
            @RequestBody MyApiDto myApiDto) {
        return myApiService.getMyApiFormData(myApiDto);

    }

    //    Post API với form-data Postman with @RequestParam
    // Câu 9: Thực hiện postman POST api MyFirstApi với formdata
    @PostMapping("/my-api-form-data-param")
    public Map<String, Object> getMyApiFormDataParam(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("file") MultipartFile file) {

        MyApiDto myApiDto = new MyApiDto(name, age, file);
        Map<String, Object> response = myApiService.myApiFormData(myApiDto);
        return response;
    }


    //    Post API với form-data Postman with @PathVariable
    // Câu 10: Thực hiện postman POST api MyFirstApi với pathvariable
    @PostMapping("/my-api-variable/{name}/{age}")
    public ResponseEntity<String> getMyApiPathVariable(
            @PathVariable("name") String name,
            @PathVariable("age") Integer age

    ) {
        return myApiService.getMyApiPathVariable(name,age);
    }


    // Câu 11: Thực hiện postman POST api MyFirstApi với formdata và kiểu json
    // Spring Boot Post không sử dụng anotation

    @PostMapping("/my-first-api")
    public ResponseEntity<String> getMyFirstAPI(HttpServletRequest request) {
        return myApiService.getMyFirstAPI(request);

    }



    // Câu 12 1. Thực hiện postman POST api MyFirstApi với 1 file text/excel
            //2. Debug file gửi lên System Out Print các dòng có trong file text


    @PostMapping("/my-api-file")
    public ResponseEntity<String> getMyApiFile(@RequestParam MultipartFile file){

           return myApiService.processFile(file);
    }



}
