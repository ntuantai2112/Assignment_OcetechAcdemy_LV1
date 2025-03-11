package com.globits.da.service;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.District;
import com.globits.da.dto.request.CertificateDto;
import com.globits.da.dto.response.CertificateResponse;

import java.util.List;

public interface CertificateService {

    List<CertificateResponse> getAllCertificate();


    List<CertificateResponse> getCertificateByName(String name);

    CertificateResponse getCertificateResponseById(Integer id);

    CertificateResponse addCertificate(CertificateDto request);

    List<CertificateResponse> addListCertificate(List<CertificateDto> certificateRequests);

    String deleteCertificate(Integer id);

    CertificateResponse updateCertificate(Integer id,CertificateDto request);







}
