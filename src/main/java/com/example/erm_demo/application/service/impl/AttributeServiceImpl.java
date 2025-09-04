package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.AttributeDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeEntity;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeGroupEntity;
import com.example.erm_demo.adapter.out.persistence.entity.AttributeValueEntity;
import com.example.erm_demo.adapter.out.persistence.mapper.AttributeMapper;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeGroupRepository;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeRepository;
import com.example.erm_demo.adapter.out.persistence.repository.AttributeValueRepository;
import com.example.erm_demo.adapter.out.persistence.specification.BaseSpecification;
import com.example.erm_demo.application.service.AttributeService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import com.example.erm_demo.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;
    private final ModelMapper modelMapper;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeGroupRepository attributeGroupRepository;
    private final AttributeMapper attributeMapper;

    @Override
    @Transactional
    public AttributeDto createAttribute(AttributeDto dto) {
        AttributeEntity entity = modelMapper.map(dto, AttributeEntity.class);
        if (dto.getDisplayType().equals("Textbox")) {
            if (dto.getDataType() != null) {
                entity.setDataType(dto.getDataType());
            }
            else throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        AttributeGroupEntity attributeGroup = attributeGroupRepository.findById(dto.getAttributeGroup().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        entity.setAttributeGroupId(attributeGroup.getId());

        entity = attributeRepository.save(entity);

        mapValueToAttributeEntity(dto, entity);

        return attributeMapper.maptoAttributeDto(entity);
    }


    private void mapValueToAttributeEntity(AttributeDto dto, AttributeEntity entity) {
        if(!dto.getDisplayType().equals("Textbox") && dto.getAttributeValues() != null)
            {
                for (var AttributeValueDto : dto.getAttributeValues()) {
                    AttributeValueEntity attributeValueEntity = AttributeValueEntity
                            .builder()
                            .value(AttributeValueDto.getValue())
                            .attributeId(entity.getId())
                            .build();
                    attributeValueRepository.save(attributeValueEntity);
                }
            } else
                throw new AppException(ErrorCode.INVALID_REQUEST);
    }

    @Override
    @Transactional
    public AttributeDto updateAttribute(AttributeDto dto) {
        AttributeEntity existingEntity = attributeRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
        existingEntity = modelMapper.map(dto, AttributeEntity.class);
        if (dto.getDisplayType().equals("Textbox")) {
            if (dto.getDataType() != null) {
                existingEntity.setDataType(dto.getDataType());
            }
            else throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (!dto.getAttributeGroup().getId().equals(existingEntity.getId())) {
            AttributeGroupEntity attributeGroupEntity = attributeGroupRepository.findById(dto.getAttributeGroup().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
            existingEntity.setAttributeGroupId(attributeGroupEntity.getId());
        }

        attributeValueRepository.deleteByAttributeId(existingEntity.getId());
        mapValueToAttributeEntity(dto, existingEntity);

        return attributeMapper.maptoAttributeDto(attributeRepository.save(existingEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeDto getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .map(attributeMapper::maptoAttributeDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteAttribute(Long id) {
        if (!attributeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        attributeValueRepository.deleteByAttributeId(id);
        attributeRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<PageResponseDto<AttributeDto>> search(String code, String dataType, Long attributeGroupId, Boolean isActive, PageRequest pageRequest) {
        Sort sortBy = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortBy);

        Specification<AttributeEntity> keywordSpec = BaseSpecification.hasKeyword(code);
        Specification<AttributeEntity> dataTypeSpec = BaseSpecification.hasKeywordInField(dataType, "dataType");
        Specification<AttributeEntity> attributeGroupSpec = BaseSpecification.hasFieldEqual("attributeGroupId", attributeGroupId);
        Specification<AttributeEntity> isActiveSpec = BaseSpecification.hasFieldBoolean("isActive", isActive);

        Specification<AttributeEntity> specification = Specification.where(keywordSpec).and(dataTypeSpec).and(attributeGroupSpec).and(isActiveSpec);

        Page<AttributeEntity> page = attributeRepository.findAll(specification, pageable);
        Page<AttributeDto> attributeDtoPage = page.map(attributeMapper::maptoAttributeDto);
        return ApiResponseUtil.createPageResponse(attributeDtoPage);

    }
}
