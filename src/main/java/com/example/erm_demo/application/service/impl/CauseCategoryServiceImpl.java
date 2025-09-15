package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.mapper.ExternalServiceValue;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.specification.CauseCategorySpecification;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CauseCategoryServiceImpl implements CauseCategoryService {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;
    private final CauseCategoryMapRepository causeCategoryMapRepository;
    private final CauseRepository causeRepository;
    private final ExternalServiceValue externalServiceValue;

    @Override
    @Transactional
    public CauseCategoryDto createCauseCategory(CauseCategoryDto dto) {
        CauseCategoryEntity entity = causeCategoryRepository.save(causeCategoryMapper.toEntity(dto));
        mapSystemToCauseCategory(entity, dto);
        return causeCategoryMapper.toDto(entity);
    }

    private void mapSystemToCauseCategory(CauseCategoryEntity entity, CauseCategoryDto dto){
        if (dto.getSystem() == null || dto.getSystem().isEmpty()) {
            return;
        }
        List<CauseCategoryMapEntity> mappings = dto.getSystem().stream()
                .map(systemDto -> CauseCategoryMapEntity.builder()
                        .systemId(systemDto.getId())
                        .causeCategoryId(entity.getId())
                        .build())
                .collect(Collectors.toList());
        causeCategoryMapRepository.saveAll(mappings);
    }

    @Override
    @Transactional
    public CauseCategoryDto updateCauseCategory(CauseCategoryDto dto) {
        causeCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        CauseCategoryEntity entity = causeCategoryRepository.save(causeCategoryMapper.toEntity(dto));
        causeCategoryMapRepository.deleteByCauseCategoryId(entity.getId());
        mapSystemToCauseCategory(entity, dto);
        return causeCategoryMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CauseCategoryDto getCauseCategoryById(Long id) {
        CauseCategoryEntity entity = causeCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        CauseCategoryDto dto = causeCategoryMapper.toDto(entity);
        addSystemDtoToCauseCategoryDto(dto);
        return dto;
    }

    void addSystemDtoToCauseCategoryDto(CauseCategoryDto dto) {
        List<CauseCategoryMapEntity> entities = causeCategoryMapRepository.findByCauseCategoryId(dto.getId());
        if(Objects.isNull(entities) || entities.isEmpty()){
            return;
        }
        Set<Long> systemIds = entities.stream().map(CauseCategoryMapEntity::getSystemId).collect(Collectors.toSet());
        Map<Long, SystemDto> systems = externalServiceValue.getSystemIds(systemIds);
        if ( Objects.nonNull(systems) && !systems.isEmpty()) {
            dto.setSystem(new ArrayList<>(systems.values()));
        }
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
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<CauseCategoryDto>> searchCauseCategories(String code, Long systemId, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<CauseCategoryEntity> keywordSpec = CauseCategorySpecification.hasKeyword(code);
        Specification<CauseCategoryEntity> systemSpec = CauseCategorySpecification.hasSystemId(systemId);
        Specification<CauseCategoryEntity> finalSpec = Specification.where(keywordSpec).and(systemSpec);

        Page<CauseCategoryEntity> causeCategoryPage = causeCategoryRepository.findAll(finalSpec, pageable);

        List<CauseCategoryDto> causeCategoryDtos = new ArrayList<>();
        entityConverToDto(causeCategoryPage, causeCategoryDtos);
        // Sửa lỗi UnsupportedOperationException bằng cách tạo Page mới
        Page<CauseCategoryDto> resultPage = new PageImpl<>(causeCategoryDtos, pageable, causeCategoryPage.getTotalPages());
        return ApiResponseUtil.createPageResponse(resultPage);
    }

    private void entityConverToDto(Page<CauseCategoryEntity> causeCategoryPage, List<CauseCategoryDto> causeCategoryDtos){

        Set<Long> causeCategoryIds = causeCategoryPage.getContent().stream()
                .map(CauseCategoryEntity::getId).collect(Collectors.toSet());
        // lay tat ca causecategorymap theo causecategoryidin
        List<CauseCategoryMapEntity> causeCategoryMapEntities = causeCategoryMapRepository.findByCauseCategoryIdIn(causeCategoryIds);
        // lay tat ca systemid trong causecategorymap
        Set<Long> systemIds = causeCategoryMapEntities.stream().map(CauseCategoryMapEntity::getSystemId).collect(Collectors.toSet());
        // lay tat ca system theo systemid
        Map<Long, SystemDto> systems = externalServiceValue.getSystemIds(systemIds);
        // group causecategorymap theo causecategoryid
        Map<Long, List<CauseCategoryMapEntity>> causeCategoryMapByCauseCategoryId = causeCategoryMapEntities.stream()
                .collect(Collectors.groupingBy(CauseCategoryMapEntity::getCauseCategoryId));
        // add system vao dto
        addSystemToCauseCategoryDtos(causeCategoryDtos, causeCategoryPage, causeCategoryMapByCauseCategoryId, systems);

    }
    private void addSystemToCauseCategoryDtos(List<CauseCategoryDto> causeCategoryDtos,
                                              Page<CauseCategoryEntity> causeCategoryPage,
                                              Map<Long, List<CauseCategoryMapEntity>> causeCategoryMapByCauseCategoryId,
                                              Map<Long, SystemDto> systems){
        for (var causeCategoryEntity : causeCategoryPage.getContent()) {
            CauseCategoryDto dto = causeCategoryMapper.toDto(causeCategoryEntity);
            List<CauseCategoryMapEntity> causeCategoryMaps = causeCategoryMapByCauseCategoryId.get(dto.getId());
            if (causeCategoryMaps == null || causeCategoryMaps.isEmpty()) {
                // Nếu không có mapping, vẫn thêm dto nhưng không set system
                dto.setSystem(new ArrayList<>());
                causeCategoryDtos.add(dto);
                continue;
            }
            List<SystemDto> systemDtos = causeCategoryMaps.stream()
                    .map(causeCategoryMap -> {
                        return systems.get(causeCategoryMap.getSystemId());
                    })
                    .collect(Collectors.toList());
            dto.setSystem(systemDtos);
            causeCategoryDtos.add(dto);
        }
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
