package com.example.erm_demo.adapter.in.rest.controller;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.application.service.CauseCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<List<CauseCategoryDto>> getAllCauseCategory() {
        return ApiResponse.<List<CauseCategoryDto>>builder()
                .message("Success")
                .data(causeCategoryService.getAllCauseCategories())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCauseCategory(@PathVariable("id") Long id) {
        causeCategoryService.deleteCauseCategory(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }


}
