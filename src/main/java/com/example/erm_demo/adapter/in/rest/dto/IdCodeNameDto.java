package com.example.erm_demo.adapter.in.rest.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdCodeNameDto {
    Long id;
    String code;
    String name;
}
