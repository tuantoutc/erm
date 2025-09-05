package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PreventiveMeasureDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.PreventiveMeasureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preventive-measure")
@RequiredArgsConstructor
public class PreventiveMeasureController {

    private final PreventiveMeasureService preventiveMeasureService;

    @PostMapping
    public ApiResponse<PreventiveMeasureDto> createPreventiveMeasure(@RequestBody @Valid PreventiveMeasureDto dto) {

        return ApiResponse.<PreventiveMeasureDto>builder()
                .message("Success")
                .data(preventiveMeasureService.createPreventiveMeasure(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<PreventiveMeasureDto> updatePreventiveMeasure(@RequestBody @Valid PreventiveMeasureDto dto) {

        return ApiResponse.<PreventiveMeasureDto>builder()
                .message("Success")
                .data(preventiveMeasureService.updatePreventiveMeasure(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<PreventiveMeasureDto> getPreventiveMeasureById(@RequestParam("id") Long id) {
        return ApiResponse.<PreventiveMeasureDto>builder()
                .message("Success")
                .data(preventiveMeasureService.getPreventiveMeasureById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deletePreventiveMeasure(@RequestParam("id") Long id) {
        preventiveMeasureService.deletePreventiveMeasure(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<PreventiveMeasureDto>> searchPreventiveMeasure(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return preventiveMeasureService.search(code, isActive, pageRequest);
    }

}
