package com.example.erm_demo.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseDto<T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<T> content;
    Long page;
    Long size;
    String sort;
    Long totalElements;
    Long totalPages;
    Long numberOfElements;
}
