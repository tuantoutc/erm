package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.AttributeGroupDto;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroup;
import com.example.erm_demo.adapter.out.persistence.entity.RiskCategory;
import com.example.erm_demo.adapter.out.persistence.mapper.AttributeGroupMapper;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeGroupRepository;
import com.example.erm_demo.application.service.AttributeGroupService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.enums.SourceType;
import com.example.erm_demo.domain.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttributeGroupServiceImpl implements AttributeGroupService {

    private final AttributeGroupMapper attributeGroupMapper;
    private final AttributeGroupRepository attributeGroupRepository;


    @Override
    @Transactional
    public AttributeGroupDto createAttributeGroup(AttributeGroupDto dto) {
        if (dto.getId() != null) {
            throw new AppException(ErrorCode.ID_MUST_BE_NULL);
        }
        AttributeGroup attributeGroup = attributeGroupMapper.maptoAttributeGroup(dto);
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroupRepository.save(attributeGroup));
    }

    @Override
    @Transactional
    public AttributeGroupDto updateAttributeGroup(AttributeGroupDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }

        // Kiểm tra entity có tồn tại không
        AttributeGroup existingEntity = attributeGroupRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        // Chỉ cho phép cập nhật nếu SourceType là BUSINESS
        if (!existingEntity.getSourceType().equals(SourceType.BUSINESS)) {
            throw new AppException(ErrorCode.SYSTEM_COMPONENT_NOT_MODIFIABLE);
        }

        AttributeGroup attributeGroup = attributeGroupMapper.updateAttributeGroup(existingEntity, dto);
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroupRepository.save(attributeGroup));
    }

    @Override
    public AttributeGroupDto getAttributeGroupById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        AttributeGroup attributeGroup = attributeGroupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        return attributeGroupMapper.maptoAttributeGroupDto(attributeGroup);
    }

    @Override
    @Transactional
    public void deleteAttributeGroup(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }

        // Kiểm tra entity có tồn tại và có SourceType là BUSINESS không
        AttributeGroup existingEntity = attributeGroupRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

        if (!existingEntity.getSourceType().equals(SourceType.BUSINESS)) {
            throw new AppException(ErrorCode.SYSTEM_COMPONENT_NOT_MODIFIABLE);
        }

        attributeGroupRepository.deleteById(id);
    }


    @Override
    public Page<AttributeGroupDto> searchByKeyWord(String code, Boolean isActive, PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);

        Page<AttributeGroup> attributeGroups = attributeGroupRepository.searchBy(code, isActive, pageable);
        return attributeGroups.map(attributeGroupMapper::maptoAttributeGroupDto);
    }

    @Override
    public Page<AttributeGroupDto> getAllAttributeGroups(PageRequest pageRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);

        Page<AttributeGroup> attributeGroupPage = attributeGroupRepository.findAll(pageable);

        return attributeGroupPage.map(attributeGroupMapper::maptoAttributeGroupDto);
    }
}
