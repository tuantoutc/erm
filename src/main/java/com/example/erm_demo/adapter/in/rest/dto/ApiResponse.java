package com.example.erm_demo.adapter.in.rest.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {

    String message;
    String traceId;
    T data;
    Long page;
    Long size;
    String sortBy;
    String sort;
    Long totalElements;
    Long totalPages;
    Long numberOfElements;
}
