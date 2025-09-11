package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.EmployeeServiceClient;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.repository.*;
import com.example.erm_demo.domain.enums.DisplayType;
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
public class RiskMapper {

    private final ModelMapper modelMapper;
    private final SystemServiceClient systemServiceClient;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final RiskTypeRepository riskTypeRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final RiskTagRepository riskTagRepository;
    private final TagRepository tagRepository;
    private final RiskAttributeLineRepository riskAttributeLineRepository;
    private final RiskAttributeLineValueRepository riskAttributeLineValueRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Value("${access-token}")
    private String token;

    @Value("${current-domain}")
    private String currentDomain;


    public RiskDto maptoRiskDto(RiskEntity entity) {

        RiskDto dto = RiskDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .recordedTime(entity.getRecordedTime())
                .priorityLevel(entity.getPriorityLevel())
                .description(entity.getDescription())
                .expectedConsequence(entity.getExpectedConsequence())
                .level(entity.getLevel())
                .point(entity.getPoint())
                .build();
        // map system từ ngoài
        mapSystemToRisk(entity, dto);

        return dto;
    }

    private void mapSystemToRisk(RiskEntity entity, RiskDto dto) {
        Set<Long> ids = Set.of(entity.getSystemId());
        ApiResponse<PageResponseDto<SystemDto>> responseSystem = systemServiceClient.getSystemsByIds(ids, 0, 20);
        if (responseSystem.getData() != null && responseSystem.getData().getContent() != null) {
            dto.setSystem(responseSystem.getData().getContent().get(0));
        }
        RiskTypeEntity riskType = riskTypeRepository.findById(entity.getRiskTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        dto.setRiskType(RiskTypeDto.builder()
                .id(riskType.getId())
                .code(riskType.getCode())
                .name(riskType.getName())
                .build());
        RiskCategoryEntity riskCategory = riskCategoryRepository.findById(entity.getRiskCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        dto.setRiskCategory(RiskCategoryDto.builder()
                .id(riskCategory.getId())
                .code(riskCategory.getCode())
                .name(riskCategory.getName())
                .build());

        Set<Long> idsEmployee = Set.of(entity.getReporterId());
        ApiResponse<PageResponseDto<EmployeeDto>> responseEmployee = employeeServiceClient.getEmployeeByIds(idsEmployee, currentDomain, "Bearer " + token, 0, 20);
        if (responseEmployee.getData() != null && responseEmployee.getData().getContent() != null) {
            dto.setReporter(responseEmployee.getData().getContent().get(0));
        }

        List<TagDto> tags = new ArrayList<>();
        List<RiskTagEntity> risktTagEntities = riskTagRepository.findByRiskId(entity.getId());
        if (risktTagEntities != null && !risktTagEntities.isEmpty()) {
            for (var riksTagEntity : risktTagEntities) {
                TagEntity tagEntity = tagRepository.findById(riksTagEntity.getTagId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                TagDto tagDto = modelMapper.map(tagEntity, TagDto.class);
                tags.add(tagDto);
            }
            dto.setTags(tags);
        } else dto.setTags(null);

        List<RiskAttributeLineDto> riskAttributeLines = new ArrayList<>();
        List<RiskAttributeLineEntity> riskAttributeLineEntities = riskAttributeLineRepository.findByRiskId(entity.getId());
        if (riskAttributeLineEntities != null && !riskAttributeLineEntities.isEmpty()) {
            for (var riskAttributeLineEntity : riskAttributeLineEntities) {
                AttributeGroupDto attributeGroupDto = AttributeGroupDto.builder()
                        .id(riskAttributeLineEntity.getAtributeGroupId())
                        .build();
                AttributeEntity attribute = attributeRepository.findById(riskAttributeLineEntity.getAttributeId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

                RiskAttributeLineDto riskAttributeLineDto = RiskAttributeLineDto.builder()
                        .id(riskAttributeLineEntity.getId())
                        .attributeGroup(attributeGroupDto)
                        .attribute(attributeMapper.maptoAttributeDto(attribute))
                        .build();
                // map value cho attribute
                List<RiskAttributeLineValueDto> riskAttributeLineValues = new ArrayList<>();
                List<RiskAttributeLineValueEntity> riskAttributeLineValueEntities = riskAttributeLineValueRepository
                        .findByRiskLinesId(riskAttributeLineEntity.getId());
                if (riskAttributeLineValueEntities != null && !riskAttributeLineValueEntities.isEmpty()) {
                    for (var riskAttributeLineValueEntity : riskAttributeLineValueEntities) {
                        if (attribute.getDisplayType() == DisplayType.TEXTBOX) {
                            RiskAttributeLineValueDto riskAttributeLineValueDto = RiskAttributeLineValueDto.builder()
                                    .id(riskAttributeLineValueEntity.getId())
                                    .textValue(riskAttributeLineValueEntity.getTextValue())
                                    .build();
                            riskAttributeLineValues.add(riskAttributeLineValueDto);
                        } else {
                            AttributeValueEntity attributeValueEntity = attributeValueRepository.findById(riskAttributeLineValueEntity.getAttributeValuesId())
                                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

                            AttributeValueDto attributeValueDto = modelMapper.map(attributeValueEntity, AttributeValueDto.class);
                            RiskAttributeLineValueDto riskAttributeLineValueDto = RiskAttributeLineValueDto.builder()
                                    .id(riskAttributeLineValueEntity.getId())
                                    .attributeValue(attributeValueDto)
                                    .build();
                            riskAttributeLineValues.add(riskAttributeLineValueDto);
                        }
                    }
                    riskAttributeLineDto.setRiskAttributeLineValues(riskAttributeLineValues);
                }
                riskAttributeLines.add(riskAttributeLineDto);
            }
            dto.setRiskAttributeLines(riskAttributeLines);
        }

    }
}
