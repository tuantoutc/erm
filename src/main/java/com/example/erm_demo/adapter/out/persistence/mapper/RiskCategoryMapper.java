package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RiskCategoryMapper {

    private final ModelMapper modelMapper;
    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapRepository riskCategoryMapRepository;

    public  RiskCategoryDto maptoRiskCategoryDto(RiskCategoryEntity entity){

        RiskCategoryDto dto = modelMapper.map(entity, RiskCategoryDto.class);
        if(entity.getParentId()!=null){
            RiskCategoryEntity parentEntity = riskCategoryRepository.findById(entity.getParentId()).get();
            if(parentEntity!=null){
                dto.setParent(maptoRiskCategoryDto(parentEntity));
            }
        }
        else dto.setParent(null);

        List<RiskCategoryMapEntity> listRiskCategoryMap = riskCategoryMapRepository.findByRiskCategoryId(entity.getId());
        if (listRiskCategoryMap != null && !listRiskCategoryMap.isEmpty()) {
            List<SystemDto> systemDtos = listRiskCategoryMap.stream().map(item -> {
                return SystemDto.builder()
                        .id(item.getSystemId())
                        .build();
            }).toList();
            dto.setSystemDtos(systemDtos);
        }

        return dto;
    }


}
