package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.SampleActionDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.SampleActionEntity;
import com.example.erm_demo.adapter.out.persistence.entity.SampleActionMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.SampleActionMapper;
import com.example.erm_demo.adapter.out.persistence.repository.*;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.SampleActionService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SampleActionImpl implements SampleActionService {

    private final SampleActionRepository sampleActionRepository;
    private final SampleActionMapRepository sampleActionMapRepository;
    private final ModelMapper modelMapper;
    private final RiskTypeRepository riskTypeRepository;
    private final CauseCategoryRepository causeCategoryRepository;
    private final PreventiveMeasureRepository preventiveMeasureRepository;
    private final SampleActionMapper sampleActionMapper;


    @Override
    @Transactional
    public SampleActionDto createSampleAction(SampleActionDto dto) {
        SampleActionEntity entity = modelMapper.map(dto, SampleActionEntity.class);
        if(!riskTypeRepository.existsById(dto.getRiskType().getId())
                && !causeCategoryRepository.existsById(dto.getCauseCategory().getId()) )
        {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        entity.setRiskTypeId(dto.getRiskType().getId());
        entity.setCauseCategoryId(dto.getCauseCategory().getId());

        entity = sampleActionRepository.save(entity);

        mapActionToSampleAction(dto, entity);

        return sampleActionMapper.mapToSampleActionDto(entity);
    }
    private void mapActionToSampleAction(SampleActionDto dto, SampleActionEntity entity) {
        if (dto.getSampleActionMaps() != null && !dto.getSampleActionMaps().isEmpty())
        {
            for(var sampleActionMapDto : dto.getSampleActionMaps())
            {
                SampleActionMapEntity mapEntity = modelMapper.map(sampleActionMapDto, SampleActionMapEntity.class);
                mapEntity.setSampleActionId(entity.getId());
                if(!preventiveMeasureRepository.existsById(sampleActionMapDto.getPreventiveMeasure().getId()))
                {
                    throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
                }
                mapEntity.setPreventiveMeasureId(sampleActionMapDto.getPreventiveMeasure().getId());
                mapEntity.setDepartmentId(sampleActionMapDto.getDepartment().getId());
                sampleActionMapRepository.save(mapEntity);
            }

        }
    }


    @Override
    @Transactional
    public SampleActionDto updateSampleAction(SampleActionDto dto) {
        SampleActionEntity existingEntity = sampleActionRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        existingEntity = modelMapper.map(dto, SampleActionEntity.class);

        addRiskTypeAndCauseCategoryToSampleAction(dto, existingEntity);

        sampleActionMapRepository.deleteBySampleActionId(existingEntity.getId());

        mapActionToSampleAction(dto, existingEntity);

        return sampleActionMapper.mapToSampleActionDto(sampleActionRepository.save(existingEntity)) ;
    }

    private void addRiskTypeAndCauseCategoryToSampleAction(SampleActionDto dto, SampleActionEntity entity) {
        if (!dto.getRiskType().getId().equals(entity.getRiskTypeId()))
        {
            RiskTypeEntity riskTypeEntity = riskTypeRepository.findById(dto.getRiskType().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            entity.setRiskTypeId(riskTypeEntity.getId());
        }
        else entity.setRiskTypeId(dto.getRiskType().getId());

        if (!dto.getCauseCategory().getId().equals(entity.getCauseCategoryId()))
        {
            CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(dto.getCauseCategory().getId())
                    .orElseThrow(()-> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            entity.setCauseCategoryId(causeCategoryEntity.getId());
        }
        else entity.setCauseCategoryId(dto.getCauseCategory().getId());

    }


    @Override
    @Transactional(readOnly = true)
    public SampleActionDto getSampleActionById(Long id) {
        return sampleActionRepository.findById(id)
                .map(sampleActionMapper::mapToSampleActionDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    public void deleteSampleAction(Long id) {
        if(!sampleActionRepository.existsById(id))
        {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        sampleActionMapRepository.deleteBySampleActionId(id);
        sampleActionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<SampleActionDto>> search(String code, Long riskTypeId, Long causeCategoryId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<SampleActionEntity> keywordSpec = BaseSpecification.hasKeyword(code);
        Specification<SampleActionEntity> riskTypeSpec = BaseSpecification.hasFieldEqual("riskTypeId", riskTypeId);
        Specification<SampleActionEntity> causeCategorySpec = BaseSpecification.hasFieldEqual("causeCategoryId", causeCategoryId);
        Specification<SampleActionEntity> isActiveSpec = BaseSpecification.hasFieldBoolean("isActive", isActive);
        Specification<SampleActionEntity> spec = Specification.where(keywordSpec)
                                                                .and(riskTypeSpec)
                                                                .and(causeCategorySpec)
                                                                .and(isActiveSpec);

        Page<SampleActionEntity> page = sampleActionRepository.findAll(spec, pageable);
        Page<SampleActionDto> dtoPage = page.map(sampleActionMapper::mapToSampleActionDto);

        return ApiResponseUtil.createPageResponse(dtoPage);

    }
}
