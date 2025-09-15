package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.TrackingCauseDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.application.service.TrackingCauseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/TrackingCause")
@RequiredArgsConstructor
public class TrackingCauseController {

    private final TrackingCauseService TrackingCauseService;

    @PostMapping
    public ApiResponse<TrackingCauseDto> createTrackingCause(@RequestBody @Valid TrackingCauseDto dto) {

        return ApiResponse.<TrackingCauseDto>builder()
                .message("Success")
                .data(TrackingCauseService.createTrackingCause(dto))
                .build();
    }

    @PutMapping
    public ApiResponse<TrackingCauseDto> updateTrackingCause(@RequestBody @Valid TrackingCauseDto dto) {

        return ApiResponse.<TrackingCauseDto>builder()
                .message("Success")
                .data(TrackingCauseService.updateTrackingCause(dto))
                .build();
    }

    @GetMapping()
    public ApiResponse<TrackingCauseDto> getTrackingCauseById(@RequestParam("id") Long id) {
        return ApiResponse.<TrackingCauseDto>builder()
                .message("Success")
                .data(TrackingCauseService.getTrackingCauseById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteTrackingCause(@RequestParam("id") Long id) {
        TrackingCauseService.deleteTrackingCause(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<TrackingCauseDto>> searchTrackingCause(

            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return TrackingCauseService.getAlls( pageRequest);
    }

}
