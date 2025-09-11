package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.TagDto;
import com.example.erm_demo.adapter.out.persistence.entity.TagEntity;
import com.example.erm_demo.adapter.out.persistence.repository.TagRepository;
import com.example.erm_demo.application.service.TagService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public TagDto getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public TagDto createTag(TagDto dto) {
        TagEntity tagEntity = modelMapper.map(dto, TagEntity.class);
        return modelMapper.map(tagRepository.save(tagEntity), TagDto.class);
    }

    @Override
    @Transactional
    public TagDto updateTag(TagDto dto) {
        TagEntity existingEntity = tagRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        existingEntity = tagRepository.save(existingEntity);
        return modelMapper.map(existingEntity, TagDto.class);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        tagRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<TagDto>> getAlls( PageRequest pageRequest) {

        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);
        Page<TagEntity> tagPage = tagRepository.findAll(pageable);
        Page<TagDto> TagDtoPage = tagPage
                .map(tag -> modelMapper.map(tag, TagDto.class));

        return ApiResponseUtil.createPageResponse(TagDtoPage);
    }


}
