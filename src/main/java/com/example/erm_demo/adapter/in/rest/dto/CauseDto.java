package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseDto {

    Long id;
    @NotNull
    String code;
    @NotNull
    String name;
    @NonNull
    TypeERM type; //  Sự cố, rủi ro
    @NotNull
    Origin origin; // Enum thay vì String
    String description; // Mô tả
    String note;
    Boolean isActive;
    @NotNull
    CauseCategoryDto causeCategoryDto;
    List<SystemDto> systemDtos;
}
