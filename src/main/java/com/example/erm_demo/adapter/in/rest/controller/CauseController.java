package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @GetMapping("/search")
    public ApiResponse<?> searchCauseCategory(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "type", required = false) TypeERM type,
            @RequestParam(value = "origin", required = false) Origin origin,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<CauseDto> result = causeService.searchByKeyWord(pageRequest, code, type, origin, isActive);
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
