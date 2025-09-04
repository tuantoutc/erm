package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CauseCategoryMapper {

    private final ModelMapper modelMapper;
    private final CauseCategoryMapRepository causeCategoryMapRepository;
    private final SystemServiceClient systemServiceClient;


    public CauseCategoryDto mapToDto(CauseCategoryEntity causeCategoryEntity) {
        CauseCategoryDto dto = modelMapper.map(causeCategoryEntity, CauseCategoryDto.class);
        List<CauseCategoryMapEntity> listSystem = causeCategoryMapRepository.findByCauseCategoryId(causeCategoryEntity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            Set<Long> ids = listSystem.stream().map(CauseCategoryMapEntity::getSystemId).collect(Collectors.toSet());
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                dto.setSystemDtos(response.getData().getContent());
            }
        }
        return dto;
    }

}
