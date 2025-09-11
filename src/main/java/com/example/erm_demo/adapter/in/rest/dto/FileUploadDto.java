package com.example.erm_demo.adapter.in.rest.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadDto {
    Long id;
    String name;
    String url;
    String type;
    LocalDateTime createdAt;


}
