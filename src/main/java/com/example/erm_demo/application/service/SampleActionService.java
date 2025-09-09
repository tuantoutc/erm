package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.SampleActionDto;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface SampleActionService {

    SampleActionDto createSampleAction(SampleActionDto SampleActionDto);
    SampleActionDto updateSampleAction(SampleActionDto SampleActionDto);
    SampleActionDto getSampleActionById(Long id );
    void deleteSampleAction(Long id);
    ApiResponse<PageResponseDto<SampleActionDto>> search( String code, Long riskTypeId, Long causeCategoryId, Boolean isActive, PageRequest pageRequest);

}
