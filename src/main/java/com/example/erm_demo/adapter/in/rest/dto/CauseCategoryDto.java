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
public class CauseCategoryDto {
    Long id;
    @NotNull
    String code;
    @NotNull
    String name;
    String description; // Mô tả
    String note;
    List<SystemDto> systemDtos; // ID của hệ thống mà loại nguyên nhân này thuộc về

}
