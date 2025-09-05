package com.example.erm_demo.adapter.in.rest.dto;


import com.example.erm_demo.domain.enums.DataType;
import com.example.erm_demo.domain.enums.DisplayType;
import com.example.erm_demo.domain.enums.SourceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    @NotNull
    DisplayType displayType; // Textbox, Selectbox, Radio, Multi-select, Checkbox
    DataType dataType; // Date, String, Int, Time, Float (chỉ dùng khi displayType = "Textbox")
    SourceType sourceType;
    String description;
    Boolean isActive;
    @NotNull

    AttributeGroupDto attributeGroup;

    // Danh sách các giá trị cho các loại không phải Textbox
    List<AttributeValueDto> attributeValues;

}
