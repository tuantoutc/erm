package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.SourceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeGroupDto {

    Long id;

    String code;
    @NonNull
    String name;
    SourceType sourceType;
    String description;
    Boolean isActive;
}
