package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseMapRepository;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class CauseMapper {

    private final ModelMapper modelMapper;
    private final CauseCategoryMapper causeCategoryMapper;
    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseMapRepository causeMapRepository;


    public CauseDto mapToCauseDto(CauseEntity entity) {
        CauseDto causeDto = modelMapper.map(entity, CauseDto.class);
        CauseCategoryEntity causeCategoryEntity = causeCategoryRepository.findById(entity.getCauseCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        causeDto.setCauseCategoryDto(causeCategoryMapper.mapToDto(causeCategoryEntity));

        List<CauseMapEntity> causeMapEntities = causeMapRepository.findByCauseId(entity.getId());
        if (causeMapEntities != null && !causeMapEntities.isEmpty()) {
            List<SystemDto> systemDtos = causeMapEntities.stream().map(item -> {
                return SystemDto.builder()
                        .id(item.getSystemId())
                        .build();
            }).toList();
            causeDto.setSystemDtos(systemDtos);
        }
        return causeDto;
    }

//    private void setCauseMapSystemFromDto(CauseEntity entity, CauseDto dto) {
//        CauseCategoryEntity category = causeCategoryRepository.findById(dto.getCauseCategoryDto().getId())
//                .orElseThrow(()-> new AppException(ErrorCode.ENTITY_NOT_FOUND));
//        if (category != null) entity.setCauseCategoryEntity(category);
//
//        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
//            List<CauseMapEntity> causeMapEntities = dto.getSystemDtos().stream().map(systemDto -> {
//                CauseMapEntity causeMapEntity = CauseMapEntity.builder()
//                        .systemId(systemDto.getId())
//                        .causeEntity(entity)
//                        .build();
//                return causeMapEntity;
//            }).toList();
//            entity.setCauseMapEntities(causeMapEntities);
//        } else {
//            entity.setCauseMapEntities(null);
//        }
//    }

}

