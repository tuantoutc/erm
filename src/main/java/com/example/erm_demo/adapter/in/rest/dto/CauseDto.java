package com.example.erm_demo.adapter.in.rest.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CauseDto{

    Long id;
    String code;
    String name;
    String type; //  Sự cố, rủi ro
    String origin;
    String description; // Mô tả
    String note;
    Boolean isActive;
    Long systemId; // ID của hệ thống mà nguyên nhân này thuộc về
    CauseCategoryDto causeCategoryDto;
}
