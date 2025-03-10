package com.globits.da.rest;

import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CommuneResponse;
import com.globits.da.dto.response.CommuneResponse;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.service.CommuneService;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commune")
public class RestCommuneController {

    @Autowired
    private CommuneService service;


    @GetMapping("/get-all")
    public ApiResponse<List<CommuneResponse>> getAll(){
        return  service.getAllCommune();
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<ApiResponse<List<CommuneResponse>>> searchDistricts(@RequestParam String name){
        return ResponseEntity.ok(service.getCommuneByName(name));
    }

    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<ApiResponse<CommuneResponse>> searchProvinces(@PathVariable("id") Integer id){
        ApiResponse<CommuneResponse> response =  service.getCommuneById(id);

        // Nếu tìm thấy, trả về dữ liệu tỉnh dưới dạng JSON
        if(response == null){
            throw  new RuntimeException("Not Found!");
        }
        return ResponseEntity.ok(response);
    }

    // Thêm xã và xác định xã đó thuộc huyện nào
    @PostMapping("/add")
    public ApiResponse<CommuneResponse> create(@RequestBody CommuneDto request){
      return  service.addCommune(request);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Integer id){

       return service.deleteCommune(id);

    }

    @PutMapping("/update/{id}")
    public ApiResponse<CommuneResponse> update(@PathVariable("id") Integer id ,@RequestBody CommuneDto request){
        return  service.updateCommune(id,request);
    }

    // Tìm kiếm danh sách xã theo ID huyện
    @GetMapping("/find-communes/{districtId}")
    public ApiResponse<List<CommuneResponse>> findCommunesByDistrictId(@PathVariable("districtId") Integer districtId){
         ApiResponse<List<CommuneResponse>> response = new ApiResponse<>();
         response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
         response.setMessage("Find sucessfully!");
         response.setResult(service.findCommunesByDistrictId(districtId));
         return response;

    }









}
