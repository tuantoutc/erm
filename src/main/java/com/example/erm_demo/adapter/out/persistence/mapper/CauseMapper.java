package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.Cause;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.entity.CauseMap;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
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


    public Cause maptoCause(CauseDto dto) {
        Cause entity = modelMapper.map(dto, Cause.class);

        setCauseMapSystemFromDto(entity, dto);
        return entity;
    }

    public Cause mapUpdateCause(Cause entity, CauseDto dto) {
        entity.getCauseMaps().clear();
        entity = modelMapper.map(dto, Cause.class);

        setCauseMapSystemFromDto(entity, dto);
        return entity;
    }

    public CauseDto mapToCauseDto(Cause entity) {
        CauseDto causeDto = modelMapper.map(entity, CauseDto.class);
        if (entity.getCauseMaps() != null) {
            List<SystemDto> systemDtos = entity.getCauseMaps().stream().map(causeMap -> {
                SystemDto systemDto = SystemDto.builder()
                        .id(causeMap.getSystemId())
                        .build();
                return systemDto;
            }).toList();
            causeDto.setSystemDtos(systemDtos);
        }
        CauseCategoryDto causeCategoryDto = causeCategoryMapper.mapToDto(entity.getCauseCategory());
        causeDto.setCauseCategoryDto(causeCategoryDto);
        return causeDto;
    }

    private void setCauseMapSystemFromDto(Cause entity, CauseDto dto) {
        CauseCategory category = causeCategoryRepository.findById(dto.getCauseCategoryDto().getId())
                .orElseThrow(()-> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        if (category != null) entity.setCauseCategory(category);

        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            List<CauseMap> causeMaps = dto.getSystemDtos().stream().map(systemDto -> {
                CauseMap causeMap = CauseMap.builder()
                        .systemId(systemDto.getId())
                        .cause(entity)
                        .build();
                return causeMap;
            }).toList();
            entity.setCauseMaps(causeMaps);
        } else {
            entity.setCauseMaps(null);
        }
    }

}

