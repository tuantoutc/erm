package com.example.erm_demo.application.service;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.TrackingCauseDto;
import org.springframework.data.domain.PageRequest;

public interface TrackingCauseService {

    TrackingCauseDto getTrackingCauseById(Long id);
    TrackingCauseDto createTrackingCause(TrackingCauseDto TrackingCauseDto);
    TrackingCauseDto updateTrackingCause(TrackingCauseDto TrackingCauseDto);
    void deleteTrackingCause(Long id);
    ApiResponse<PageResponseDto<TrackingCauseDto>> getAlls(  PageRequest pageRequest);

}
