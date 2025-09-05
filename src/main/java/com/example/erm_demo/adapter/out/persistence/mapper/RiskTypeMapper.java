package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeAttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeAttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeRepository;
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
    private final RiskTypeRepository riskTypeRepository;
    private final RiskTypeMapRepository riskTypeMapRepository;
    private final SystemServiceClient systemServiceClient;
    private final RiskTypeAttributeRepository riskTypeAttributeRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;


    public  RiskTypeDto maptoRiskTypeDto(RiskTypeEntity entity){

        RiskTypeDto dto = modelMapper.map(entity, RiskTypeDto.class);
        List<RiskTypeMapEntity> listSystem = riskTypeMapRepository.findByRiskTypeId(entity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            Set<Long> ids = listSystem.stream().map(RiskTypeMapEntity::getSystemId).collect(Collectors.toSet());
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                dto.setSystemDtos(response.getData().getContent());
            }
        }
        List<RiskTypeAttributeEntity> riskTypeAttributes = riskTypeAttributeRepository.findByRiskTypeId(entity.getId());
        List<RiskTypeAttributeDto> riskTypeAttributeDtos = new ArrayList<>();
        for (var item : riskTypeAttributes )
        {
            AttributeEntity attribute = attributeRepository.findById(item.getAttributeId()).orElseThrow(()->new RuntimeException("Attribute not found"));
            RiskTypeAttributeDto riskTypeAttributeDto = new RiskTypeAttributeDto();
            riskTypeAttributeDto.setId(item.getId());
            riskTypeAttributeDto.setAttribute(attributeMapper.maptoAttributeDto(attribute));
            AttributeGroupDto attributeGroupDto  = AttributeGroupDto.builder()
                    .id(attribute.getAttributeGroupId()).build();
            riskTypeAttributeDto.setAttributeGroup(attributeGroupDto);
            riskTypeAttributeDtos.add(riskTypeAttributeDto);
        }
        dto.setRiskTypeAttributes(riskTypeAttributeDtos);

        return dto;
    }


}
