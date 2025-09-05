package com.example.erm_demo.adapter.in.rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiskTypeAttributeValueDto {

    Long id;
    AttributeValueDto attributeValue;
    String textValue;

}
