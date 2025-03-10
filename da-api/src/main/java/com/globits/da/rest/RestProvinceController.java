package com.globits.da.rest;

import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.exception.CodeConfig;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.exception.ProvinceCodeException;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestProvinceController {

    @Autowired
    private ProvinceService provinceService;


    @GetMapping("/get-all-province")
    public ApiResponse<List<ProvinceResponse>> getAll() {
        return apiResponse( provinceService.getAllProvince());
    }

    @GetMapping("/search-province")
    public ApiResponse<List<ProvinceResponse>> searchProvinces(@RequestParam String name) {
        List<ProvinceResponse> list = provinceService.getProvinceByName(name);
        return apiResponse(list);
    }

    @GetMapping("/search-province-by-id/{id}")
    public ApiResponse<ProvinceResponse> searchProvinces(@PathVariable("id") Integer id) {
        ProvinceResponse response = provinceService.getProvinceById(id);
        return apiResponse(response);
    }


    @PostMapping("/add-province")
    public ApiResponse<ProvinceResponse> createProvince(@RequestBody ProvinceDto request) {
        return apiResponse(provinceService.addProvince(request));
    }

    @DeleteMapping("/delete-province/{id}")
    public ApiResponse<String> deleteProvince(@PathVariable("id") Integer id) {

        return apiResponse(provinceService.deleteProvince(id));

    }

    @PutMapping("/update-province/{provinceId}")
    public ApiResponse<ProvinceResponse> updateProvince(@PathVariable("provinceId") Integer id, @RequestBody ProvinceDto request) {
        return apiResponse(provinceService.updateProvince(id, request));
    }

    // Thực hiện thêm provice và district cùng 1 lúc
    @PostMapping("/add-province-districts")
    public ResponseEntity<ApiResponse<ProvinceResponse>> createProvinceAndDistricts(@RequestBody ProvinceDto request) {
        ApiResponse<ProvinceResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("add successfully!");
        response.setResult(provinceService.createProvinceAndDistricts(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-province-districts-communes")
    public ResponseEntity<ApiResponse<ProvinceResponse>> createProvinceAndDistrictAndCommune(@RequestBody ProvinceDto request) {
        ApiResponse<ProvinceResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("add successfully!");
        response.setResult(provinceService.createProvinceAndDistrictAndCommune(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-province-districts/{provinceId}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> updateProvinceAndDistricts(
            @PathVariable("provinceId") Integer provinceId
            , @RequestBody ProvinceDto request) {
        ApiResponse<ProvinceResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("update successfully!");
        response.setResult(provinceService.updateProvince(provinceId, request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-province-crud-districts/{provinceId}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> updateProvinceAndCRUDDistrict(
            @PathVariable("provinceId") Integer provinceId
            , @RequestBody ProvinceDto request) {
        ApiResponse<ProvinceResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("update successfully!");
        response.setResult(provinceService.updateProvinceAndCRUDDistrict(provinceId, request));
        return ResponseEntity.ok(response);
    }


    private <T> ApiResponse<T> apiResponse(T result) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        CodeConfig code = CodeConfig.SUCCESS_CODE;
        apiResponse.setCode(code.getCode());
        apiResponse.setMessage(code.getMessage());
        apiResponse.setResult(result);
        return apiResponse;
    }


}
