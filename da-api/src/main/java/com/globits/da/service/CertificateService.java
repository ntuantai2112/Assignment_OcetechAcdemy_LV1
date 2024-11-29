package com.globits.da.service;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.District;
import com.globits.da.dto.request.CertificateDto;
import com.globits.da.dto.response.CertificateResponse;

import java.util.List;

public interface CertificateService {

    List<Certificate> getAllCertificate();


    List<CertificateResponse> getCertificateByName(String name);

    CertificateResponse addCertificate(CertificateDto request);

    String deleteCertificate(Integer id);

    CertificateResponse updateCertificate(Integer id,CertificateDto request);








}
