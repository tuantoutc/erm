package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;
import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseCategoryMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseCategoryRepository;
import com.example.erm_demo.application.service.CauseCategoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class CauseCategoryServiceImpl implements CauseCategoryService {

    private final CauseCategoryRepository causeCategoryRepository;
    private final CauseCategoryMapper causeCategoryMapper;


    @Override
    public CauseCategoryDto createCauseCategory(CauseCategoryDto causeCategoryDto) {
        if(causeCategoryDto.getId()==null) {
            CauseCategory causeCategory = causeCategoryMapper.mapToCauseCategory(causeCategoryDto);
            return causeCategoryMapper.mapToCauseCategoryDto(causeCategoryRepository.save(causeCategory));
        }
        else
            return null;
    }

    @Override
    public CauseCategoryDto updateCauseCategory(CauseCategoryDto causeCategoryDto) {
        return null;
    }

    @Override
    public CauseCategoryDto getCauseCategoryById(Long id) {
        if(id == null){
            return null;
        }
        return causeCategoryRepository.findById(id)
                .map(causeCategoryMapper::mapToCauseCategoryDto)
                .orElse(null);
    }

    @Override
    public void deleteCauseCategory(Long id) {
        if (id != null) {
            if (!causeCategoryRepository.existsById(id)) {
                throw new RuntimeException("Cause Category not found with id: " + id);
            }
            causeCategoryRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("ID cannot be null");
        }

    }

    @Override
    public List<CauseCategoryDto> getAllCauseCategories() {
        List<CauseCategory> causeCategories = causeCategoryRepository.findAll();
        if (!causeCategories.isEmpty()) {
            return causeCategories.stream()
                    .map(causeCategoryMapper::mapToCauseCategoryDto)
                    .toList();
        } else
            return List.of();
    }

}
