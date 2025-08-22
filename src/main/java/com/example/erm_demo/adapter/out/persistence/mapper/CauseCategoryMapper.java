package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMap;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryMapRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CauseCategoryMapper {
    private final ModelMapper modelMapper;

    private final CauseCategoryMapRepository causeCategoryMapRepository;

    public CauseCategory mapToCauseCategory(CauseCategoryDto dto) {
        CauseCategory causeCategory = modelMapper.map(dto, CauseCategory.class);
//        if(dto.getSystemDtos() != null) {
//            List<CauseCategoryMap> causeCategoryMaps = new ArrayList<>();
//            for (SystemDto systemDto : dto.getSystemDtos()) {
//                CauseCategoryMap causeCategoryMap = new CauseCategoryMap();
//                causeCategoryMap.setSystemId(systemDto.getId());
//                causeCategoryMap.setCauseCategory(causeCategory);
//                causeCategoryMaps.add(causeCategoryMap);
//            }
        if (dto.getSystemDtos() != null && !dto.getSystemDtos().isEmpty()) {
            List<CauseCategoryMap> causeCategoryMaps = dto.getSystemDtos().stream()
                    .map(systemDto -> {
                        CauseCategoryMap causeCategoryMap = new CauseCategoryMap();
                        causeCategoryMap.setSystemId(systemDto.getId());
                        causeCategoryMap.setCauseCategory(causeCategory);
                        return causeCategoryMap;
                    })
                    .toList();
            causeCategory.setCauseCategoryMaps(causeCategoryMaps);
        }
        else {
            causeCategory.setCauseCategoryMaps(null);
        }
        return causeCategory;

    }

    public CauseCategoryDto mapToCauseCategoryDto(CauseCategory causeCategory) {
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

}
