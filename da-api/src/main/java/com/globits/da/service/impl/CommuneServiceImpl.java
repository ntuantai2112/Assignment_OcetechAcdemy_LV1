package com.globits.da.service.impl;

import com.globits.da.domain.Commune;
import com.globits.da.dto.response.ApiResponse;
import com.globits.da.dto.request.CommuneDto;
import com.globits.da.dto.response.CommuneResponse;
import com.globits.da.mapper.CommuneMapper;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.service.CommuneService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommuneServiceImpl implements CommuneService {


    @Autowired
    CommuneRepository repository;

    @Autowired
    CommuneMapper mapper;

    @Autowired
    ApiResponse<CommuneResponse> apiResponse;


    @Override
    public ApiResponse<List<CommuneResponse>> getAllCommune() {
        List<Commune> communes = repository.findAll();
        List<CommuneResponse> communeResponses = new ArrayList<>();
        for (Commune commune : communes) {
            communeResponses.add(mapper.toCommuneResponse(commune));
        }

        ApiResponse<List<CommuneResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setResult(communeResponses);
        return response;
    }

    @Override
    public ApiResponse<List<CommuneResponse>> getCommuneByName(String name) {
        List<Commune> communes = repository.findByNameQuery(name);
        List<CommuneResponse> communeResponses = new ArrayList<>();
        for (Commune commune : communes) {
            communeResponses.add(mapper.toCommuneResponse(commune));
        }

        ApiResponse<List<CommuneResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setResult(communeResponses);
        return response;

    }

    @Override
    public ApiResponse<CommuneResponse> getCommuneById(Integer id) {
        ApiResponse<CommuneResponse> apiResponse = new ApiResponse<>();
        Commune commune =  repository.findById(id).get();
        apiResponse.setCode(200);
        apiResponse.setMessage(null);
        apiResponse.setResult(mapper.toCommuneResponse(commune));

        return apiResponse;
    }

    @Override
    public ApiResponse<CommuneResponse> addCommune(CommuneDto request) {
        Commune commune = mapper.toCommune(request);
        mapper.toCommuneResponse(repository.save(commune));
        apiResponse.setCode(200);
        apiResponse.setResult(mapper.toCommuneResponse(repository.save(commune)));
        apiResponse.setMessage("Add Commune Successfully!");
        return apiResponse;
    }

    @Override
    public ApiResponse<String> deleteCommune(Integer id) {
        ApiResponse<String> response = new ApiResponse<>();
        ApiResponse<CommuneResponse> apiResponse = new ApiResponse<>();

        Commune commune = repository.findById(id).get();
        apiResponse.setCode(200);
        apiResponse.setMessage("Delete Succesfully!");
        apiResponse.setResult(mapper.toCommuneResponse(commune));
        repository.delete(commune);
        return response;
    }

    @Override
    public ApiResponse<CommuneResponse> updateCommune(Integer id, CommuneDto request) {
        ApiResponse<CommuneResponse> apiResponse = new ApiResponse<>();
        Commune commune = repository.findById(id).get();
        mapper.updateCommune(commune,request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Update Successfully!");
        apiResponse.setResult(mapper.toCommuneResponse(repository.save(commune)));
        return apiResponse;
    }


}
