package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategory;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMap;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryMapRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RiskCategoryMapper {

    private final ModelMapper modelMapper;

    public RiskCategory maptoRiskCategory(RiskCategoryDto dto){
        RiskCategory entity = modelMapper.map(dto, RiskCategory.class);
        entity.setId(null);
        setRiskCategoryMapSystemFromDto(entity,dto);
        return entity;
    }
    public RiskCategory updateRiskCategory(RiskCategory entity,RiskCategoryDto dto){
        entity.getRiskCategoryMaps().clear();
        entity = modelMapper.map(dto, RiskCategory.class);
        setRiskCategoryMapSystemFromDto(entity,dto);
        return entity;
    }

    public  RiskCategoryDto maptoRiskCategoryDto(RiskCategory entity){

        RiskCategoryDto dto = modelMapper.map(entity, RiskCategoryDto.class);
        if(entity.getParent()!=null){
            RiskCategoryDto parent = RiskCategoryDto.builder()
                    .id(entity.getParent().getId())
                    .name(entity.getParent().getName())
                    .code(entity.getParent().getCode())
                    .build();
            dto.setParent(parent);
        }

        if(entity.getRiskCategoryMaps() != null){
            List<SystemDto> systemDtos = entity.getRiskCategoryMaps().stream().map(riskCategoryMap -> {
                SystemDto systemDto = SystemDto.builder()
                        .id(riskCategoryMap.getSystemId())
                        .build();
                return systemDto;
            }).toList();
            dto.setSystemDtos(systemDtos);
        }

        return dto;
    }

    private void setRiskCategoryMapSystemFromDto( RiskCategory entity, RiskCategoryDto dto){

        if(dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()){
            List<RiskCategoryMap> riskCategoryMaps = dto.getSystemDtos().stream().map(systemDto -> {
                RiskCategoryMap riskCategoryMap = RiskCategoryMap.builder()
                        .systemId(systemDto.getId())
                        .riskCategory(entity)
                        .build();
                return riskCategoryMap;
            }).toList();
            entity.setRiskCategoryMaps(riskCategoryMaps);
        }
        else {
            entity.setRiskCategoryMaps(null);
        }

    }
}
