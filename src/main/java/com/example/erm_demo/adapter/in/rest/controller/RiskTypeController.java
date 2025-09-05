package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskTypeDto;
import com.example.erm_demo.application.service.RiskTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk-type")
@RequiredArgsConstructor
public class RiskTypeController {

    private final RiskTypeService riskTypeService;

    @PostMapping
    public ApiResponse<RiskTypeDto> createRiskType(@RequestBody @Valid RiskTypeDto dto) {

        return ApiResponse.<RiskTypeDto>builder()
                .message("Success")
                .data(riskTypeService.createRiskType(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<RiskTypeDto> updateRiskType(@RequestBody @Valid RiskTypeDto dto) {

        return ApiResponse.<RiskTypeDto>builder()
                .message("Success")
                .data(riskTypeService.updateRiskType(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<RiskTypeDto> getRiskTypeById(@RequestParam("id") Long id) {
        return ApiResponse.<RiskTypeDto>builder()
                .message("Success")
                .data(riskTypeService.getRiskTypeById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteRiskType(@RequestParam("id") Long id) {
        riskTypeService.deleteRiskType(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<RiskTypeDto>> searchRiskType(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return  riskTypeService.search(code, systemId, isActive, pageRequest);

    }

}
