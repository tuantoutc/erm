package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.ObjectApplicableType;
import com.example.erm_demo.domain.enums.State;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackingCauseDto {

    Long id;
    CauseCategoryDto causeCategory;
    CauseDto cause;
    Long count;
    ObjectApplicableType objectApplicableType;
    State state;
    List<TrackingCauseMapDto> trackingCauseMaps;
}
