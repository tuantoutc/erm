package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.AttributeGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attribute-group")
@RequiredArgsConstructor
public class AttributeGroupController {

    private final AttributeGroupService attributeGroupService;

    @PostMapping
    public ApiResponse<AttributeGroupDto> createAttributeGroup(@RequestBody @Valid AttributeGroupDto dto) {

        return ApiResponse.<AttributeGroupDto>builder()
                .message("Success")
                .data(attributeGroupService.createAttributeGroup(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<AttributeGroupDto> updateAttributeGroup(@RequestBody @Valid AttributeGroupDto dto) {

        return ApiResponse.<AttributeGroupDto>builder()
                .message("Success")
                .data(attributeGroupService.updateAttributeGroup(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<AttributeGroupDto> getAttributeGroupById(@RequestParam("id") Long id) {
        return ApiResponse.<AttributeGroupDto>builder()
                .message("Success")
                .data(attributeGroupService.getAttributeGroupById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteAttributeGroup(@RequestParam("id") Long id) {
        attributeGroupService.deleteAttributeGroup(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<AttributeGroupDto>> searchAttributeGroup(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return attributeGroupService.search(code, isActive, pageRequest);
    }

}
