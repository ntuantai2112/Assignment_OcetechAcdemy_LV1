package com.globits.da.rest;

import com.globits.da.dto.request.CertificateDto;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.response.CertificateResponse;
import com.globits.da.dto.response.CommuneResponse;
import com.globits.da.exception.AppException;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.service.CertificateService;
import com.globits.da.service.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificate")
public class RestCertificateController {

    @Autowired
    private CertificateService service;

    @Autowired
    private ApiResponse<CertificateResponse> response ;

    @GetMapping("/get-all")
    public ApiResponse<List<CertificateResponse>> getAll() {
        ApiResponse<List<CertificateResponse>> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage(ErrorCodeException.SUCCESS_CODE.getMessage());
        response.setResult(service.getAllCertificate());
        return response;
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> searchDistricts(@RequestParam String name) {
        ApiResponse<List<CertificateResponse>> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage(ErrorCodeException.SUCCESS_CODE.getMessage());
        response.setResult(service.getCertificateByName(name));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-id/{id}")
    public ResponseEntity<ApiResponse<CertificateResponse>> searchProvinces(@PathVariable("id") Integer id) {
        ApiResponse<CertificateResponse> response = new ApiResponse<>();
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage(ErrorCodeException.SUCCESS_CODE.getMessage());
        response.setResult(service.getCertificateResponseById(id));

        // Nếu tìm thấy, trả về dữ liệu tỉnh dưới dạng JSON
        if (response == null) {
            throw new AppException(ErrorCodeException.CERTIFICATE_NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/add")
    public ApiResponse<CertificateResponse> create(@RequestBody CertificateDto request) {
        ApiResponse<CertificateResponse> response = new ApiResponse<>();
        response.setResult(service.addCertificate(request));
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Add Certificate Successfully!");
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> delete(@PathVariable("id") Integer id) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult(service.deleteCertificate(id));
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        return response;

    }

    @PutMapping("/update/{id}")
    public ApiResponse<CertificateResponse> update(@PathVariable("id") Integer id, @RequestBody CertificateDto request) {
        ApiResponse<CertificateResponse> response = new ApiResponse<>();
        response.setResult( service.updateCertificate(id, request));
        response.setCode(ErrorCodeException.SUCCESS_CODE.getCode());
        response.setMessage("Update Certificate Successfully!");
        return response;
    }


}
