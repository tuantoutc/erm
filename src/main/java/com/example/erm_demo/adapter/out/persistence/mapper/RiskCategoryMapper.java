package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RiskCategoryMapper {

    private final ModelMapper modelMapper;
    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskCategoryMapRepository riskCategoryMapRepository;
    private final SystemServiceClient systemServiceClient;

    public  RiskCategoryDto maptoRiskCategoryDto(RiskCategoryEntity entity){

        RiskCategoryDto dto = modelMapper.map(entity, RiskCategoryDto.class);
        if(entity.getParentId()!=null){
            RiskCategoryEntity parentEntity = riskCategoryRepository.findById(entity.getParentId()).get();
            if(parentEntity!=null){
                RiskCategoryDto parent = RiskCategoryDto.builder()
                        .id(parentEntity.getId())
                        .name(parentEntity.getName())
                        .code(parentEntity.getCode())
                        .build();

                dto.setParent(parent);
            }
        }
        else dto.setParent(null);

        List<RiskCategoryMapEntity> listSystem = riskCategoryMapRepository.findByRiskCategoryId(entity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            Set<Long> ids = listSystem.stream().map(RiskCategoryMapEntity::getSystemId).collect(Collectors.toSet());
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                dto.setSystemDtos(response.getData().getContent());
            }
        }

        return dto;
    }


}
