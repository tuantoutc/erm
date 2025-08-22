package com.example.erm_demo.adapter.in.rest.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse <T> {

    String message;
    String traceId;
    T data;
    Long page;
    Long size;
    String sort;
    Long totalElements;
    Long totalPages;
    Long numberOfElements;
}
