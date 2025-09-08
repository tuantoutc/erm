package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseMapRepository;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class CauseMapper {

    private final ModelMapper modelMapper;
    private final CauseCategoryMapper causeCategoryMapper;
    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseMapRepository causeMapRepository;
    private final SystemServiceClient systemServiceClient;


    public CauseDto mapToCauseDto(CauseEntity entity) {
        CauseDto causeDto = modelMapper.map(entity, CauseDto.class);
        CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(entity.getCauseCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        CauseCategoryDto causeCategoryDto  = CauseCategoryDto.builder()
                                            .id(causeCategoryEntity.getId())
                                            .name(causeCategoryEntity.getName())
                                            .code(causeCategoryEntity.getCode())
                                            .build();
        causeDto.setCauseCategory(causeCategoryDto);

        List<CauseMapEntity> listSystem = causeMapRepository.findByCauseId(entity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            Set<Long> ids = listSystem.stream().map(CauseMapEntity::getSystemId).collect(Collectors.toSet());
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response != null && response.getData() != null && response.getData().getContent() != null) {
                causeDto.setSystemDtos(response.getData().getContent());
            }
        }
        return causeDto;
    }


}

