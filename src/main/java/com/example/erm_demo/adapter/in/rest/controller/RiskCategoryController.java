package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.RiskCategoryDto;
import com.example.erm_demo.application.service.RiskCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk-category")
@RequiredArgsConstructor
public class RiskCategoryController {

    private final RiskCategoryService riskCategoryService;

    @PostMapping
    public ApiResponse<RiskCategoryDto> createRiskCategory(@RequestBody @Valid RiskCategoryDto dto) {

        return ApiResponse.<RiskCategoryDto>builder()
                .message("Success")
                .data(riskCategoryService.createRiskCategory(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<RiskCategoryDto> updateRiskCategory(@RequestBody @Valid RiskCategoryDto dto) {

        return ApiResponse.<RiskCategoryDto>builder()
                .message("Success")
                .data(riskCategoryService.updateRiskCategory(dto))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RiskCategoryDto> getRiskCategoryById(@PathVariable("id") Long id) {
        return ApiResponse.<RiskCategoryDto>builder()
                .message("Success")
                .data(riskCategoryService.getRiskCategoryById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<?> getAllRiskCategory( @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<RiskCategoryDto> result = riskCategoryService.getAllRiskCategories(pageRequest);
        return ApiResponse.builder()
                .message("Success")
                .data(result.getContent())

                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRiskCategory(@PathVariable("id") Long id) {
        riskCategoryService.deleteRiskCategory(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<?> searchRiskCategory(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<RiskCategoryDto> result = riskCategoryService.searchByKeyWord( code, systemId, isActive,  pageRequest);
        return   ApiResponse.builder()
                .message("Success")
                .data(result.getContent())

                .build();

    }

}
