package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.specification.CauseCategorySpecification;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
            CauseCategoryEntity causeCategoryEntity = modelMapper.map(dto, CauseCategoryEntity.class);
            causeCategoryEntity = causeCategoryRepository.save(causeCategoryEntity);

            mapSystemToCauseCategory(causeCategoryEntity, dto);

            return causeCategoryMapper.mapToDto(causeCategoryEntity);

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
        CauseCategoryEntity existsEntity = causeCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        existsEntity = modelMapper.map(dto, CauseCategoryEntity.class);
        causeCategoryMapRepository.deleteByCauseCategoryId(existsEntity.getId());
        mapSystemToCauseCategory(existsEntity, dto);
        return causeCategoryMapper.mapToDto(causeCategoryRepository.save(existsEntity));
    }

    @Override
    public CauseCategoryDto getCauseCategoryById(Long id) {
        return causeCategoryRepository.findById(id)
                .map(causeCategoryMapper::mapToDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteCauseCategory(Long id) {
        if (!causeCategoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeRepository.deleteByCauseCategoryId(id);
        causeCategoryMapRepository.deleteByCauseCategoryId(id);
        causeCategoryRepository.deleteById(id);

    }


    @Override
    @Transactional
    public ApiResponse<PageResponseDto<CauseCategoryDto>> searchCauseCategories(String code, Long systemId, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        // Sử dụng Specification thay vì query method
//        Specification<CauseCategoryEntity> spec = CauseCategorySpecification.searchCriteria(code, systemId);
//        Page<CauseCategoryEntity> causeCategoryPage = causeCategoryRepository.findAll(spec, pageable);

        Specification<CauseCategoryEntity> keywordSpec = CauseCategorySpecification.hasKeyword(code);
        Specification<CauseCategoryEntity> systemSpec = CauseCategorySpecification.hasSystemId(systemId);
        Specification<CauseCategoryEntity> finalSpec = Specification.where(keywordSpec).and(systemSpec);

        Page<CauseCategoryEntity> causeCategoryPage = causeCategoryRepository.findAll(finalSpec, pageable);


        Page<CauseCategoryDto> causeCategoryDtoPage = causeCategoryPage.map(causeCategoryMapper::mapToDto);

        return ApiResponseUtil.createPageResponse(causeCategoryDtoPage);
    }

    // Method mới để demo các cách sử dụng Specification khác nhau
//    public ApiResponse<PageResponseDto<CauseCategoryDto>> searchWithDifferentSpecifications(String code, Long systemId, PageRequest pageRequest) {
//        Sort sortBy = Sort.by("id").ascending();
//        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
//
//        // Cách 1: Sử dụng Specification tổng hợp
//        Specification<CauseCategoryEntity> combinedSpec = CauseCategorySpecification.searchCriteria(code, systemId);
//
//        // Cách 2: Kết hợp các Specification riêng lẻ
//        Specification<CauseCategoryEntity> keywordSpec = CauseCategorySpecification.hasKeyword(code);
//        Specification<CauseCategoryEntity> systemSpec = CauseCategorySpecification.hasSystemId(systemId);
//        Specification<CauseCategoryEntity> finalSpec = Specification.where(keywordSpec).and(systemSpec);
//
//        // Cách 3: Sử dụng JOIN thay vì Subquery
//        // Specification<CauseCategoryEntity> joinSpec = CauseCategorySpecification.hasSystemIdWithJoin(systemId);
//
//        Page<CauseCategoryEntity> causeCategoryPage = causeCategoryRepository.findAll(finalSpec, pageable);
//        Page<CauseCategoryDto> causeCategoryDtoPage = causeCategoryPage.map(causeCategoryMapper::mapToDto);
//
//        return ApiResponseUtil.createPageResponse(causeCategoryDtoPage);
//    }

}
