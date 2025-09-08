package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.repository.*;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RiskTypeMapper {

    private final ModelMapper modelMapper;
    private final RiskTypeMapRepository riskTypeMapRepository;
    private final SystemServiceClient systemServiceClient;
    private final RiskTypeAttributeRepository riskTypeAttributeRepository;
    private final RiskTypeAttributeValueRepository riskTypeAttributeValueRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final AttributeValueRepository attributeValueRepository;


    public  RiskTypeDto maptoRiskTypeDto(RiskTypeEntity entity){

        RiskTypeDto dto = modelMapper.map(entity, RiskTypeDto.class);
        // map system từ ngoài
        mapSystemToRiskType(entity, dto);

        // map attribute vao riskType
        mapAttributeToRiskType(dto, entity);

        return dto;
    }

    private void mapSystemToRiskType(RiskTypeEntity entity, RiskTypeDto dto) {
        List<RiskTypeMapEntity> listSystem = riskTypeMapRepository.findByRiskTypeId(entity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            Set<Long> ids = listSystem.stream().map(RiskTypeMapEntity::getSystemId).collect(Collectors.toSet());
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                dto.setSystemDtos(response.getData().getContent());
            }
        }
    }
// map attribute vao riskType : lấy ra danh sách attribute của riskType
// với mỗi attribute lấy ra danh sách attributeValue
// gán vào dto
    private void mapAttributeToRiskType(RiskTypeDto riskTypeDto, RiskTypeEntity entity) {
        List<RiskTypeAttributeEntity> riskTypeAttributes = riskTypeAttributeRepository.findByRiskTypeId(entity.getId());
        List<RiskTypeAttributeDto> riskTypeAttributeDtos = new ArrayList<>();
        for (var item : riskTypeAttributes )
        {
            AttributeEntity attribute = attributeRepository.findById(item.getAttributeId()).orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));
            AttributeGroupDto attributeGroupDto  = AttributeGroupDto.builder()
                    .id(attribute.getAttributeGroupId()).build();

            RiskTypeAttributeDto riskTypeAttributeDto =RiskTypeAttributeDto.builder()
                    .id(item.getId())
                    .attributeGroup(attributeGroupDto)
                    .attribute(attributeMapper.maptoAttributeDto(attribute))
                    .build();
            // map value cho attribute
            mapAttributeValueToRiskType(riskTypeAttributeDto, item);

            riskTypeAttributeDtos.add(riskTypeAttributeDto);

        }
        riskTypeDto.setRiskTypeAttributes(riskTypeAttributeDtos);
    }

// lấy các value được chọn trong attribute ra và hiển thị
    private void mapAttributeValueToRiskType(RiskTypeAttributeDto riskTypeAttributeDto, RiskTypeAttributeEntity entity) {

        List<RiskTypeAttributeValueEntity> riskTypeAttributeValues= riskTypeAttributeValueRepository.findByRiskTypesAttributeId(entity.getId());
        List<RiskTypeAttributeValueDto> riskTypeAttributeValueDtos = new ArrayList<>();

        for (var valueEntity : riskTypeAttributeValues){

            AttributeValueEntity attributeValueEntity = attributeValueRepository.findById(valueEntity.getAttributeValueId())
                    .orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));

            AttributeValueDto attributeValueDto = AttributeValueDto.builder()
                    .id(attributeValueEntity.getId())
                    .value(attributeValueEntity.getValue())
                    .build();

            RiskTypeAttributeValueDto riskTypeAttributeValueDto = RiskTypeAttributeValueDto.builder()
                    .id(valueEntity.getId())
                    .attributeValue(attributeValueDto)
                    .build();

            riskTypeAttributeValueDtos.add(riskTypeAttributeValueDto);

        }
        riskTypeAttributeDto.setRiskTypeAttributeValues(riskTypeAttributeValueDtos);

    }

}
