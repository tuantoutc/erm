package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import com.example.erm_demo.domain.enums.SourceType;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class AttributeGroupMapper {

    private final ModelMapper modelMapper;

    public  AttributeGroupDto maptoAttributeGroupDto(AttributeGroupEntity entity){
        return modelMapper.map(entity, AttributeGroupDto.class);
    }


}
