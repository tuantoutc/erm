package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AttributeGroupService {

    AttributeGroupDto createAttributeGroup(AttributeGroupDto dto);
    AttributeGroupDto updateAttributeGroup(AttributeGroupDto dto);
    AttributeGroupDto getAttributeGroupById(Long id );
    void deleteAttributeGroup(Long id);
    ApiResponse<PageResponseDto<AttributeGroupDto>> search(String code, Boolean isActive, PageRequest pageRequest);
}
