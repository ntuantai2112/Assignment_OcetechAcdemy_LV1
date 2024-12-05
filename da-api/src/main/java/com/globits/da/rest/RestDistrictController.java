package com.globits.da.rest;

import com.globits.da.domain.District;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.ProvinceResponse;
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
    public List<DistrictResponse> getAll(){
        return  service.getAllDistrict();
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<DistrictResponse>> searchDistricts(@RequestParam String name){
        return ResponseEntity.ok(service.getDistrictByName(name));
    }

    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<DistrictResponse> searchProvinces(@PathVariable("id") Integer id){
        DistrictResponse response =  service.getDistrictById(id);

        // Nếu tìm thấy, trả về dữ liệu tỉnh dưới dạng JSON
        if(response == null){
            throw  new RuntimeException("Not Found!");
        }
        return ResponseEntity.ok(response);
    }




    @PostMapping("/add/{provinceId}")
    public ApiResponse<DistrictResponse> create(@PathVariable("provinceId") Integer provinceId,@RequestBody DistrictDto request){
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Add district successfully!");
        response.setResult(service.addDistrict(provinceId,request));
        return response;
    }


    @PostMapping("/add")
    public ApiResponse<DistrictResponse> createDistrict(@RequestBody DistrictDto request){
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Add district successfully!");
        response.setResult(service.addDistrict(request));
        return response;
    }

    // Khi thực hiện xóa huyện , thì có xã có id Huyện cũng bị xóa theo
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){

       return service.deleteDistrict(id);

    }

    @PutMapping("/update/{id}")
    public DistrictResponse update(@PathVariable("id") Integer id ,@RequestBody DistrictDto request){
        return  service.updateDistrict(id,request);
    }


    @GetMapping("/find-districts/{provinceId}")
    public ApiResponse<List<DistrictResponse>> getDistrictByProvinceId(@PathVariable("provinceId") Integer provinceId){
        ApiResponse<List<DistrictResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success fully!");
        response.setResult(service.findDistrictsByProvinceId(provinceId));
        return  response;
    }

    // Thực hiện thêm huyện và thêm xã cùng 1 lúc
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

    @PostMapping("/create-district-commune")
    public ApiResponse<DistrictResponse> createDistrictAndCommune(@RequestBody DistrictDto districtRequest){
        ApiResponse<DistrictResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Add Sucessfully!");
        response.setResult(service.createDistrictAndCommune(districtRequest));
        return  response;


    }







}
