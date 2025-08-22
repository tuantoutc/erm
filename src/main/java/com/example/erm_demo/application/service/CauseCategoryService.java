package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.CauseCategoryDto;

import java.util.List;

public interface CauseCategoryService {

    CauseCategoryDto createCauseCategory(CauseCategoryDto causeCategoryDto);

    CauseCategoryDto updateCauseCategory(CauseCategoryDto causeCategoryDto);

    CauseCategoryDto getCauseCategoryById(Long id);

    void deleteCauseCategory(Long id);

    List<CauseCategoryDto> getAllCauseCategories();


}
