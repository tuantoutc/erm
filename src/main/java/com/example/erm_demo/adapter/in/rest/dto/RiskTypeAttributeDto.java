package com.example.erm_demo.adapter.in.rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiskTypeAttributeDto {

    Long id;
    AttributeGroupDto attributeGroup;
    AttributeDto attribute;
    List<RiskTypeAttributeValueDto> riskTypeAttributeValues;


}
