package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.TagDto;
import com.example.erm_demo.adapter.in.rest.dto.TrackingCauseDto;
import com.example.erm_demo.adapter.out.persistence.entity.TagEntity;
import com.example.erm_demo.adapter.out.persistence.repository.TagRepository;
import com.example.erm_demo.application.service.TagService;
import com.example.erm_demo.application.service.TrackingCauseService;
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
public class TrackingCauseServiceImpl implements TrackingCauseService {
    @Override
    public TrackingCauseDto getTrackingCauseById(Long id) {
        return null;
    }

    @Override
    public TrackingCauseDto createTrackingCause(TrackingCauseDto TrackingCauseDto) {
        return null;
    }

    @Override
    public TrackingCauseDto updateTrackingCause(TrackingCauseDto TrackingCauseDto) {
        return null;
    }

    @Override
    public void deleteTrackingCause(Long id) {

    }

    @Override
    public ApiResponse<PageResponseDto<TrackingCauseDto>> getAlls(PageRequest pageRequest) {
        return null;
    }
}
