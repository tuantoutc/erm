package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
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

    @GetMapping()
    public ApiResponse<RiskCategoryDto> getRiskCategoryById(@RequestParam("id") Long id) {
        return ApiResponse.<RiskCategoryDto>builder()
                .message("Success")
                .data(riskCategoryService.getRiskCategoryById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteRiskCategory(@RequestParam("id") Long id) {
        riskCategoryService.deleteRiskCategory(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<RiskCategoryDto>> searchRiskCategory(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return  riskCategoryService.searchRiskCategories(code, systemId, isActive, pageRequest);

    }

}
