package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import com.example.erm_demo.domain.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CauseServiceImpl implements CauseService {


    private final CauseRepository causeRepository;
    private final CauseMapRepository causeMapRepository;
    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseMapper causeMapper;
    private final ModelMapper modelMapper;



    @Override
    public CauseDto getCauseById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        return causeRepository.findById(id)
                .map(causeMapper::mapToCauseDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

    }

    @Override
    @Transactional
    public CauseDto createCause(CauseDto dto) {
        if (dto.getId() == null) {
            CauseEntity causeEntity = new CauseEntity();

            mapCauseCategoryToCause(causeEntity, dto);

            causeEntity = causeRepository.save(causeEntity);

            mapSystemToCause(causeEntity, dto);

            return causeMapper.mapToCauseDto(causeRepository.save(causeEntity));
        }
        else{
            throw new AppException(ErrorCode.ID_MUST_BE_NULL);
        }
    }
    private void mapSystemToCause(CauseEntity entity, CauseDto dto){
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

    private void mapCauseCategoryToCause(CauseEntity entity, CauseDto dto){
        entity = modelMapper.map(dto, CauseEntity.class);
        CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(dto.getCauseCategoryDto().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        entity.setCauseCategoryId(causeCategoryEntity.getId());
    }

    @Override
    @Transactional
    public CauseDto updateCause(CauseDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        CauseEntity existingEntity = causeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        causeMapRepository.deleteByCauseId(existingEntity.getId());

        mapCauseCategoryToCause(existingEntity, dto);

        existingEntity = causeRepository.save(existingEntity);

        mapSystemToCause(existingEntity, dto);

        return causeMapper.mapToCauseDto(causeRepository.save(existingEntity));

    }

    @Override
    public void deleteCause(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!causeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeMapRepository.deleteByCauseId(id);
        causeRepository.deleteById(id);
    }

    @Override
    public List<CauseDto> getAllCauses() {
        List<CauseEntity> causeEntityList = causeRepository.findAll();
        if (!causeEntityList.isEmpty()) {
            return causeEntityList.stream()
                    .map(causeMapper::mapToCauseDto)
                    .toList();
        }
        return List.of();
    }

    @Override
    public Page<CauseDto> searchByKeyWord(String code, TypeERM type, Origin origin, Boolean isActive, PageRequest pageRequest) {

        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Page<CauseEntity> causes = causeRepository.search(code, type, origin, isActive, pageable);
        return causes.map(causeMapper::mapToCauseDto);

    }


}
