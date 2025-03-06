package com.globits.da.rest;


import com.globits.da.dto.request.MyApiDTO;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.exception.CodeConfig;
import com.globits.da.service.MyApiService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestMyAPIController {

    @Autowired
    MyApiService myApiService;

    //    Câu 4: 1. Tạo REST GET api MyFirstApi với source code vừa clone
    //           2. Trả về 1 String MyFirstApi
    @GetMapping("/my-api")
    public ApiResponse<String> getMyFirstApi() {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Successfully");
        apiResponse.setResult("Rest My First API");
        return apiResponse;
    }

    //    Câu 5: Call Service MyFirstApiService từ Rest Controller gọi method trả về string MyFirstApiService
    @GetMapping("/my-api-service")
    public ApiResponse<String> getMyFirstApiService() {
        return apiResponse(myApiService.myFirstApiService());
    }

    //  Post APi với 1 đối tượng
    // Câu 6: Sử dụng DTO vừa tạo POST api MyFirstApi  với @RequestBody
    // Câu 8: Post body với giá trị rỗng (null) với thuộc tính age
    @PostMapping("/my-api")
    public ApiResponse<MyApiDTO> createMyFirstApi(@RequestBody MyApiDTO myApiDTO) {
        return apiResponse(myApiService.createMyApiDTO(myApiDTO));
    }


    //    Post API với form-data Postman with @RequestBody
    // Câu 7: Thực hiện postman POST api MyFirstApi với formdata
    @PostMapping("/my-api-form-data")
    public ApiResponse<MyApiDTO> createMyApiFormData(
            @RequestBody MyApiDTO myApiDto) {
        return apiResponse(myApiService.createMyApiFormData(myApiDto));
    }


    //    Post API với form-data Postman with @RequestParam
    // Câu 9: Thực hiện postman POST api MyFirstApi với formdata
    @PostMapping("/my-api-form-data-request-param")
    public ApiResponse<MyApiDTO> createMyApiFormData(
            @RequestParam("code") String code,
            @RequestParam("name") String name,
            @RequestParam("age") int age) {

        MyApiDTO myApiDto = new MyApiDTO(code, name, age);
        return apiResponse(myApiService.createMyApiFormData(myApiDto));
    }

    @PostMapping("/my-api-form-data-param")
    public Map<String, Object> getMyApiFormDataParam(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("file") MultipartFile file) {

        MyApiDTO myApiDto = new MyApiDTO();
        Map<String, Object> response = myApiService.myApiFormData(myApiDto);
        return response;
    }


    //    Post API với form-data Postman with @PathVariable
    // Câu 10: Thực hiện postman POST api MyFirstApi với pathvariable
    @PostMapping("/my-api-path-variable/{code}/{name}/{age}")
    public ApiResponse<MyApiDTO> createMyApiPathVariable(
            @PathVariable("code") String code,
            @PathVariable("name") String name,
            @PathVariable("age") Integer age

    ) {
        MyApiDTO myApiDto = new MyApiDTO(code, name, age);
        return apiResponse(myApiService.createMyApiFormData(myApiDto));
    }


    // Câu 11: Thực hiện postman POST api MyFirstApi với formdata và kiểu json
    // Spring Boot Post không sử dụng anotation
    @PostMapping("/my-first-api")
    public ApiResponse<MyApiDTO> postMyFirstAPI(HttpServletRequest request) throws IOException {
        return apiResponse(myApiService.postMyFirstAPI(request));

    }

    // Sử dụng với form-data không sử dụng Annotation
    @PostMapping("/my-api-not-anotation")
    public ApiResponse<MyApiDTO> createMyFirstApiNotAnnotation(MyApiDTO myApiDTO) {
        return apiResponse(myApiDTO);
    }



    // Câu 12 1. Thực hiện postman POST api MyFirstApi với 1 file text/excel
    //2. Debug file gửi lên System Out Print các dòng có trong file text


    @PostMapping("/my-api-file")
    public ResponseEntity<String> getMyApiFile(@RequestParam MultipartFile file) {
        return myApiService.processFile(file);
    }

    // Câu 13:
    //1. Thực hiện postman post raw json với API khi xóa @RequestBody
    //2. Ghi lại kết quả và tìm hiểu lý do

    @PostMapping("/my-api-no-request-body")
    public ResponseEntity<MyApiDTO> getMyApiNoRequestBody(MyApiDTO myApiDto) {
        return myApiService.getMyApiNoRequestBody(myApiDto);

    }

    private ApiResponse apiResponse(Object result) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        CodeConfig codeConfig = CodeConfig.SUCCESS_CODE;
        apiResponse.setCode(codeConfig.getCode());
        apiResponse.setMessage(codeConfig.getMessage());
        apiResponse.setResult(result);
        return apiResponse;
    }


}
