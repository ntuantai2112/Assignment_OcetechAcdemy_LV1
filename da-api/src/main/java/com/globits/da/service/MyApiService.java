package com.globits.da.service;

import com.globits.da.dto.request.MyApiDTO;
import com.globits.da.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public interface MyApiService {

    String myFirstApiService();

    MyApiDTO createMyApiDTO(MyApiDTO myApiDto);

    MyApiDTO createMyApiFormData(MyApiDTO myApiDto);

    Map<String, Object> myApiFormData(MyApiDTO myApiDto);

    ResponseEntity<MyApiDTO> getMyApiFormData(MyApiDTO myApiDto);

    MyApiDTO createMyApiPathVariable(String code, String name, Integer age);

    MyApiDTO postMyFirstAPI(HttpServletRequest request) throws IOException;

    ResponseEntity<String> processFile(MultipartFile file);


    ResponseEntity<MyApiDTO> getMyApiNoRequestBody(MyApiDTO myApiDto);
}
