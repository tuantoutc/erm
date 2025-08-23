package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.application.service.CauseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ApiResponse<CauseDto> getCauseById(@PathVariable("id") Long id) {
        return ApiResponse.<CauseDto>builder()
                .message("Success")
                .data(causeService.getCauseById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<CauseDto>> getAllCause() {
        return ApiResponse.<List<CauseDto>>builder()
                .message("Success")
                .data(causeService.getAllCauses())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCause(@PathVariable("id") Long id) {
        causeService.deleteCause(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

}
