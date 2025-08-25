package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.domain.enums.Origin;
import com.example.erm_demo.domain.enums.TypeERM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CauseService {

    CauseDto getCauseById(Long id);
    CauseDto createCause(CauseDto causeDto);
    CauseDto updateCause( CauseDto causeDto);
    void deleteCause(Long id);
    List<CauseDto> getAllCauses();

    Page<CauseDto> searchByKeyWord(PageRequest pageRequest, String keyword, TypeERM type, Origin origin, Boolean isActive);

}
