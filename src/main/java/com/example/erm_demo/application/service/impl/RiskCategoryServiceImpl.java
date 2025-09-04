package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.RiskCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.specification.RiskCategorySpecification;
import com.example.erm_demo.application.service.RiskCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
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
public class RiskCategoryServiceImpl implements RiskCategoryService {

    private final RiskCategoryRepository riskCategoryRepository;

    private final RiskCategoryMapper riskCategoryMapper;

    private final ModelMapper modelMapper;

    private final RiskCategoryMapRepository riskCategoryMapRepository;


    @Override
    @Transactional(readOnly = true)
    public RiskCategoryDto getRiskCategoryById(Long id) {
        return riskCategoryRepository.findById(id)
                .map(riskCategoryMapper::maptoRiskCategoryDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public RiskCategoryDto createRiskCategory(RiskCategoryDto dto) {
        RiskCategoryEntity entity = modelMapper.map(dto, RiskCategoryEntity.class);
        if (dto.getParent() != null && dto.getParent().getId() != null) {
            RiskCategoryEntity parent = riskCategoryRepository.findById(dto.getParent().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            entity.setParentId(parent.getId());
        } else entity.setParentId(null);
        entity = riskCategoryRepository.save(entity);
        mapSystemToRiskCategory(entity, dto);

        return riskCategoryMapper.maptoRiskCategoryDto(entity);
    }

    private void mapSystemToRiskCategory(RiskCategoryEntity entity, RiskCategoryDto dto) {
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            for (var systemDto : dto.getSystemDtos()) {
                RiskCategoryMapEntity riskCategoryMapEntity = RiskCategoryMapEntity
                        .builder()
                        .systemId(systemDto.getId())
                        .riskCategoryId(entity.getId())
                        .build();
                riskCategoryMapRepository.save(riskCategoryMapEntity);
            }
        }
    }

    @Override
    @Transactional
    public RiskCategoryDto updateRiskCategory(RiskCategoryDto dto) {

        RiskCategoryEntity entity = riskCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        entity = modelMapper.map(dto, RiskCategoryEntity.class);

        riskCategoryMapRepository.deleteByRiskCategoryId(entity.getId());

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            RiskCategoryEntity parent = riskCategoryRepository.findById(dto.getParent().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            entity.setParentId(parent.getId());
        } else entity.setParentId(null);

        entity = riskCategoryRepository.save(entity);
        mapSystemToRiskCategory(entity, dto);

        return riskCategoryMapper.maptoRiskCategoryDto(entity);
    }

    @Override
    @Transactional
    public void deleteRiskCategory(Long id) {
        if (!riskCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        // trước khi xóa riskCategory cần xóa các bản ghi trong bảng mapping
        riskCategoryMapRepository.deleteByRiskCategoryId(id);
        riskCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<RiskCategoryDto>> searchRiskCategories(String code, Long systemId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<RiskCategoryEntity> spec = RiskCategorySpecification.advancedSearchCriteria(code, systemId, isActive);

        Page<RiskCategoryEntity> page = riskCategoryRepository.findAll(spec, pageable);

        Page<RiskCategoryDto> pageDto = page.map(riskCategoryMapper::maptoRiskCategoryDto);
        return ApiResponseUtil.createPageResponse(pageDto);
    }
}
