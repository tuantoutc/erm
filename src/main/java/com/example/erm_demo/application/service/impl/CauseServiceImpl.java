package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseMapper;
import com.example.erm_demo.adapter.out.persistence.mapper.ExternalServiceValue;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.specification.CauseSpecification;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final CauseMapRepository causeMapRepository;
    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseMapper causeMapper;
    private final ExternalServiceValue externalServiceValue;

    @Override
    @Transactional(readOnly = true)
    public CauseDto getCauseById(Long id) {
        CauseEntity causeEntity = causeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        CauseDto causeDto =  causeMapper.toDto(causeEntity);
        causeDto = addSystemToCauseDto(causeDto, causeEntity);
        return causeDto;
    }
    private CauseDto addSystemToCauseDto(CauseDto causeDto, CauseEntity causeEntity){
        List<CauseMapEntity> causeMapEntities = causeMapRepository.findByCauseId(causeEntity.getId());
        Set<Long> systemIds = causeMapEntities.stream()
                .map(CauseMapEntity::getSystemId)
                .collect(Collectors.toSet());
        if (Objects.isNull(systemIds) || systemIds.isEmpty()){
            return causeDto;
        }
        Map<Long, SystemDto> system = externalServiceValue.getSystemIds(systemIds);
        if (Objects.isNull(system) || system.isEmpty()){
            return causeDto;
        }
        List<SystemDto> systemDtos = new ArrayList<>(system.values());
        causeDto.setSystem(systemDtos);
        return causeDto;
    }

    @Override
    @Transactional
    public CauseDto createCause(CauseDto dto) {
        if(!causeCategoryRepository.existsById(dto.getCauseCategory().getId())){
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        CauseEntity causeEntity = causeMapper.toEntity(dto);
        causeEntity = causeRepository.save(causeEntity);

        mapSystemToCause(causeEntity, dto);

        return causeMapper.toDto(causeEntity);
    }

    private void mapSystemToCause(CauseEntity entity, CauseDto dto) {
        if(Objects.isNull(dto.getSystem()) || dto.getSystem().isEmpty()){
            return;
        }
        List<CauseMapEntity> causeMapEntities = dto.getSystem().stream()
                .map(systemDto ->
                    CauseMapEntity.builder()
                            .systemId(systemDto.getId())
                            .causeId(entity.getId())
                            .build())
                .collect(Collectors.toList());
        causeMapRepository.saveAll(causeMapEntities);

    }

    @Override
    @Transactional
    public CauseDto updateCause(CauseDto dto) {
        CauseEntity existingEntity = causeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        causeMapRepository.deleteByCauseId(existingEntity.getId());

        if (!dto.getCauseCategory().getId().equals(existingEntity.getCauseCategoryId())) {
            CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(dto.getCauseCategory().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            existingEntity.setCauseCategoryId(causeCategoryEntity.getId());
        }
        existingEntity = causeMapper.toEntity(dto);
        causeRepository.save(existingEntity);
        mapSystemToCause(existingEntity, dto);
        return causeMapper.toDto(existingEntity);
    }

    @Override
    @Transactional
    public void deleteCause(Long id) {
        if (!causeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeMapRepository.deleteByCauseId(id);
        causeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<CauseDto>> searchCauses(String code, Long causeCategoryId, Origin origin, Boolean isActive, PageRequest pageRequest) {

        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<CauseEntity> spec = CauseSpecification.advancedSearchCriteria(code, causeCategoryId, origin, isActive);

        Page<CauseEntity> page = causeRepository.findAll(spec, pageable);
//        Page<CauseDto> causeDtoPage = page.map(causeMap::mapToCauseDto);

        return null;
    }


}
