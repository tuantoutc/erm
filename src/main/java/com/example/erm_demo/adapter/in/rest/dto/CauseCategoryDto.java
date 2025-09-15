package com.example.erm_demo.adapter.in.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CauseCategoryDto {
    Long id;
    @NotNull
    String code;
    @NotNull
    String name;
    String description; // Mô tả
    String note;
    List<SystemDto> system ;
}
