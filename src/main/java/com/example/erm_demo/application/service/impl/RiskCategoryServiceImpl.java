package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.RiskCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.RiskCategoryRepository;
import com.example.erm_demo.application.service.RiskCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RiskCategoryServiceImpl implements RiskCategoryService {

    private final RiskCategoryRepository riskCategoryRepository;

    private final RiskCategoryMapper riskCategoryMapper;


    @Override
    public RiskCategoryDto getRiskCategoryById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!riskCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        RiskCategoryEntity entity = riskCategoryRepository.findById(id).get();
        return riskCategoryMapper.maptoRiskCategoryDto(entity);

    }

    @Override
    @Transactional
    public RiskCategoryDto createRiskCategory(RiskCategoryDto dto) {
        if (dto.getId() != null) {
            throw new AppException(ErrorCode.ID_MUST_BE_NULL);
        }
        RiskCategoryEntity riskCategoryEntity = riskCategoryMapper.maptoRiskCategory(dto);
        if (dto.getParent() != null) {
            RiskCategoryEntity parent = riskCategoryRepository.findById(dto.getParent().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            riskCategoryEntity.setParent(parent);
        }
        return riskCategoryMapper.maptoRiskCategoryDto(riskCategoryRepository.save(riskCategoryEntity));
    }

    @Override
    @Transactional
    public RiskCategoryDto updateRiskCategory(RiskCategoryDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        RiskCategoryEntity entity = riskCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        entity.setParent(null);
        entity = riskCategoryMapper.updateRiskCategory(entity, dto);
        if (dto.getParent() != null) {

            // kiêm tra để tránh entity có parent là chính nó
            if (dto.getParent().getId() != null && dto.getParent().getId().equals(dto.getId())) {
                throw new AppException(ErrorCode.PARENT_CANNOT_BE_SELF);
            }
            RiskCategoryEntity parent = riskCategoryRepository.findById(dto.getParent().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            entity.setParent(parent);
        }
        return riskCategoryMapper.maptoRiskCategoryDto(riskCategoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteRiskCategory(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!riskCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        riskCategoryRepository.deleteById(id);
    }

    @Override
    public Page<RiskCategoryDto> searchByKeyWord(String code, Long systemId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Page<RiskCategoryEntity> riskCategories = riskCategoryRepository.searchBy(code, systemId, isActive, pageable);

        return riskCategories.map(riskCategoryMapper::maptoRiskCategoryDto);
    }

    @Override
    public Page<RiskCategoryDto> getAllRiskCategories(PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Page<RiskCategoryEntity> riskCategories = riskCategoryRepository.findAll(pageable);
        return riskCategories.map(riskCategoryMapper::maptoRiskCategoryDto);
    }
}
