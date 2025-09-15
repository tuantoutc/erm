package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.repository.*;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.application.service.PreventiveMeasureService;
import com.example.erm_demo.application.service.SampleActionService;
import com.example.erm_demo.domain.enums.DisplayType;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.ObjectApplicableType;
import com.example.erm_demo.domain.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RiskMapper {

    private final ModelMapper modelMapper;
    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;
    private final RiskTypeRepository riskTypeRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskTagRepository riskTagRepository;
    private final TagRepository tagRepository;
    private final RiskAttributeLineRepository riskAttributeLineRepository;
    private final RiskAttributeLineValueRepository riskAttributeLineValueRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final RiskCauseLineRepository riskCauseLineRepository;
    private final TrackingCauseRepository trackingCauseRepository;
    private final CauseCategoryService causeCategoryService;
    private final CauseService causeService;
    private final SampleActionService sampleActionService;
    private final TrackingCauseMapRepository trackingCauseMapRepository;

    private final ExternalServiceValue externalServiceValue;

    private final TrackingCauseMapFailProductRepository trackingCauseMapFailProductRepository;
    private final RiskCauseLineActionLineRepository riskCauseLineActionLineRepository;
    private final TrackingActionRepository trackingActionRepository;
    private final PreventiveMeasureService preventiveMeasureService;


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
        mapOtherValueToRisk(entity, dto);
        // map tags
        mapTagsToRisk(entity, dto);
        // map cause
        mapCauseToRisk(entity, dto);

        return dto;
    }

    private void mapTagsToRisk(RiskEntity entity, RiskDto dto) {
        List<TagDto> tags = new ArrayList<>();
        List<RiskTagEntity> risktTagEntities = riskTagRepository.findByRiskId(entity.getId());
        if (risktTagEntities != null && !risktTagEntities.isEmpty()) {
            for (var riskTagEntity : risktTagEntities) {
                TagEntity tagEntity = tagRepository.findById(riskTagEntity.getTagId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                TagDto tagDto = modelMapper.map(tagEntity, TagDto.class);
                tags.add(tagDto);
            }
            dto.setTags(tags);
        } else dto.setTags(null);
    }

    private void mapOtherValueToRisk(RiskEntity entity, RiskDto dto) {
        // map system từ ngoài
        dto.setSystem(externalServiceValue.getSystemId(entity.getSystemId()));
        // map reporter từ ngoài
        dto.setReporter(externalServiceValue.getEmployeeById(entity.getReporterId()));
        // map riskType
        RiskTypeEntity riskType = riskTypeRepository.findById(entity.getRiskTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        dto.setRiskType(RiskTypeDto.builder()
                .id(riskType.getId())
                .code(riskType.getCode())
                .name(riskType.getName())
                .build());
        // map riskCategory
        RiskCategoryEntity riskCategory = riskCategoryRepository.findById(entity.getRiskCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        dto.setRiskCategory(RiskCategoryDto.builder()
                .id(riskCategory.getId())
                .code(riskCategory.getCode())
                .name(riskCategory.getName())
                .build());

        // map attribute vao risk
        addAttributeToRisk(entity, dto);

    }

    private void addAttributeToRisk(RiskEntity entity, RiskDto dto) {
        // map attribute vao risk
        List<RiskAttributeLineDto> riskAttributeLines = new ArrayList<>();
        List<RiskAttributeLineEntity> riskAttributeLineEntities = riskAttributeLineRepository.findByRiskId(entity.getId());
        if (riskAttributeLineEntities != null && !riskAttributeLineEntities.isEmpty()) {
            for (var riskAttributeLineEntity : riskAttributeLineEntities) {
                AttributeGroupDto attributeGroupDto = AttributeGroupDto.builder()
                        .id(riskAttributeLineEntity.getAtributeGroupId())
                        .build();
                AttributeEntity attribute = attributeRepository.findById(riskAttributeLineEntity.getAttributeId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

                AttributeDto attributeDto = AttributeDto.builder()
                        .id(attribute.getId())
                        .name(attribute.getName())
                        .code(attribute.getCode())
                        .dataType(attribute.getDataType())
                        .displayType(attribute.getDisplayType())
                        .build();
                RiskAttributeLineDto riskAttributeLineDto = RiskAttributeLineDto.builder()
                        .id(riskAttributeLineEntity.getId())
                        .attributeGroup(attributeGroupDto)
                        .attribute(attributeDto)
                        .build();
                // map value cho attribute
                mapAttributeValueToAttribute(riskAttributeLineEntity, attribute, riskAttributeLineDto);

                riskAttributeLines.add(riskAttributeLineDto);
            }
            dto.setRiskAttributeLines(riskAttributeLines);
        }
    }


    private void mapAttributeValueToAttribute(RiskAttributeLineEntity riskAttributeLineEntity, AttributeEntity attribute, RiskAttributeLineDto riskAttributeLineDto) {
        // map value cho attribute
        List<RiskAttributeLineValueDto> riskAttributeLineValues = new ArrayList<>();
        List<RiskAttributeLineValueEntity> riskAttributeLineValueEntities = riskAttributeLineValueRepository
                .findByRiskLinesId(riskAttributeLineEntity.getId());

        if (riskAttributeLineValueEntities != null && !riskAttributeLineValueEntities.isEmpty()) {
            for (var riskAttributeLineValueEntity : riskAttributeLineValueEntities) {
                // if attribute displayType is TEXTBOX, map textValue and display
                if (attribute.getDisplayType() == DisplayType.TEXTBOX) {
                    RiskAttributeLineValueDto riskAttributeLineValueDto = RiskAttributeLineValueDto.builder()
                            .id(riskAttributeLineValueEntity.getId())
                            .textValue(riskAttributeLineValueEntity.getTextValue())
                            .build();
                    riskAttributeLineValues.add(riskAttributeLineValueDto);
                }
                // displayType value attribute choice
                else {
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
    }

    void mapCauseToRisk(RiskEntity entity, RiskDto dto) {
        List<RiskCauseLineEntity> riskCauseLineEntities = riskCauseLineRepository.findByRiskId(entity.getId());
        List<RiskCauseLineDto> riskCauseLines = new ArrayList<>();
        if (riskCauseLineEntities != null && !riskCauseLineEntities.isEmpty()) {
            for (var riskCauseLineEntity : riskCauseLineEntities) {

                // TrackingCauseDto
                TrackingCauseEntity trackingCauseEntity = trackingCauseRepository.findById(riskCauseLineEntity.getTrackingCauseId())
                        .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                TrackingCauseDto trackingCauseDto = TrackingCauseDto.builder()
                        .id(trackingCauseEntity.getId())
                        .objectApplicableType(trackingCauseEntity.getObjectApplicableType())
                        .count(trackingCauseEntity.getCount())
                        .state(trackingCauseEntity.getState())
                        .cause(causeService.getCauseById(trackingCauseEntity.getCauseId()))
                        .causeCategory(causeCategoryService.getCauseCategoryById(trackingCauseEntity.getCauseCategoryId()))
                        .build();

                // trackingCauseMapDto
//                List<TrackingCauseMapEntity> trackingCauseMapEntities = trackingCauseMapRepository.findByTrackingCauseId(trackingCauseEntity.getId());
//                if (trackingCauseMapEntities != null && !trackingCauseMapEntities.isEmpty()) {
//                    List<TrackingCauseMapDto> trackingCauseMapDtos = new ArrayList<>();
//                    if (trackingCauseEntity.getObjectApplicableType() == ObjectApplicableType.HUMAN) {
//                        for (var trackingCauseMapEntity : trackingCauseMapEntities) {
//                            TrackingCauseMapDto trackingCauseMapDto = TrackingCauseMapDto.builder()
//                                    .id(trackingCauseMapEntity.getId())
//                                    .department(externalServiceValue.getDepartmentById(trackingCauseMapEntity.getDepartmentId()))
//                                    .positionId(trackingCauseMapEntity.getPositionId())
//                                    .employee(externalServiceValue.getEmployeeById(trackingCauseMapEntity.getEmployeeId()))
//                                    .build();
//                            trackingCauseMapDtos.add(trackingCauseMapDto);
//                        }
//                    }
//                    if (trackingCauseEntity.getObjectApplicableType() == ObjectApplicableType.PROVIDER) {
//                        for (var trackingCauseMapEntity : trackingCauseMapEntities) {
//                            TrackingCauseMapDto trackingCauseMapDto = TrackingCauseMapDto.builder()
//                                    .id(trackingCauseMapEntity.getId())
//                                    .groupParnerId(trackingCauseMapEntity.getGroupParnerId())
//                                    .parnerId(trackingCauseMapEntity.getParnerId())
//                                    .build();
//                            trackingCauseMapDtos.add(trackingCauseMapDto);
//                        }
//                    }
//                    if (trackingCauseEntity.getObjectApplicableType() == ObjectApplicableType.PARTNER) {
//                        for (var trackingCauseMapEntity : trackingCauseMapEntities) {
//                            TrackingCauseMapDto trackingCauseMapDto = TrackingCauseMapDto.builder()
//                                    .id(trackingCauseMapEntity.getId())
//                                    .parnerType(trackingCauseMapEntity.getParnerType())
//                                    .groupParnerId(trackingCauseMapEntity.getGroupParnerId())
//                                    .parnerId(trackingCauseMapEntity.getParnerId())
//                                    .build();
//                            trackingCauseMapDtos.add(trackingCauseMapDto);
//                        }
//                    }
//                    if (trackingCauseEntity.getObjectApplicableType() == ObjectApplicableType.DEVICE) {
//                        for (var trackingCauseMapEntity : trackingCauseMapEntities) {
//                            TrackingCauseMapDto trackingCauseMapDto = TrackingCauseMapDto.builder()
//                                    .id(trackingCauseMapEntity.getId())
//                                    .department(externalServiceValue.getDepartmentById(trackingCauseMapEntity.getDepartmentId()))
//                                    .productId(trackingCauseMapEntity.getProductId())
//                                    .build();
//                            List<TrackingCauseMapFailProductEntity> failProductEntities =
//                                    trackingCauseMapFailProductRepository.findByTrackingCausesMapId(trackingCauseMapEntity.getId());
//                            if (failProductEntities != null && !failProductEntities.isEmpty()) {
//                                List<DicDto> dicDtos = new ArrayList<>();
//                                for (var failProductEntity : failProductEntities) {
//                                    DicDto dicDto = DicDto.builder()
//                                            .id(failProductEntity.getDicId())
//                                            .build();
//                                    dicDtos.add(dicDto);
//                                }
//
//                                trackingCauseMapDto.setDics(dicDtos);
//                            }
//                            trackingCauseMapDtos.add(trackingCauseMapDto);
//                        }
//                    }
//                    trackingCauseDto.setTrackingCauseMaps(trackingCauseMapDtos);
//
//                }

                // SampleAction
                SampleActionDto sampleActionDto = sampleActionService.getSampleActionById(riskCauseLineEntity.getSampleActionId());

                RiskCauseLineDto riskCauseLineDto = RiskCauseLineDto.builder()
                        .id(riskCauseLineEntity.getId())
                        .trackingCause(trackingCauseDto)
                        .sampleAction(sampleActionDto)
                        .build();

                List<RiskCauseLineActionLineEntity> riskCauseLineActionLineEntities =
                        riskCauseLineActionLineRepository.findByRiskCauseLineId(riskCauseLineEntity.getId());
                if (riskCauseLineActionLineEntities != null && !riskCauseLineActionLineEntities.isEmpty()) {
                    List<RiskCauseLineActionLineDto> riskCauseLineActionLineDtos = new ArrayList<>();
                    for (var riskCauseLineActionLineEntity : riskCauseLineActionLineEntities) {
                        TrackingActionEntity trackingActionEntity = trackingActionRepository.findById(riskCauseLineActionLineEntity.getTrackingActionId())
                                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                        TrackingActionDto trackingActionDto = TrackingActionDto.builder()
                                .id(trackingActionEntity.getId())
                                .type(trackingActionEntity.getActionType())
                                .content(trackingActionEntity.getContent())
                                .planDate(trackingActionEntity.getPlanDate())
                                .preventiveMeasure(preventiveMeasureService.getPreventiveMeasureById(trackingActionEntity.getPreventiveMeasureId()))
                                .department(externalServiceValue.getDepartmentById(trackingActionEntity.getDepartmentId()))
                                .build();
                        RiskCauseLineActionLineDto riskCauseLineActionLineDto = RiskCauseLineActionLineDto.builder()
                                .id(riskCauseLineActionLineEntity.getId())
                                .trackingAction(trackingActionDto)
                                .build();
                        riskCauseLineActionLineDtos.add(riskCauseLineActionLineDto);
                    }
                    riskCauseLineDto.setRiskCauseLineActionLines(riskCauseLineActionLineDtos);
                }
                riskCauseLines.add(riskCauseLineDto);
            }
            dto.setRiskCauseLines(riskCauseLines);
        }

    }
}