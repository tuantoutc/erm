package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.CauseDto;

import java.util.List;

public interface CauseService {

    CauseDto getCauseById(Long id);
    CauseDto createCause(CauseDto causeDto);
    CauseDto updateCause( CauseDto causeDto);
    void deleteCause(Long id);
    List<CauseDto> getAllCauses();
}
