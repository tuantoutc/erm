package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CauseCategoryService {

    CauseCategoryDto createCauseCategory(CauseCategoryDto causeCategoryDto);

    CauseCategoryDto updateCauseCategory(CauseCategoryDto causeCategoryDto);

    CauseCategoryDto getCauseCategoryById(Long id);

    void deleteCauseCategory(Long id);

    ApiResponse<PageResponseDto<CauseCategoryDto>> searchCauseCategories(String code, Long systemId, PageRequest pageRequest);

}
