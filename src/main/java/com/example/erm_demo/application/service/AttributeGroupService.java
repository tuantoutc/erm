package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AttributeGroupService {

    AttributeGroupDto createAttributeGroup(AttributeGroupDto dto);
    AttributeGroupDto updateAttributeGroup(AttributeGroupDto dto);
    AttributeGroupDto getAttributeGroupById(Long id );
    void deleteAttributeGroup(Long id);
    Page<AttributeGroupDto> searchByKeyWord( String code, Boolean isActive, PageRequest pageRequest);
    Page<AttributeGroupDto> getAllAttributeGroups(PageRequest pageRequest);
}
