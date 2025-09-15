package com.example.erm_demo.application.service.impl;


import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategoryMapEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ValueCauseCategory {
    Set<Long> causeCategoryIds;
    List<CauseCategoryMapEntity> causeCategoryMapEntities;
    Set<Long> systemIds;
    Map<Long, SystemDto> systems ;
    Map<Long, List<CauseCategoryMapEntity>> causeCategoryMapByCauseCategoryId;
    
}
