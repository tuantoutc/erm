package com.example.erm_demo.adapter.in.rest.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackingCauseMapDto {

    Long id;
    DepartmentDto department;

    Long positionId;

    EmployeeDto employee;

    Long productId;

    Long parnerType;

    Long groupParnerId;

    Long parnerId;

    List<DicDto> dics;

}

