package com.example.erm_demo.adapter.out.persistence.mapper;

import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CauseCategoryMapper {

    CauseCategoryDto toDto(CauseCategoryEntity entity);


    CauseCategoryEntity toEntity(CauseCategoryDto dto);
}
