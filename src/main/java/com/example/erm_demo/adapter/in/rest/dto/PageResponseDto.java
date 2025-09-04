package com.example.erm_demo.adapter.in.rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseDto<T> {
    List<T> content;
    Long page;
    Long size;
    String sort;
    Long totalElements;
    Long totalPages;
    Long numberOfElements;
}
