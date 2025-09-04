package com.example.erm_demo.application.service;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import org.springframework.data.domain.PageRequest;

public interface AttributeService {


    AttributeDto createAttribute(AttributeDto dto);
    AttributeDto updateAttribute(AttributeDto dto);
    AttributeDto getAttributeById(Long id );
    void deleteAttribute(Long id);
    ApiResponse<PageResponseDto<AttributeDto>> search(String code, String dataType, Long attributeGroupId, Boolean isActive, PageRequest pageRequest);
    
}
