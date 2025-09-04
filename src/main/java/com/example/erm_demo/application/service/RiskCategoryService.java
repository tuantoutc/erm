package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiskCategoryService {

    RiskCategoryDto createRiskCategory(RiskCategoryDto riskCategoryDto);
    RiskCategoryDto updateRiskCategory(RiskCategoryDto riskCategoryDto);
    RiskCategoryDto getRiskCategoryById(Long id );
    void deleteRiskCategory(Long id);
    ApiResponse<PageResponseDto<RiskCategoryDto>> searchRiskCategories( String code, Long systemId, Boolean isActive, PageRequest pageRequest);

}
