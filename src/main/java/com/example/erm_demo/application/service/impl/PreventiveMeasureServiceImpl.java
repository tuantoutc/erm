package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PreventiveMeasureDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.out.persistence.entity.PreventiveMeasureEntity;
import com.example.erm_demo.adapter.out.persistence.repository.PreventiveMeasureRepository;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.PreventiveMeasureService;
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
public class PreventiveMeasureServiceImpl implements PreventiveMeasureService {

    private final PreventiveMeasureRepository preventiveMeasureRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PreventiveMeasureDto createPreventiveMeasure(PreventiveMeasureDto dto) {
        PreventiveMeasureEntity entity = modelMapper.map(dto, PreventiveMeasureEntity.class);
        entity = preventiveMeasureRepository.save(entity);
        return modelMapper.map(entity, PreventiveMeasureDto.class);
    }

    @Override
    @Transactional
    public PreventiveMeasureDto updatePreventiveMeasure(PreventiveMeasureDto dto) {
        PreventiveMeasureEntity existingEntity = preventiveMeasureRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        existingEntity = modelMapper.map(dto, PreventiveMeasureEntity.class);

        return modelMapper.map(preventiveMeasureRepository.save(existingEntity), PreventiveMeasureDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public PreventiveMeasureDto getPreventiveMeasureById(Long id) {
        return preventiveMeasureRepository.findById(id)
                .map(value-> modelMapper.map(value, PreventiveMeasureDto.class))
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deletePreventiveMeasure(Long id) {
        if (!preventiveMeasureRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        preventiveMeasureRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<PreventiveMeasureDto>> search(String code, Boolean isActive, PageRequest pageRequest) {
        Sort customSort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), customSort);

        Specification<PreventiveMeasureEntity> keywordSpec = BaseSpecification.hasKeyword(code);
        Specification<PreventiveMeasureEntity> isActiveSpec = BaseSpecification.hasFieldBoolean("isActive", isActive);
        Specification<PreventiveMeasureEntity> specification = Specification.where(keywordSpec).and(isActiveSpec);

        Page<PreventiveMeasureEntity> page = preventiveMeasureRepository.findAll(specification, pageable);

        Page<PreventiveMeasureDto> pageDto = page.map(entity -> modelMapper.map(entity, PreventiveMeasureDto.class));
        return ApiResponseUtil.createPageResponse(pageDto);

    }
}
