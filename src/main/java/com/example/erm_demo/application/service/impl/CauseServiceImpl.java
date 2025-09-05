package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.adapter.out.persistence.specification.CauseSpecification;
import com.example.erm_demo.application.service.CauseService;
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
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final CauseMapRepository causeMapRepository;
    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseMapper causeMapper;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public CauseDto getCauseById(Long id) {
        return causeRepository.findById(id)
                .map(causeMapper::mapToCauseDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

    }

    @Override
    public CauseDto createCause(CauseDto dto) {
        CauseEntity causeEntity = modelMapper.map(dto, CauseEntity.class);

        CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(dto.getCauseCategoryDto().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        causeEntity.setCauseCategoryId(causeCategoryEntity.getId());

        causeEntity = causeRepository.save(causeEntity);

        mapSystemToCause(causeEntity, dto);

        return causeMapper.mapToCauseDto(causeEntity);
    }

    private void mapSystemToCause(CauseEntity entity, CauseDto dto) {
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            for (var systemDto : dto.getSystemDtos()) {
                CauseMapEntity causeMapEntity = CauseMapEntity
                        .builder()
                        .systemId(systemDto.getId())
                        .causeId(entity.getId())
                        .build();
                causeMapRepository.save(causeMapEntity);
            }
        }
    }

    @Override
    @Transactional
    public CauseDto updateCause(CauseDto dto) {
        CauseEntity existingEntity = causeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        causeMapRepository.deleteByCauseId(existingEntity.getId());

        existingEntity = modelMapper.map(dto, CauseEntity.class);
        if (!dto.getCauseCategoryDto().getId().equals(existingEntity.getCauseCategoryId())) {
            CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(dto.getCauseCategoryDto().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            existingEntity.setCauseCategoryId(causeCategoryEntity.getId());
        }
        existingEntity = causeRepository.save(existingEntity);

        mapSystemToCause(existingEntity, dto);

        return causeMapper.mapToCauseDto(existingEntity);

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
        Page<CauseDto> causeDtoPage = page.map(causeMapper::mapToCauseDto);

        return ApiResponseUtil.createPageResponse(causeDtoPage);
    }


}
