package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.SourceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PreventiveMeasureDto {
    Long id;
    String code;
    @NotNull
    String name;
    @NotNull
    String description;
    Boolean isActive;

}
