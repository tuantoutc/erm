package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskTypeDto;
import org.springframework.data.domain.PageRequest;

public interface RiskTypeService {

    RiskTypeDto createRiskType(RiskTypeDto riskTypeDto);
    RiskTypeDto updateRiskType(RiskTypeDto riskTypeDto);
    RiskTypeDto getRiskTypeById(Long id );
    void deleteRiskType(Long id);
    ApiResponse<PageResponseDto<RiskTypeDto>> search( String code, Long systemId, Boolean isActive, PageRequest pageRequest);

}
