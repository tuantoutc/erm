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

    public AttributeGroupEntity maptoAttributeGroup(AttributeGroupDto dto){
        AttributeGroupEntity entity = modelMapper.map(dto, AttributeGroupEntity.class);
        entity.setId(null);

        // Kiểm tra nếu dto có SourceType = SYSTEM thì set SYSTEM, ngược lại set BUSINESS
        if(dto.getSourceType() != null && dto.getSourceType().equals(SourceType.SYSTEM)){
            entity.setSourceType(SourceType.SYSTEM);
        } else {
            entity.setSourceType(SourceType.BUSINESS);
        }
        return entity;
    }

    public AttributeGroupEntity updateAttributeGroup(AttributeGroupEntity entity, AttributeGroupDto dto){
        entity = modelMapper.map(dto, AttributeGroupEntity.class);

        // Kiểm tra nếu dto có SourceType = SYSTEM thì set SYSTEM, ngược lại set BUSINESS
        if(dto.getSourceType() != null && dto.getSourceType().equals(SourceType.SYSTEM)){
            entity.setSourceType(SourceType.SYSTEM);
        } else {
            entity.setSourceType(SourceType.BUSINESS);
        }
        return entity;
    }

    public  AttributeGroupDto maptoAttributeGroupDto(AttributeGroupEntity entity){

        return modelMapper.map(entity, AttributeGroupDto.class);
    }


}
