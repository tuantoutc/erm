package com.example.erm_demo.adapter.in.rest.dto;

import com.example.erm_demo.domain.enums.PriorityLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskDto {

    Long id;
    String code;
    @NotNull
    String name;
    SystemDto system;
    RiskTypeDto riskType;
    RiskCategoryDto riskCategory;
    EmployeeDto reporter;
    Date recordedTime;
    PriorityLevel priorityLevel;
    String description;
    String expectedConsequence;
    Long level;
    Long point;

    List<RiskAttributeLineDto> riskAttributeLines;

    List<RelatedRiskDto> relatedRisks;

    List<TagDto>  tags;

    List<RiskCauseLineDto> riskCauseLines;


}
