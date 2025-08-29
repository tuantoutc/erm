package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class CauseCategoryServiceImpl implements CauseCategoryService {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;
    private final ModelMapper modelMapper;
    private final CauseCategoryMapRepository causeCategoryMapRepository;
    private final CauseRepository causeRepository;


    @Override
    @Transactional
    public CauseCategoryDto createCauseCategory(CauseCategoryDto dto) {
        if (dto.getId() == null) {
            CauseCategoryEntity causeCategoryEntity = modelMapper.map(dto, CauseCategoryEntity.class);
            causeCategoryEntity = causeCategoryRepository.save(causeCategoryEntity);

            mapSystemToCauseCategory(causeCategoryEntity, dto);

            return causeCategoryMapper.mapToDto(causeCategoryEntity);
        }
        else
            throw new AppException(ErrorCode.ID_MUST_BE_NULL);
    }


    private void mapSystemToCauseCategory(CauseCategoryEntity entity, CauseCategoryDto dto){
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            for (var systemDto : dto.getSystemDtos()) {
                CauseCategoryMapEntity causeCategoryMapEntity = CauseCategoryMapEntity
                        .builder()
                        .systemId(systemDto.getId())
                        .causeCategoryId(entity.getId())
                        .build();
                causeCategoryMapRepository.save(causeCategoryMapEntity);
            }
        }
    }


    @Override
    @Transactional
    public CauseCategoryDto updateCauseCategory(CauseCategoryDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        CauseCategoryEntity existsEntity = causeCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        existsEntity = modelMapper.map(dto, CauseCategoryEntity.class);
        causeCategoryMapRepository.deleteByCauseCategoryId(existsEntity.getId());
        mapSystemToCauseCategory(existsEntity, dto);
        return causeCategoryMapper.mapToDto(causeCategoryRepository.save(existsEntity));
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
    @Transactional
    public void deleteCauseCategory(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!causeCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeRepository.deleteByCauseCategoryId(id);
        causeCategoryMapRepository.deleteByCauseCategoryId(id);
        causeCategoryRepository.deleteById(id);

    }

    @Override
    public Page<CauseCategoryDto> getAllCauseCategories(PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
        Page<CauseCategoryEntity> causeCategories = causeCategoryRepository.findAll(pageable);
        if (!causeCategories.isEmpty()) {
            return causeCategories.map(causeCategoryMapper::mapToDto);
        } else
            return null;

    }

    @Override
    public Page<CauseCategoryDto> searchCauseCategories(String code, Long systemId, PageRequest pageRequest) {

        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
        Page<CauseCategoryEntity> causeCategoryPage = causeCategoryRepository.findAllBy(code, systemId, pageable);
        // Always return a Page, never null
        return causeCategoryPage.map(causeCategoryMapper::mapToDto);
    }

}
