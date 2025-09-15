package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.mapper.RiskMapper;
import com.example.erm_demo.adapter.out.persistence.repository.*;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.RiskFileService;
import com.example.erm_demo.application.service.RiskService;
import com.example.erm_demo.domain.enums.DisplayType;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.ObjectApplicableType;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class RiskServiceImpl implements RiskService {

    private final RiskRepository riskRepository;
    private final RiskMapper riskMapper;
    private final RiskFileRepository riskFileRepository;
    private final RiskFileService riskFileService;
    private final RiskTypeRepository riskTypeRepository;
    private final RiskCategoryRepository riskCategoryRepository;
    private final RiskTagRepository riskTagRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeRepository attributeRepository;
    private final RiskAttributeLineRepository riskAttributeLineRepository;
    private final RiskAttributeLineValueRepository riskAttributeLineValueRepository;
    private final TrackingCauseRepository trackingCauseRepository;
    private final TrackingCauseMapRepository trackingCauseMapRepository;
    private final TrackingCauseMapFailProductRepository trackingCauseMapFailProductRepository;
    private final SampleActionRepository sampleActionRepository;
    private final RiskCauseLineRepository riskCauseLineRepository;
    private final TrackingActionRepository trackingActionRepository;
    private final RiskCauseLineActionLineRepository riskCauseLineActionLineRepository;

    @Override
    @Transactional(readOnly = true)
    public RiskDto getRiskById(Long id) {
        return riskRepository.findById(id).map(riskMapper::maptoRiskDto).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public RiskDto createRisk(RiskDto dto) {
        if (!riskTypeRepository.existsById(dto.getRiskType().getId()) || !riskCategoryRepository.existsById(dto.getRiskCategory().getId())) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        RiskEntity riskEntity = RiskEntity.builder().code(dto.getCode())
                .name(dto.getName())
                .systemId(dto.getSystem().getId())
                .riskTypeId(dto.getRiskType().getId())
                .riskCategoryId(dto.getRiskCategory().getId())
                .reporterId(dto.getReporter().getId())
                .recordedTime(dto.getRecordedTime())
                .priorityLevel(dto.getPriorityLevel())
                .description(dto.getDescription())
                .expectedConsequence(dto.getExpectedConsequence()).level(dto.getLevel()).point(dto.getPoint()).build();
        riskEntity = riskRepository.save(riskEntity);
        // map tag to risk

        mapTagToRisk(dto, riskEntity);

//        uploadFileToRisk(files, riskEntity);

        mapAtributeToRisk(dto, riskEntity);

        mapCauseToRisk(dto, riskEntity);

        return riskMapper.maptoRiskDto(riskEntity);
    }


    private void mapTagToRisk(RiskDto riskDto, RiskEntity riskEntity) {
        if (riskDto.getTags() != null && !riskDto.getTags().isEmpty()) {
            for (var tagDto : riskDto.getTags()) {
                RiskTagEntity riskTagEntity = RiskTagEntity.builder().riskId(riskEntity.getId()).tagId(tagDto.getId()).build();
                riskTagRepository.save(riskTagEntity);
            }
        }
    }

    private void mapAtributeToRisk(RiskDto riskDto, RiskEntity riskEntity) {
        List<RiskAttributeLineDto> riskAttributeLines = riskDto.getRiskAttributeLines();
        if (riskAttributeLines != null && !riskAttributeLines.isEmpty()) {
            for (var riskAttributeLine : riskAttributeLines) {

                AttributeGroupEntity attributeGroup = attributeGroupRepository.findById(riskAttributeLine.getAttributeGroup().getId()).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                AttributeEntity attribute = attributeRepository.findById(riskAttributeLine.getAttribute().getId()).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                RiskAttributeLineEntity riskAttributeLineEntity = RiskAttributeLineEntity.builder()
                        .riskId(riskEntity.getId())
                        .atributeGroupId(attributeGroup.getId())
                        .attributeId(attribute.getId())
                        .build();
                riskAttributeLineEntity = riskAttributeLineRepository.save(riskAttributeLineEntity);

                mapAttributeValueToRisk(riskAttributeLine, riskAttributeLineEntity, attribute);
            }
        }
    }

    private void mapAttributeValueToRisk(RiskAttributeLineDto dto, RiskAttributeLineEntity riskAttributeLineEntity, AttributeEntity attribute) {
        List<RiskAttributeLineValueDto> riskAttributeLineValues = dto.getRiskAttributeLineValues();

        if (riskAttributeLineValues != null && !riskAttributeLineValues.isEmpty()) {
            for (var riskAttributeLineValue : riskAttributeLineValues) {
                if (attribute.getDisplayType() == DisplayType.TEXTBOX) {
                    RiskAttributeLineValueEntity riskAttributeLineValueEntity = RiskAttributeLineValueEntity.builder()
                            .riskLinesId(riskAttributeLineEntity.getId())
                            .textValue(riskAttributeLineValue.getTextValue()).build();
                    riskAttributeLineValueRepository.save(riskAttributeLineValueEntity);
                } else {
                    RiskAttributeLineValueEntity riskAttributeLineValueEntity = RiskAttributeLineValueEntity.builder()
                            .riskLinesId(riskAttributeLineEntity.getId())
                            .attributeValuesId(riskAttributeLineValue.getAttributeValue().getId()).build();
                    riskAttributeLineValueRepository.save(riskAttributeLineValueEntity);
                }
            }
        }
    }

    private void mapCauseToRisk(RiskDto riskDto, RiskEntity riskEntity) {

        List<RiskCauseLineDto> riskCauseLines = riskDto.getRiskCauseLines();
        if (riskCauseLines != null && !riskCauseLines.isEmpty()) {

            for (var riskCauseLine : riskCauseLines) {
                // add new tracking cause and tracking cause map to risk cause line
                TrackingCauseDto trackingCauseDto = riskCauseLine.getTrackingCause();
                if (trackingCauseDto.getId() == null) {
                    // create new tracking cause
                    TrackingCauseEntity trackingCause = TrackingCauseEntity.builder()
                            .causeCategoryId(trackingCauseDto.getCauseCategory().getId())
                            .causeId(trackingCauseDto.getCause().getId())
                            .count(1L)
                            .objectApplicableType(trackingCauseDto.getObjectApplicableType())
                            .state(trackingCauseDto.getState()).build();
                    trackingCauseRepository.save(trackingCause);
                    // add tracking cause map to tracking cause
                    addTrackingCauseMapToTrackingCause(trackingCause, trackingCauseDto);
                    // add sample action to risk cause line
                    if (!sampleActionRepository.existsById(riskCauseLine.getSampleAction().getId())) {
                        throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
                    }
                    RiskCauseLineEntity riskCauseLineEntity = RiskCauseLineEntity.builder()
                            .riskId(riskEntity.getId())
                            .trackingCauseId(trackingCause.getId())
                            .sampleActionId(riskCauseLine.getSampleAction().getId()).build();
                    riskCauseLineRepository.save(riskCauseLineEntity);
                    // map action line to risk cause line
                    addActionLineToRiskCauseLine(riskCauseLine, riskCauseLineEntity);

                } else {
                    if (trackingCauseDto.getId() != 0L && trackingCauseDto.getId() != null) {
                        TrackingCauseEntity trackingCauseEntity = trackingCauseRepository.findById(trackingCauseDto.getId()).orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
                        trackingCauseEntity.setCount(trackingCauseEntity.getCount() + 1L);
                        trackingCauseRepository.save(trackingCauseEntity);

                        if (!sampleActionRepository.existsById(riskCauseLine.getSampleAction().getId())) {
                            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
                        }
                        RiskCauseLineEntity riskCauseLineEntity = RiskCauseLineEntity.builder().riskId(riskEntity.getId()).trackingCauseId(trackingCauseDto.getId()).sampleActionId(riskCauseLine.getSampleAction().getId()).build();
                        riskCauseLineRepository.save(riskCauseLineEntity);
                        // map action line to risk cause line
                        addActionLineToRiskCauseLine(riskCauseLine, riskCauseLineEntity);

                    }
                }
            }
        }
    }


    // add new  tracking cause map to tracking cause
    private void addTrackingCauseMapToTrackingCause(TrackingCauseEntity trackingCause, TrackingCauseDto trackingCauseDto) {
        List<TrackingCauseMapDto> trackingCauseMaps = trackingCauseDto.getTrackingCauseMaps();
        if (trackingCauseMaps != null && !trackingCauseMaps.isEmpty()) {
            if (trackingCause.getObjectApplicableType() == ObjectApplicableType.HUMAN) {
                for (var trackingCauseMap : trackingCauseMaps) {
                    TrackingCauseMapEntity trackingCauseMapEntity = TrackingCauseMapEntity.builder()
                            .departmentId(trackingCauseMap.getDepartment().getId())
                            .employeeId(trackingCauseMap.getEmployee().getId()).positionId(trackingCauseMap.getPositionId()).trackingCauseId(trackingCause.getId()).build();
                    trackingCauseMapRepository.save(trackingCauseMapEntity);
                }
            }

            if (trackingCause.getObjectApplicableType() == ObjectApplicableType.PARTNER) {
                for (var trackingCauseMap : trackingCauseMaps) {
                    TrackingCauseMapEntity trackingCauseMapEntity = TrackingCauseMapEntity.builder().parnerType(trackingCauseMap.getParnerType()).groupParnerId(trackingCauseMap.getGroupParnerId()).parnerId(trackingCauseMap.getParnerId()).trackingCauseId(trackingCause.getId()).build();
                    trackingCauseMapRepository.save(trackingCauseMapEntity);
                }
            }

            if (trackingCause.getObjectApplicableType() == ObjectApplicableType.PROVIDER) {
                for (var trackingCauseMap : trackingCauseMaps) {
                    TrackingCauseMapEntity trackingCauseMapEntity = TrackingCauseMapEntity.builder().groupParnerId(trackingCauseMap.getGroupParnerId()).parnerId(trackingCauseMap.getParnerId()).trackingCauseId(trackingCause.getId()).build();
                    trackingCauseMapRepository.save(trackingCauseMapEntity);
                }
            }

            if (trackingCause.getObjectApplicableType() == ObjectApplicableType.DEVICE) {
                for (var trackingCauseMap : trackingCauseMaps) {
                    TrackingCauseMapEntity trackingCauseMapEntity = TrackingCauseMapEntity.builder().departmentId(trackingCauseMap.getDepartment().getId()).productId(trackingCauseMap.getProductId()).trackingCauseId(trackingCause.getId()).build();
                    trackingCauseMapRepository.save(trackingCauseMapEntity);
                    List<DicDto> dics = trackingCauseMap.getDics();
                    if (dics != null && !dics.isEmpty()) {
                        for (var dic : dics) {
                            TrackingCauseMapFailProductEntity trackingCauseMapFailProductEntity = TrackingCauseMapFailProductEntity.builder().trackingCausesMapId(trackingCauseMapEntity.getId()).dicId(dic.getId()).build();
                            trackingCauseMapFailProductRepository.save(trackingCauseMapFailProductEntity);
                        }
                    }
                }

            }

        }
    }


    private void addActionLineToRiskCauseLine(RiskCauseLineDto riskCauseLine, RiskCauseLineEntity riskCauseLineEntity) {
        List<RiskCauseLineActionLineDto> riskCauseLineActionLines = riskCauseLine.getRiskCauseLineActionLines();
        if (riskCauseLineActionLines != null && !riskCauseLineActionLines.isEmpty()) {
            for (var riskCauseLineActionLine : riskCauseLineActionLines) {
                TrackingActionDto trackingActionDto = riskCauseLineActionLine.getTrackingAction();
                TrackingActionEntity trackingAction = TrackingActionEntity.builder()
                        .actionType(trackingActionDto.getType())
                        .preventiveMeasureId(trackingActionDto.getPreventiveMeasure().getId())
                        .departmentId(trackingActionDto.getDepartment().getId())
                        .content(trackingActionDto.getContent())
                        .planDate(trackingActionDto.getPlanDate()).build();
                trackingActionRepository.save(trackingAction);
                RiskCauseLineActionLineEntity riskCauseLineActionLineEntity = RiskCauseLineActionLineEntity.builder()
                        .riskCauseLineId(riskCauseLineEntity.getId())
                        .trackingActionId(trackingAction.getId()).build();
                riskCauseLineActionLineRepository.save(riskCauseLineActionLineEntity);

            }
        }
    }

    private void uploadFileToRisk(MultipartFile[] files, RiskEntity riskEntity) {
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        // Save file to disk
                        String filePath = riskFileService.saveFile(file);
                        // Save file info to database
                        RiskFileEntity riskFile = RiskFileEntity.builder().name(UUID.randomUUID().toString()).url(filePath).type(file.getContentType()).createdAt(LocalDateTime.now()).riskId(riskEntity.getId()).build();

                        riskFileRepository.save(riskFile);
                    } catch (Exception e) {
                        // Log error nhưng không dừng quá trình tạo risk
                        System.err.println("Lỗi upload file: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public RiskDto updateRisk(RiskDto dto, MultipartFile[] files) {
        return null;
    }

    @Override
    @Transactional
    public void deleteRisk(Long id) {
        if (!riskRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        riskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<RiskDto>> search(String code, Long systemId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);


        Page<RiskEntity> page = riskRepository.findAll(pageable);
        Page<RiskDto> riskDtoPage = page.map(riskMapper::maptoRiskDto);

        return ApiResponseUtil.createPageResponse(riskDtoPage);
    }
}
