package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.SampleActionDto;
import com.example.erm_demo.application.service.SampleActionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample-action")
@RequiredArgsConstructor
public class SampleActionController {

    private final SampleActionService sampleActionService;

    @PostMapping
    public ApiResponse<SampleActionDto> createSampleAction(@RequestBody @Valid SampleActionDto dto) {

        return ApiResponse.<SampleActionDto>builder()
                .message("Success")
                .data(sampleActionService.createSampleAction(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<SampleActionDto> updateSampleAction(@RequestBody @Valid SampleActionDto dto) {

        return ApiResponse.<SampleActionDto>builder()
                .message("Success")
                .data(sampleActionService.updateSampleAction(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<SampleActionDto> getSampleActionById(@RequestParam("id") Long id) {
        return ApiResponse.<SampleActionDto>builder()
                .message("Success")
                .data(sampleActionService.getSampleActionById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteSampleAction(@RequestParam("id") Long id) {
        sampleActionService.deleteSampleAction(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<SampleActionDto>> searchSampleAction(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "riskTypeId", required = false) Long riskTypeId,
            @RequestParam(value = "causeCategoryId", required = false) Long causeCategoryId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return  sampleActionService.search(code, riskTypeId, causeCategoryId, isActive, pageRequest);

    }

}
