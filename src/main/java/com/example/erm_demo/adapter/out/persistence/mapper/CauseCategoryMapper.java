package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryEntity;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CauseCategoryMapper {

    private final ModelMapper modelMapper;
    private final CauseCategoryMapRepository causeCategoryMapRepository;

    public CauseCategoryDto mapToDto(CauseCategoryEntity causeCategoryEntity) {
        CauseCategoryDto dto = modelMapper.map(causeCategoryEntity, CauseCategoryDto.class);
        List<CauseCategoryMapEntity> listSystem = causeCategoryMapRepository.findByCauseCategoryId(causeCategoryEntity.getId());
        if (listSystem != null && !listSystem.isEmpty()) {
            List<SystemDto> systemDtos = listSystem.stream().map(item -> {
                return SystemDto.builder()
                        .id(item.getSystemId())
                        .build();
            }).toList();
            dto.setSystemDtos(systemDtos);
        }

        return dto;
    }

}
