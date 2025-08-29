package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.SourceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeDto {
    Long id;
    String code;
    @NotNull
    String name;
    String displayType;
    String dataType;
    SourceType sourceType;
    String description;
    Boolean isActive;
    AttributeGroupDto attributeGroup;



}
