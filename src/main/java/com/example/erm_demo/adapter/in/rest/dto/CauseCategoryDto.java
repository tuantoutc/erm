package com.example.erm_demo.adapter.in.rest.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseCategoryDto {
    Long id;
    String code;
    String name;
    String description; // Mô tả
    String note;
    Long systemId; // ID của hệ thống mà loại nguyên nhân này thuộc về

}
