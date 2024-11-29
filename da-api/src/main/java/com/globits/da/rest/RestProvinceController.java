package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.request.ProvinceDto;
import com.globits.da.dto.response.ProvinceResponse;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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









}
