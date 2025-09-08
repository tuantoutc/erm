package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.*;
import com.example.erm_demo.adapter.out.persistence.mapper.RiskTypeMapper;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeAttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeAttributeValueRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeRepository;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.RiskTypeService;
import com.example.erm_demo.domain.enums.DisplayType;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class RiskTypeServiceImpl implements RiskTypeService {

    private final RiskTypeRepository riskTypeRepository;

    private final RiskTypeMapper riskTypeMapper;

    private final ModelMapper modelMapper;

    private final RiskTypeMapRepository riskTypeMapRepository;
    private final RiskTypeAttributeRepository riskTypeAttributeRepository;
    private final RiskTypeAttributeValueRepository riskTypeAttributeValueRepository;



    @Override
    @Transactional(readOnly = true)
    public RiskTypeDto getRiskTypeById(Long id) {
        return riskTypeRepository.findById(id)
                .map(riskTypeMapper::maptoRiskTypeDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public RiskTypeDto createRiskType(RiskTypeDto dto) {

        // kiem tra tinh hop le cua origin va object
        if (!Origin.isValidObjectForOrigin(dto.getOrigin(), dto.getObject()))
        {
            throw new AppException(ErrorCode.INVALID_OBJECT_FOR_ORIGIN);
        }
        RiskTypeEntity entity = modelMapper.map(dto, RiskTypeEntity.class);

        entity = riskTypeRepository.save(entity);
        // map phần system vào cho risk type
        mapSystemToRiskType(entity, dto);
        // map phần attribute vào cho risk type
        mapAttributeToRiskType(dto, entity);

        return riskTypeMapper.maptoRiskTypeDto(entity);
    }

    private void mapSystemToRiskType(RiskTypeEntity entity, RiskTypeDto dto) {
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            for (var systemDto : dto.getSystemDtos()) {
                RiskTypeMapEntity riskTypeMapEntity = RiskTypeMapEntity
                        .builder()
                        .systemId(systemDto.getId())
                        .riskTypeId(entity.getId())
                        .build();
                riskTypeMapRepository.save(riskTypeMapEntity);
            }
        }
    }
    private void mapAttributeToRiskType(RiskTypeDto dto, RiskTypeEntity entity) {
        if(dto.getRiskTypeAttributes() != null && !dto.getRiskTypeAttributes().isEmpty()) {
            for(var riskTypeAttributeDto : dto.getRiskTypeAttributes()) {
                RiskTypeAttributeEntity riskTypeAttributeEntity = RiskTypeAttributeEntity.builder()
                        .attributeId(riskTypeAttributeDto.getAttribute().getId())
                        .riskTypeId(entity.getId())
                        .attributeGroupId(riskTypeAttributeDto.getAttributeGroup().getId())
                        .build();
                // logic to map attributes to risk type
                riskTypeAttributeRepository.save(riskTypeAttributeEntity);
                // map cac giá trị của attribute vào risk type
                mapAttributeValueToRiskType(riskTypeAttributeDto, riskTypeAttributeEntity);

            }
        }
    }
    private void mapAttributeValueToRiskType(RiskTypeAttributeDto dto, RiskTypeAttributeEntity entity) {
        AttributeDto attributeDto = dto.getAttribute();
        if (!(attributeDto.getDisplayType() == DisplayType.TEXTBOX) && dto.getRiskTypeAttributeValues() != null)
        {
            for (var riskTypeAttributeValueDto : dto.getRiskTypeAttributeValues()) {
                RiskTypeAttributeValueEntity riskTypeAttributeValueEntity = RiskTypeAttributeValueEntity
                        .builder()
                        .attributeValueId(riskTypeAttributeValueDto.getAttributeValue().getId())
                        .riskTypesAttributeId(entity.getId())
                        .build();
                riskTypeAttributeValueRepository.save(riskTypeAttributeValueEntity);
            }
        }
    }

    @Override
    @Transactional
    public RiskTypeDto updateRiskType(RiskTypeDto dto) {

        RiskTypeEntity existingEntity = riskTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        if (!Origin.isValidObjectForOrigin(dto.getOrigin(), dto.getObject()))
        {
            throw new AppException(ErrorCode.INVALID_OBJECT_FOR_ORIGIN);
        }
        existingEntity = modelMapper.map(dto, RiskTypeEntity.class);

        riskTypeMapRepository.deleteByRiskTypeId(existingEntity.getId());

        List<RiskTypeAttributeEntity> existingAttributes = riskTypeAttributeRepository.findByRiskTypeId(existingEntity.getId());
        for ( var riskTypeAttribute : existingAttributes)
        {
            riskTypeAttributeValueRepository.deleteByRiskTypesAttributeId(riskTypeAttribute.getId());
        }
        riskTypeAttributeRepository.deleteByRiskTypeId(existingEntity.getId());


        existingEntity = riskTypeRepository.save(existingEntity);
        // map phần system vào cho risk type
        mapSystemToRiskType(existingEntity, dto);
        // map phần attribute vào cho risk type
        mapAttributeToRiskType(dto, existingEntity);

        return riskTypeMapper.maptoRiskTypeDto(existingEntity);
    }

    @Override
    @Transactional
    public void deleteRiskType(Long id) {
        if (!riskTypeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        // trước khi xóa riskType cần xóa các bản ghi trong bảng mapping
//        riskTypeMapRepository.deleteByRiskTypeId(id);
        riskTypeMapRepository.deleteByRiskTypeId(id);

        List<RiskTypeAttributeEntity> existingAttributes = riskTypeAttributeRepository.findByRiskTypeId(id);
        for ( var riskTypeAttribute : existingAttributes)
        {
            riskTypeAttributeValueRepository.deleteByRiskTypesAttributeId(riskTypeAttribute.getId());
        }
        riskTypeAttributeRepository.deleteByRiskTypeId(id);

        riskTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<RiskTypeDto>> search(String code, Long systemId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<RiskTypeEntity> keywordSpec = BaseSpecification.hasKeyword(code);
        Specification<RiskTypeEntity> isSystemSpec = BaseSpecification.hasRelatedId
                        (
                                systemId,
                                RiskTypeMapEntity.class,
                                "riskTypeId",
                                "systemId"
                        );
        Specification<RiskTypeEntity> isActiveSpec = BaseSpecification.hasFieldBoolean("isActive", isActive);

        Specification<RiskTypeEntity> spec = Specification.where(keywordSpec)
                                                                        .and(isSystemSpec)
                                                                        .and(isActiveSpec);

        Page<RiskTypeEntity> page = riskTypeRepository.findAll(spec, pageable);
        Page<RiskTypeDto> riskTypeDtoPage = page.map(riskTypeMapper::maptoRiskTypeDto);

        return ApiResponseUtil.createPageResponse(riskTypeDtoPage);
    }
}
