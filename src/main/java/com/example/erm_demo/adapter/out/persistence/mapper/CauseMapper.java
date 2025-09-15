package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CauseMapper {
    @Mapping(target = "causeCategoryId", source = "causeCategory.id")
    CauseEntity toEntity(CauseDto dto);

    CauseDto toDto(CauseEntity entity);
}
