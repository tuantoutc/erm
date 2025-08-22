package com.example.erm_demo.adapter.in.rest.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, message = "Code must be at least 3 characters long")
    String code;
    @Size(min = 3 , message = "Name must be at least 3 characters long")
    String name;
    String description; // Mô tả
    String note;
    List<SystemDto> systemDtos; // ID của hệ thống mà loại nguyên nhân này thuộc về

}
