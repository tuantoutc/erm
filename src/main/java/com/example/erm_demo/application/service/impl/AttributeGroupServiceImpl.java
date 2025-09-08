package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.AttributeGroupMapper;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeGroupRepository;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.AttributeGroupService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.SourceType;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttributeGroupServiceImpl implements AttributeGroupService {

    private final AttributeGroupMapper attributeGroupMapper;
    private final AttributeGroupRepository attributeGroupRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AttributeGroupDto createAttributeGroup(AttributeGroupDto dto) {
        AttributeGroupEntity attributeGroupEntity = modelMapper.map(dto, AttributeGroupEntity.class);
        if( dto.getSourceType() == null || !(dto.getSourceType()==SourceType.SYSTEM))
        {
            attributeGroupEntity.setSourceType(SourceType.BUSINESS);
        }
        else attributeGroupEntity.setSourceType(SourceType.SYSTEM);
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroupRepository.save(attributeGroupEntity));
    }

    @Override
    @Transactional
    public AttributeGroupDto updateAttributeGroup(AttributeGroupDto dto) {
        // Kiểm tra entity có tồn tại không
        AttributeGroupEntity existingEntity = attributeGroupRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        // Chỉ cho phép cập nhật nếu SourceType là BUSINESS
        if (!(existingEntity.getSourceType() == SourceType.BUSINESS)) {
            throw new AppException(ErrorCode.SYSTEM_COMPONENT_NOT_MODIFIABLE);
        }
        existingEntity = modelMapper.map(dto , AttributeGroupEntity.class);
        if( dto.getSourceType() == null || !(dto.getSourceType()==SourceType.SYSTEM))
        {
            existingEntity.setSourceType(SourceType.BUSINESS);
        }
        else existingEntity.setSourceType(SourceType.SYSTEM);
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroupRepository.save(existingEntity));
    }

    @Override
    public AttributeGroupDto getAttributeGroupById(Long id) {
        AttributeGroupEntity attributeGroupEntity = attributeGroupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroupEntity);
    }

    @Override
    @Transactional
    public void deleteAttributeGroup(Long id) {
        // Kiểm tra entity có tồn tại và có SourceType là BUSINESS không
        AttributeGroupEntity existingEntity = attributeGroupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        if (!existingEntity.getSourceType().equals(SourceType.BUSINESS)) {
            throw new AppException(ErrorCode.SYSTEM_COMPONENT_NOT_MODIFIABLE);
        }
//        attributeRepository.deleteByAtributeGroupId(id);
        attributeGroupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ApiResponse<PageResponseDto<AttributeGroupDto>> search(String code, Boolean isActive, PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);

        Specification<AttributeGroupEntity> keywordSpec = BaseSpecification.hasKeyword(code);
        Specification<AttributeGroupEntity> isActiveSpec = BaseSpecification.hasFieldBoolean("isActive", isActive);
        Specification<AttributeGroupEntity> specification = Specification.where(keywordSpec).and(isActiveSpec);

        Page<AttributeGroupEntity> page = attributeGroupRepository.findAll(specification, pageable);
        Page<AttributeGroupDto>  pageDto = page.map(attributeGroupMapper::maptoAttributeGroupDto);

        return ApiResponseUtil.createPageResponse(pageDto);
    }
}
