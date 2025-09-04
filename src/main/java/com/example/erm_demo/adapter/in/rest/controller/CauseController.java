package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.domain.enums.Origin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cause")
@RequiredArgsConstructor
public class CauseController {

    private final CauseService causeService;

    @PostMapping
    public ApiResponse<CauseDto> createCause(@RequestBody @Valid CauseDto dto) {

        return ApiResponse.<CauseDto>builder()
                .message("Success")
                .data(causeService.createCause(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<CauseDto> updateCause(@RequestBody @Valid CauseDto dto) {

        return ApiResponse.<CauseDto>builder()
                .message("Success")
                .data(causeService.updateCause(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<CauseDto> getCauseById(@RequestParam("id") Long id) {
        return ApiResponse.<CauseDto>builder()
                .message("Success")
                .data(causeService.getCauseById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteCause(@RequestParam("id") Long id) {
        causeService.deleteCause(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<PageResponseDto<CauseDto>> searchCause(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "causeCategoryId", required = false) Long causeCategoryId,
            @RequestParam(value = "origin", required = false) Origin origin,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return causeService.searchCauses(code, causeCategoryId, origin, isActive, pageRequest);

    }

}
