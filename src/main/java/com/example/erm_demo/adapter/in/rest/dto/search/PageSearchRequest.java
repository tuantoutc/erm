package com.example.erm_demo.adapter.in.rest.dto.search;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageSearchRequest {
    int page = 0;
    int size = 10;
    String sortBy = "id"; // format: field,(asc|desc)
    String sort = "asc";

}
