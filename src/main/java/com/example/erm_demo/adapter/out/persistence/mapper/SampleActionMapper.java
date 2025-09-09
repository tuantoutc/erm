package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.DepartmentServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.PreventiveMeasureRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.SampleActionMapRepository;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class SampleActionMapper {

    private final ModelMapper modelMapper;
    private final CauseCategoryRepository causeCategoryRepository;
    private final RiskTypeRepository riskTypeRepository;
    private final SampleActionMapRepository sampleActionMapRepository;
    private final PreventiveMeasureRepository preventiveMeasureRepository;
    private final DepartmentServiceClient departmentServiceClient;

    @Value("${access-token}")
    private String token;

    public SampleActionDto mapToSampleActionDto(SampleActionEntity entity) {
        SampleActionDto sampleActionDto = modelMapper.map(entity, SampleActionDto.class);
        RiskTypeEntity riskTypeEntity = riskTypeRepository.findById(entity.getRiskTypeId())
                .orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));
        sampleActionDto.setRiskType(RiskTypeDto.builder()
                                                .id(riskTypeEntity.getId())
                                                .code(riskTypeEntity.getCode())
                                                .name(riskTypeEntity.getName())
                                                .build());
        CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(entity.getCauseCategoryId())
                .orElseThrow(()->new AppException(ErrorCode.ENTITY_NOT_FOUND));
        sampleActionDto.setCauseCategory(CauseCategoryDto.builder()
                                                        .id(causeCategoryEntity.getId())
                                                        .code(causeCategoryEntity.getCode())
                                                        .name(causeCategoryEntity.getName())
                                                        .build());

        mapListActionToSampleAction(sampleActionDto, entity);

        return sampleActionDto;
    }

    private void mapListActionToSampleAction(SampleActionDto dto, SampleActionEntity entity) {

        List<SampleActionMapEntity> listAction = sampleActionMapRepository.findBySampleActionId(entity.getId());
        List<SampleActionMapDto> sampleActionMapDtos = new ArrayList<>();
        if(listAction != null && !listAction.isEmpty())
        {
            for(var actionMap : listAction) {
                SampleActionMapDto mapDto = modelMapper.map(actionMap, SampleActionMapDto.class);
                PreventiveMeasureEntity preventiveMeasureEntity = preventiveMeasureRepository.findById(actionMap.getPreventiveMeasureId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                mapDto.setPreventiveMeasure(PreventiveMeasureDto.builder()
                                                                .id(preventiveMeasureEntity.getId())
                                                                .code(preventiveMeasureEntity.getCode())
                                                                .name(preventiveMeasureEntity.getName())
                                                                .build());
                try {
                    Set<Long> ids = Set.of(actionMap.getDepartmentId());
                    ApiResponse<PageResponseDto<DepartmentDto>> response = departmentServiceClient
                            .getDepartmentByIds(ids, 201L, "Bearer " + token, 0, 20);
                    if (response.getData().getContent() != null &&
                        !response.getData().getContent().isEmpty()) {
                        mapDto.setDepartment(response.getData().getContent().get(0));
                    }
                } catch (AppException e) {
                    throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                sampleActionMapDtos.add(mapDto);
            }
        }
        dto.setSampleActionMaps(sampleActionMapDtos);
    }

}