package com.example.erm_demo.adapter.in.rest.dto;

import com.example.erm_demo.domain.enums.ActionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleActionMapDto {

    Long id;
    ActionType type;
    PreventiveMeasureDto preventiveMeasure;
    DepartmentDto department;
    String content;

}
