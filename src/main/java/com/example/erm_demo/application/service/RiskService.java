package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface RiskService {


//    RiskDto createRisk(RiskDto riskDto, MultipartFile[] files) ;
    RiskDto createRisk(RiskDto riskDto) ;


    RiskDto updateRisk(RiskDto riskDto, MultipartFile[] files) ;
    RiskDto getRiskById(Long id );
    void deleteRisk(Long id);
    ApiResponse<PageResponseDto<RiskDto>> search( String code, Long systemId, Boolean isActive, PageRequest pageRequest);

}
