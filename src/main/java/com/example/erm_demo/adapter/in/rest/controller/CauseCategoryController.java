package com.example.erm_demo.adapter.in.rest.controller;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.application.service.CauseCategoryService;
import com.example.erm_demo.application.service.impl.CauseCategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cause-category")
@RequiredArgsConstructor
public class CauseCategoryController {


    private final CauseCategoryService causeCategoryService;

    @PostMapping
    public ApiResponse<CauseCategoryDto> createCauseCategory(@RequestBody @Valid CauseCategoryDto dto) {

        return ApiResponse.<CauseCategoryDto>builder()
                .message("Success")
                .data(causeCategoryService.createCauseCategory(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<CauseCategoryDto> updateCauseCategory(@RequestBody @Valid CauseCategoryDto dto) {

        return ApiResponse.<CauseCategoryDto>builder()
                .message("Success")
                .data(causeCategoryService.updateCauseCategory(dto))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CauseCategoryDto> getCauseCategoryById(@PathVariable("id") Long id) {


        return ApiResponse.<CauseCategoryDto>builder()
                .message("Success")
                .data(causeCategoryService.getCauseCategoryById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<?> getAllCauseCategory( @RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CauseCategoryDto> result = causeCategoryService.getAllCauseCategories(pageRequest);
        return ApiResponse.builder()
                .message("Success")
                .data(result.getContent())
                .size((long) result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages((long) result.getTotalPages())
                .numberOfElements((long) result.getNumberOfElements())
                .sort(String.valueOf(result.getSort()))
                .page((long) result.getNumber())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCauseCategory(@PathVariable("id") Long id) {
        causeCategoryService.deleteCauseCategory(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<?> searchCauseCategory(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    )
    {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<CauseCategoryDto> result = causeCategoryService.searchCauseCategories(code, systemId, pageRequest);
        ApiResponse response = ApiResponse.builder()
                .message("Success")
                .data(result.getContent())
                .size((long) result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages((long) result.getTotalPages())
                .numberOfElements((long) result.getNumberOfElements())
                .sort(String.valueOf(result.getSort()))
                .page((long) result.getNumber())
                .build();

        return response;
    }


}
