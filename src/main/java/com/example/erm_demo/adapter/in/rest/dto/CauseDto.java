package com.example.erm_demo.adapter.in.rest.dto;


import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseDto{

    Long id;
    @Size(min = 3, message = "Code must be at least 3 characters long")
    String code;
    @Size(min = 3, message = "Code must be at least 3 characters long")
    String name;
    String type; //  Sự cố, rủi ro
    String origin;
    String description; // Mô tả
    String note;
    Boolean isActive;
    CauseCategoryDto causeCategoryDto;
    List<SystemDto> systemDtos;
}
