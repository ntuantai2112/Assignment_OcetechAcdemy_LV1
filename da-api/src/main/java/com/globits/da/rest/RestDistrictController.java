package com.globits.da.rest;

import com.globits.da.domain.District;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.dto.response.DistrictResponse;
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






    @PostMapping("/add")
    public DistrictResponse create(@RequestBody DistrictDto request){
      return  service.addDistrict(request);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id){

       return service.deleteDistrict(id);

    }

    @PutMapping("/update/{id}")
    public DistrictResponse update(@PathVariable("id") Integer id ,@RequestBody DistrictDto request){
        return  service.updateDistrict(id,request);
    }









}
