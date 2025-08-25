package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CauseCategoryServiceImpl implements CauseCategoryService {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;


    @Override
    public CauseCategoryDto createCauseCategory(CauseCategoryDto dto) {
        if (dto.getId() == null) {
            CauseCategory causeCategory = causeCategoryMapper.mapToEntity(dto);
            return causeCategoryMapper.mapToDto(causeCategoryRepository.save(causeCategory));
        }
        return null;
    }

    @Override
    public CauseCategoryDto updateCauseCategory(CauseCategoryDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        return causeCategoryRepository.findById(dto.getId())
                .map(category -> causeCategoryMapper.mapUpdate(category, dto))
                .map(causeCategoryRepository::save)
                .map(causeCategoryMapper::mapToDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    public CauseCategoryDto getCauseCategoryById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        return causeCategoryRepository.findById(id)
                .map(causeCategoryMapper::mapToDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    public void deleteCauseCategory(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!causeCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeCategoryRepository.deleteById(id);

    }

    @Override
    public Page<CauseCategoryDto> getAllCauseCategories( PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
        Page<CauseCategory> causeCategories = causeCategoryRepository.findAll(pageable);
        if (!causeCategories.isEmpty()) {
            return causeCategories.map(causeCategoryMapper::mapToDto);
        }
        else
            return null;

    }

    @Override
    public Page<CauseCategoryDto> searchCauseCategories(String code, Long systemId, PageRequest pageRequest) {

        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Page<CauseCategory> causeCategoryPage = causeCategoryRepository.findAllBy(code, systemId, pageable);

        // Always return a Page, never null
        return causeCategoryPage.map(causeCategoryMapper::mapToDto);
    }

}
