package com.example.erm_demo.adapter.in.rest.dto;

import com.example.erm_demo.domain.enums.ActionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackingActionDto {

    Long id;
    ActionType type;
    PreventiveMeasureDto preventiveMeasure;
    DepartmentDto department;
    String content;
    Date planDate;

}
