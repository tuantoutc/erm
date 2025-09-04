package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attribute")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping
    public ApiResponse<AttributeDto> createAttribute(@RequestBody @Valid AttributeDto dto) {

        return ApiResponse.<AttributeDto>builder()
                .message("Success")
                .data(attributeService.createAttribute(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<AttributeDto> updateAttribute(@RequestBody @Valid AttributeDto dto) {

        return ApiResponse.<AttributeDto>builder()
                .message("Success")
                .data(attributeService.updateAttribute(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<AttributeDto> getAttributeById(@RequestParam("id") Long id) {
        return ApiResponse.<AttributeDto>builder()
                .message("Success")
                .data(attributeService.getAttributeById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteAttribute(@RequestParam("id") Long id) {
        attributeService.deleteAttribute(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<AttributeDto>> searchAttribute(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "dataType", required = false) String dataType,
            @RequestParam(value = "attributeGroupId", required = false) Long attributeGroupId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return attributeService.search(code,dataType, attributeGroupId, isActive, pageRequest);
    }

}
