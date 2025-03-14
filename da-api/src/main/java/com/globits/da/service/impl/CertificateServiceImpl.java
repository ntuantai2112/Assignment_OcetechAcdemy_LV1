package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.request.CertificateDto;
import com.globits.da.dto.request.DistrictDto;
import com.globits.da.dto.response.CertificateResponse;
import com.globits.da.dto.response.DistrictResponse;
import com.globits.da.exception.AppException;
import com.globits.da.exception.EmployeeAppException;
import com.globits.da.exception.EmployeeCodeException;
import com.globits.da.exception.ErrorCodeException;
import com.globits.da.mapper.CertificateMapper;
import com.globits.da.mapper.DistrictMapper;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class CertificateServiceImpl implements CertificateService {


    @Autowired
    private CertificateRepository repository;

    @Autowired
    private CertificateMapper mapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

//    @Override
//    public DistrictResponse updateDistrict(Integer id, DistrictDto request) {
//        District district = repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!"));
//        mapper.updateDistrict(district, request);
//        return mapper.toDistrictReponse(repository.save(district));
//    }

    @Override
    public List<CertificateResponse> getAllCertificate() {

        List<CertificateResponse> certificateResponses = repository.findAllWithEmployeeAndProvince()
                .stream().map(mapper::toCertificateResponse).collect(Collectors.toList());
        return certificateResponses;
    }

    @Override
    public List<CertificateResponse> getCertificateByName(String name) {
        List<Certificate> certificates = repository.findByNameQuery(name);
        List<CertificateResponse> certificateResponses = new ArrayList<>();
        for (Certificate certificate : certificates) {
            certificateResponses.add(mapper.toCertificateResponse(certificate));
        }
        return certificateResponses;
    }

    @Override
    public CertificateResponse getCertificateResponseById(Integer id) {
        Certificate certificate = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.CERTIFICATE_NOT_FOUND));
        return mapper.toCertificateResponse(certificate);
    }

    @Override
    public CertificateResponse addCertificate(CertificateDto request) {
        if (request.getValidFrom() == null || request.getValidUntil() == null) {
            throw new IllegalArgumentException("ValidFrom and ValidUntil cannot be null");
        }

        LocalDate currentDate = LocalDate.now();
        // Kiểm tra trùng văn bằng trong tỉnh
        if (!repository.findValidCertificatesByProvince(request.getEmployeeId(), request.getName(), request.getProvinceId(), currentDate).isEmpty()) {
            throw new AppException(ErrorCodeException.CERTIFICATE_ALREADY_EXITS);
        }

        // Kiểm tra số lượng văn bằng cùng loại
        if (repository.findValidCertificatesByName(request.getEmployeeId(), request.getName(), currentDate).size() >= 3) {
            throw new AppException(ErrorCodeException.CERTIFICATE_MAX);
        }



        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new EmployeeAppException(EmployeeCodeException.EMPLOYEE_NOT_FOUND));
        Province province = provinceRepository.findById(request.getProvinceId()).orElseThrow(() -> new AppException(ErrorCodeException.PROVINCE_NOT_FOUND));

        Certificate certificate = mapper.toCertificate(request);
        certificate.setCreatedAt(LocalDateTime.now());
        certificate.setUpdatedAt(LocalDateTime.now());
        certificate.setEmployee(employee);
        certificate.setProvince(province);
        return mapper.toCertificateResponse(repository.save(certificate));
    }

    @Override
    public String deleteCertificate(Integer id) {
        Certificate certificate = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.CERTIFICATE_NOT_FOUND));
        repository.delete(certificate);
        return "Delete Successfully!";
    }

    @Override
    public CertificateResponse updateCertificate(Integer id, CertificateDto request) {
        Certificate certificate = repository.findById(id).orElseThrow(() -> new AppException(ErrorCodeException.CERTIFICATE_NOT_FOUND));
        mapper.updateCertificate(certificate, request);
        return mapper.toCertificateResponse(repository.save(certificate));
    }
}
