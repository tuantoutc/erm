package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.AttributeDto;
import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.in.rest.dto.AttributeValueDto;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeValueEntity;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeGroupRepository;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeValueRepository;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AttributeMapper {
    private final ModelMapper modelMapper;
    private final AttributeGroupRepository attributeGroupRepository ;
    private final AttributeGroupMapper attributeGroupMapper;
    private final AttributeValueRepository attributeValueRepository ;


    public AttributeDto maptoAttributeDto(AttributeEntity entity) {
        AttributeDto attributeDto = modelMapper.map(entity, AttributeDto.class);
        AttributeGroupEntity attributeGroupEntity= attributeGroupRepository.findById(entity.getAttributeGroupId())
                        .orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));
        attributeDto.setAttributeGroup(attributeGroupMapper.maptoAttributeGroupDto(attributeGroupEntity));

        if(entity.getDataType() == null)
        {
            List<AttributeValueEntity> listAttributeValue = attributeValueRepository.findByAttributeId(entity.getId());
            List<AttributeValueDto> listAttributeValueDto = listAttributeValue.stream()
                    .map(av -> modelMapper.map(av, AttributeValueDto.class))
                    .toList();
            attributeDto.setAttributeValues(listAttributeValueDto);
        }
        else attributeDto.setAttributeValues(null);
        return attributeDto;

    }
//    public AttributeDto maptoAttributeAndValue(AttributeGroupEntity entity) {
//
//        AttributeDto attributeDto = modelMapper.map(entity, AttributeDto.class);
//        AttributeGroupEntity attributeGroupEntity= attributeGroupRepository.findById(entity.getAttributeGroupId())
//                .orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));
//        attributeDto.setAttributeGroup(attributeGroupMapper.maptoAttributeGroupDto(attributeGroupEntity));
//        if(entity.getDataType() == null)
//        {
//            List<AttributeValueEntity> listAttributeValue = attributeValueRepository.findByAttributeId(entity.getId());
//            List<AttributeValueDto> listAttributeValueDto = listAttributeValue.stream()
//                    .map(av -> modelMapper.map(av, AttributeValueDto.class))
//                    .toList();
//            attributeDto.setAttributeValues(listAttributeValueDto);
//        }
//        else attributeDto.setAttributeValues(null);
//        return attributeDto;
//
//    }



}
