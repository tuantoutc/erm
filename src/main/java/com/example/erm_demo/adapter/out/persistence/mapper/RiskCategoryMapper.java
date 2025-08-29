package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RiskCategoryMapper {

    private final ModelMapper modelMapper;

    public RiskCategoryEntity maptoRiskCategory(RiskCategoryDto dto){
        RiskCategoryEntity entity = modelMapper.map(dto, RiskCategoryEntity.class);
        entity.setId(null);
        setRiskCategoryMapSystemFromDto(entity,dto);
        return entity;
    }
    public RiskCategoryEntity updateRiskCategory(RiskCategoryEntity entity, RiskCategoryDto dto){
        entity.getRiskCategoryMapEntities().clear();
        entity = modelMapper.map(dto, RiskCategoryEntity.class);
        setRiskCategoryMapSystemFromDto(entity,dto);
        return entity;
    }

    public  RiskCategoryDto maptoRiskCategoryDto(RiskCategoryEntity entity){

        RiskCategoryDto dto = modelMapper.map(entity, RiskCategoryDto.class);
        if(entity.getParent()!=null){
            RiskCategoryDto parent = RiskCategoryDto.builder()
                    .id(entity.getParent().getId())
                    .name(entity.getParent().getName())
                    .code(entity.getParent().getCode())
                    .build();
            dto.setParent(parent);
        }

        if(entity.getRiskCategoryMapEntities() != null){
            List<SystemDto> systemDtos = entity.getRiskCategoryMapEntities().stream().map(riskCategoryMapEntity -> {
                SystemDto systemDto = SystemDto.builder()
                        .id(riskCategoryMapEntity.getSystemId())
                        .build();
                return systemDto;
            }).toList();
            dto.setSystemDtos(systemDtos);
        }

        return dto;
    }

    private void setRiskCategoryMapSystemFromDto(RiskCategoryEntity entity, RiskCategoryDto dto){

        if(dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()){
            List<RiskCategoryMapEntity> riskCategoryMapEntities = dto.getSystemDtos().stream().map(systemDto -> {
                RiskCategoryMapEntity riskCategoryMapEntity = RiskCategoryMapEntity.builder()
                        .systemId(systemDto.getId())
                        .riskCategoryEntity(entity)
                        .build();
                return riskCategoryMapEntity;
            }).toList();
            entity.setRiskCategoryMapEntities(riskCategoryMapEntities);
        }
        else {
            entity.setRiskCategoryMapEntities(null);
        }

    }
}
