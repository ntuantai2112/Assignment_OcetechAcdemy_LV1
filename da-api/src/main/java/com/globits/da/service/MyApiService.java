package com.globits.da.service;

import com.globits.da.dto.MyApiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface MyApiService {

    String myFirstApi();

    String myFirstApiDTO( MyApiDto myApiDto);

    Map<String,Object> myApiFormData(MyApiDto myApiDto);

    ResponseEntity<MyApiDto> getMyApiFormData(MyApiDto myApiDto);

    ResponseEntity<String> getMyApiPathVariable(String name, Integer age);

    ResponseEntity<String> getMyFirstAPI(HttpServletRequest request);

    ResponseEntity<String> processFile(MultipartFile file);
}
