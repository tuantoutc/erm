package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeAttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeAttributeValueEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskTypeMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.RiskTypeMapper;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeAttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeAttributeValueRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskTypeRepository;
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
        RiskTypeEntity entity = modelMapper.map(dto, RiskTypeEntity.class);

// kiem tra tinh hop le cua origin va object
        if (!Origin.isValidObjectForOrigin(dto.getOrigin(), dto.getObject()))
        {
            throw new AppException(ErrorCode.INVALID_OBJECT_FOR_ORIGIN);
        }
        entity = riskTypeRepository.save(entity);

        mapSystemToRiskType(entity, dto);
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
        if(dto.getRiskTypeAttributes() != null) {
            for(var riskTypeAttributeDto : dto.getRiskTypeAttributes()) {
                RiskTypeAttributeEntity riskTypeAttributeEntity = RiskTypeAttributeEntity.builder()
                        .attributeId(riskTypeAttributeDto.getAttribute().getId())
                        .riskTypeId(entity.getId())
                        .attributeGroupId(riskTypeAttributeDto.getAttributeGroup().getId())
                        .build();
                // logic to map attributes to risk type
                riskTypeAttributeRepository.save(riskTypeAttributeEntity);
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

        RiskTypeEntity entity = riskTypeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        entity = modelMapper.map(dto, RiskTypeEntity.class);

//        riskTypeMapRepository.deleteByRiskTypeId(entity.getId());



        entity = riskTypeRepository.save(entity);
        mapSystemToRiskType(entity, dto);

        return riskTypeMapper.maptoRiskTypeDto(entity);
    }

    @Override
    @Transactional
    public void deleteRiskType(Long id) {
        if (!riskTypeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        // trước khi xóa riskType cần xóa các bản ghi trong bảng mapping
//        riskTypeMapRepository.deleteByRiskTypeId(id);
        riskTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<RiskTypeDto>> search(String code, Long systemId, Boolean isActive, PageRequest pageRequest) {
//        Sort sortBy = Sort.by("id").ascending();
//        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
//
//        Specification<RiskTypeEntity> spec ;
//
//        Page<RiskTypeEntity> page = riskTypeRepository.findAll(spec, pageable);
//
//        Page<RiskTypeDto> pageDto = page.map(riskTypeMapper::maptoRiskTypeDto);
//        return ApiResponseUtil.createPageResponse(pageDto);
        return null;
    }
}
