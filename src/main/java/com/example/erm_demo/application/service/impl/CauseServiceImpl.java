package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.application.service.CauseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CauseServiceImpl implements CauseService {
    @Override
    public CauseDto getCauseById(Long id) {
        return null;
    }

    @Override
    public CauseDto createCause(CauseDto causeDto) {
        return null;
    }

    @Override
    public CauseDto updateCause(CauseDto causeDto) {
        return null;
    }

    @Override
    public void deleteCause(Long id) {

    }

    @Override
    public List<CauseDto> getAllCauses() {
        return List.of();
    }
}
