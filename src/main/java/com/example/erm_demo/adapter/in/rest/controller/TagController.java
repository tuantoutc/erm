package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.TagDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.TagService;
import com.example.erm_demo.domain.enums.Origin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ApiResponse<TagDto> createTag(@RequestBody @Valid TagDto dto) {

        return ApiResponse.<TagDto>builder()
                .message("Success")
                .data(tagService.createTag(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<TagDto> updateTag(@RequestBody @Valid TagDto dto) {

        return ApiResponse.<TagDto>builder()
                .message("Success")
                .data(tagService.updateTag(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<TagDto> getTagById(@RequestParam("id") Long id) {
        return ApiResponse.<TagDto>builder()
                .message("Success")
                .data(tagService.getTagById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteTag(@RequestParam("id") Long id) {
        tagService.deleteTag(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<PageResponseDto<TagDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return tagService.getAlls(pageRequest);

    }

}
