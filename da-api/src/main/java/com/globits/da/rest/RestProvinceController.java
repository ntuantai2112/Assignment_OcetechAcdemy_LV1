package com.globits.da.rest;

import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.exception.ErrorCodeException;
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
    public List<ProvinceResponse> getAll(){
        return  provinceService.getAllProvince();
    }

    @GetMapping("/search-province")
    public ResponseEntity<List<ProvinceResponse>> searchProvinces(@RequestParam String name){
        return ResponseEntity.ok(provinceService.getProvinceByName(name));
    }

    @GetMapping("/search-province-by-id/{id}")
    public ResponseEntity<ProvinceResponse> searchProvinces(@PathVariable("id") Integer id){
        ProvinceResponse response =  provinceService.getProvinceById(id);

        // Nếu tìm thấy, trả về dữ liệu tỉnh dưới dạng JSON

        return ResponseEntity.ok(response);
    }



    @PostMapping("/add-province")
    public ProvinceResponse createProvince(@RequestBody ProvinceDto request){
      return  provinceService.addProvince(request);
    }

    @DeleteMapping("/delete-province/{id}")
    public String deleteProvince(@PathVariable("id") Integer id){

       return provinceService.deleteProvince(id);

    }

    @PutMapping("/update-province/{provinceId}")
    public ProvinceResponse updateProvince(@PathVariable("provinceId") Integer id ,@RequestBody ProvinceDto request){
        return  provinceService.updateProvince(id,request);
    }

    // Thực hiện thêm provice và district cùng 1 lúc
    @PostMapping("/add-province-districts")
    public ResponseEntity<ApiResponse<ProvinceResponse>> createProvinceAndDistricts(@RequestBody ProvinceDto request){
        ApiResponse<ProvinceResponse>response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("add successfully!");
        response.setResult(provinceService.createProvinceAndDistricts(request));
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/add-province-districts-communes")
    public ResponseEntity<ApiResponse<ProvinceResponse>> createProvinceAndDistrictAndCommune(@RequestBody ProvinceDto request){
        ApiResponse<ProvinceResponse>response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("add successfully!");
        response.setResult(provinceService.createProvinceAndDistrictAndCommune(request));
        return  ResponseEntity.ok(response);
    }

    @PutMapping("/update-province-districts/{provinceId}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> updateProvinceAndDistricts(
            @PathVariable("provinceId") Integer provinceId
            ,@RequestBody ProvinceDto request){
        ApiResponse<ProvinceResponse>response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("update successfully!");
        response.setResult(provinceService.updateProvince(provinceId,request));
        return  ResponseEntity.ok(response);
    }

    @PutMapping("/update-province-crud-districts/{provinceId}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> updateProvinceAndCRUDDistrict(
            @PathVariable("provinceId") Integer provinceId
            ,@RequestBody ProvinceDto request){
        ApiResponse<ProvinceResponse>response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("update successfully!");
        response.setResult(provinceService.updateProvinceAndCRUDDistrict(provinceId,request));
        return  ResponseEntity.ok(response);
    }









}
