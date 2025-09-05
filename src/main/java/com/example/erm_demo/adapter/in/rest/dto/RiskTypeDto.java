package com.example.erm_demo.adapter.in.rest.dto;

import com.example.erm_demo.domain.enums.ObjectType;
import com.example.erm_demo.domain.enums.Origin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiskTypeDto {

    Long id;
    String code;
    @NotNull
    String name;
    @NotNull
    Origin origin; // Enum thay vì String
    String note;
    Boolean isActive;
    @NotNull
    ObjectType object;

    List<SystemDto> systemDtos;

    List<RiskTypeAttributeDto> riskTypeAttributes;

}
