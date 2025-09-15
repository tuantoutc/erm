package com.example.erm_demo.util;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import java.util.UUID;

public class ApiResponseUtil {

    public static <T> ApiResponse<PageResponseDto<T>> createPageResponse(Page<T> page) {
        PageResponseDto<T> pageResponseDto = PageResponseDto.<T>builder()
                .content(page.getContent())
                .page((long) page.getNumber())
                .size((long) page.getSize())
                .sort(page.getSort().toString())
                .totalElements(page.getTotalElements())
                .totalPages((long) page.getTotalPages())
                .numberOfElements((long) page.getNumberOfElements())
                .build();

        return ApiResponse.<PageResponseDto<T>>builder()
                .message("Success")
                .traceId(UUID.randomUUID().toString().replace("-", ""))
                .data(pageResponseDto)
                .build();
    }

//    public static <T> ApiResponse<PageResponseDto<T>> createPageResponse(List<T> content, long page, long size, String sort, long totalElements, long totalPages, long numberOfElements) {
//        PageResponseDto<T> pageResponseDto = PageResponseDto.<T>builder()
//                .content(content)
//                .page(page)
//                .size(size)
//                .sort(sort)
//                .totalElements(totalElements)
//                .totalPages(totalPages)
//                .numberOfElements(numberOfElements)
//                .build();
//
//        return ApiResponse.<PageResponseDto<T>>builder()
//                .message("Success")
//                .traceId(UUID.randomUUID().toString().replace("-", ""))
//                .data(pageResponseDto)
//                .build();
//    }
}
