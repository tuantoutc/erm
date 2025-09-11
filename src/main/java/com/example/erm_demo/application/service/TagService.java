package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.TagDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.domain.enums.Origin;
import org.springframework.data.domain.PageRequest;

public interface TagService {

    TagDto getTagById(Long id);
    TagDto createTag(TagDto tagDto);
    TagDto updateTag(TagDto tagDto);
    void deleteTag(Long id);
    ApiResponse<PageResponseDto<TagDto>> getAlls(  PageRequest pageRequest);

}
