package com.globits.da.rest;

import com.globits.da.domain.District;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.exception.CodeConfig;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/district")
public class RestDistrictController {

    @Autowired
    private DistrictService service;


    @GetMapping("/get-all")
    public ApiResponse<List<DistrictResponse>> getAll() {
        List<DistrictResponse> list = service.getAllDistrict();
        return apiResponse(list);
    }

    @GetMapping("/search-by-name")
    public ApiResponse<List<DistrictResponse>> searchDistricts(@RequestParam String name) {
        return apiResponse(service.getDistrictByName(name));
    }

    @GetMapping("/search-by-id/{id}")
    public ApiResponse<DistrictResponse> searchProvinces(@PathVariable("id") Integer id) {
        DistrictResponse response = service.getDistrictById(id);

        // Nếu tìm thấy, trả về dữ liệu tỉnh dưới dạng JSON
        if (response == null) {
            throw new RuntimeException("Not Found!");
        }
        return apiResponse(response);
    }

    // Thêm huyện xác định Huyện đó thuộc tỉnh nào
    @PostMapping("/add/{provinceId}")
    public ApiResponse<DistrictResponse> create(@PathVariable("provinceId") Integer provinceId, @RequestBody DistrictDto request) {
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Add district successfully!");
        response.setResult(service.addDistrict(provinceId, request));
        return response;
    }


    @PostMapping("/add")
    public ApiResponse<DistrictResponse> createDistrict(@RequestBody DistrictDto request) {
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Add district successfully!");
        response.setResult(service.addDistrict(request));
        return response;
    }

    // Khi thực hiện xóa huyện , thì có xã có id Huyện cũng bị xóa theo
    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Integer id) {
        return apiResponse(service.deleteDistrict(id));
    }

    @PutMapping("/update/{id}")
    public ApiResponse<DistrictResponse> update(@PathVariable("id") Integer id, @RequestBody DistrictDto request) {

        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Update Success fully!");
        response.setResult(service.updateDistrict(id, request));
        return response;
    }


    @GetMapping("/find-districts/{provinceId}")
    public ApiResponse<List<DistrictResponse>> getDistrictByProvinceId(@PathVariable("provinceId") Integer provinceId) {
        ApiResponse<List<DistrictResponse>> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Success fully!");
        response.setResult(service.findDistrictsByProvinceId(provinceId));
        return response;
    }

    // Thực hiện thêm huyện và thêm xã cùng 1 lúc theo ID Tỉnh
//    @PostMapping("/create-district-commune/{provinceId}")
//    public ApiResponse<DistrictResponse> createDistrictAndCommune(@PathVariable("provinceId") Integer provinceId,@RequestBody DistrictDto districtRequest){
//        ApiResponse<DistrictResponse> response = new ApiResponse<>();
//        response.setCode(200);
//        response.setMessage("Add Sucessfully!");
//        response.setResult(service.createDistrictAndCommune(provinceId,districtRequest));
//        return  response;
//
//
//    }


    // Thực hiện thêm huyện và thêm xã cùng 1 lúc theo Tên tỉnh truyền vào từ District Request
    @PostMapping("/create-district-commune")
    public ApiResponse<DistrictResponse> createDistrictAndCommune(@RequestBody DistrictDto districtRequest) {
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Add Sucessfully!");
        response.setResult(service.createDistrictAndCommune(districtRequest));
        return response;

    }


    @PutMapping("/update-district-crud-commune/{districtId}")
    public ApiResponse<DistrictResponse> createDistrictAndCRUDCommune(
            @PathVariable("districtId") Integer districtId,
            @RequestBody DistrictDto districtRequest) {
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Add Sucessfully!");
        response.setResult(service.createDistrictAndCRUDCommune(districtId, districtRequest));
        return response;

    }

    private <T> ApiResponse<T> apiResponse(T result) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        CodeConfig codeConfig = CodeConfig.SUCCESS_CODE;
        apiResponse.setCode(codeConfig.getCode());
        apiResponse.setMessage(codeConfig.getMessage());
        apiResponse.setResult(result);
        return apiResponse;
    }


}
