package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMap;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CauseCategoryMapper {

    private final ModelMapper modelMapper;

    public CauseCategory mapToEntity(CauseCategoryDto dto) {
        CauseCategory causeCategory = modelMapper.map(dto, CauseCategory.class);
        setCauseCategoryMapSystemFromDto(causeCategory, dto);
        return causeCategory;

    }

    public CauseCategory mapUpdate(CauseCategory entity, CauseCategoryDto dto) {
        entity.getCauseCategoryMaps().clear();// Clear existing mappings

        entity = modelMapper.map(dto, CauseCategory.class);
        setCauseCategoryMapSystemFromDto(entity, dto);

        return entity;

    }

    public CauseCategoryDto mapToDto(CauseCategory causeCategory) {
        CauseCategoryDto dto = modelMapper.map(causeCategory, CauseCategoryDto.class);
        if (causeCategory.getCauseCategoryMaps() != null) {
            List<SystemDto> systemDtos = causeCategory.getCauseCategoryMaps().stream()
                    .map(causeCategoryMap -> {
                        SystemDto systemDto = new SystemDto();
                        systemDto.setId(causeCategoryMap.getSystemId());
                        return systemDto;
                    })
                    .toList();
            dto.setSystemDtos(systemDtos);
        }
        return dto;
    }

    private void setCauseCategoryMapSystemFromDto(CauseCategory entity, CauseCategoryDto dto) {
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            List<CauseCategoryMap> causeCategoryMaps = dto.getSystemDtos().stream()
                    .map(systemDto -> {
                        CauseCategoryMap causeCategoryMap = CauseCategoryMap.builder()
                                .systemId(systemDto.getId())
                                .causeCategory(entity)
                                .build();
                        return causeCategoryMap;
                    }).toList();
            entity.setCauseCategoryMaps(causeCategoryMaps);
        } else {
            entity.setCauseCategoryMaps(null);
        }
    }


}
