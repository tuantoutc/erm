package com.example.erm_demo.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleActionDto {

    Long id;
    String code;
    @NotNull
    String name;
    String note;
    Boolean isActive;
    @NotNull
    RiskTypeDto riskType;
    @NotNull
    CauseCategoryDto causeCategory;
    List<SampleActionMapDto> sampleActionMaps;

}
