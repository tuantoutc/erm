package com.example.erm_demo.application.service;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PreventiveMeasureDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import org.springframework.data.domain.PageRequest;

public interface PreventiveMeasureService {


    PreventiveMeasureDto createPreventiveMeasure(PreventiveMeasureDto dto);
    PreventiveMeasureDto updatePreventiveMeasure(PreventiveMeasureDto dto);
    PreventiveMeasureDto getPreventiveMeasureById(Long id );
    void deletePreventiveMeasure(Long id);
    ApiResponse<PageResponseDto<PreventiveMeasureDto>> search(String code, Boolean isActive, PageRequest pageRequest);
    
}
