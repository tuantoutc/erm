package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.CauseCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public ApiResponse<CauseCategoryDto> getCauseCategoryById(@RequestParam("id") Long id) {
        return ApiResponse.<CauseCategoryDto>builder()
                .message("Success")
                .data(causeCategoryService.getCauseCategoryById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteCauseCategory(@RequestParam("id") Long id) {
        causeCategoryService.deleteCauseCategory(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<CauseCategoryDto>> searchCauseCategory(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return causeCategoryService.searchCauseCategories(code, systemId, pageRequest);
    }
}
