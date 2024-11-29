package com.globits.da.service;

import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.response.CommuneResponse;

import java.util.List;

public interface CommuneService {

     ApiResponse<List<CommuneResponse>> getAllCommune();


    ApiResponse<List<CommuneResponse>> getCommuneByName(String name);

    ApiResponse<CommuneResponse> getCommuneById(Integer id);

    ApiResponse<CommuneResponse> addCommune(CommuneDto request);

    ApiResponse<String> deleteCommune(Integer id);

    ApiResponse<CommuneResponse> updateCommune(Integer id,CommuneDto request);








}
