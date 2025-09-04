package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
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

    @GetMapping("/{id}")
    public ApiResponse<AttributeGroupDto> getAttributeGroupById(@PathVariable("id") Long id) {
        return ApiResponse.<AttributeGroupDto>builder()
                .message("Success")
                .data(attributeGroupService.getAttributeGroupById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<?> getAllAttributeGroup(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AttributeGroupDto> result = attributeGroupService.getAllAttributeGroups(pageRequest);
        return ApiResponse.builder()
                .message("Success")
                .data(result.getContent())

                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAttributeGroup(@PathVariable("id") Long id) {
        attributeGroupService.deleteAttributeGroup(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<?> searchAttributeGroup(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AttributeGroupDto> result = attributeGroupService.searchByKeyWord(code, isActive, pageRequest);
        return  ApiResponse.builder()
                .message("Success")
                .data(result.getContent())

                .build();


    }

}
