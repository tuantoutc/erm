package com.example.erm_demo.adapter.in.rest.controller;

import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.RiskDto;
import com.example.erm_demo.application.service.RiskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/risk")
@RequiredArgsConstructor
public class RiskController {

    private final RiskService riskService;

    // API cho multipart form data (có file upload)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RiskDto> createRiskWithFiles(
            @RequestPart("data") String riskData,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws Exception {
        // Parse JSON string thành RiskDto
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RiskDto dto = objectMapper.readValue(riskData, RiskDto.class);

        return ApiResponse.<RiskDto>builder()
                .message("Success")
                .data(riskService.createRisk(dto, files))
                .build();
    }

    // API cho JSON request (không có file upload)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<RiskDto> createRisk(
            @RequestBody @Valid RiskDto dto
    ) {
        return ApiResponse.<RiskDto>builder()
                .message("Success")
                .data(riskService.createRisk(dto, null))
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RiskDto> updateRiskWithFiles(
            @RequestPart("data") String riskData,
            @RequestPart(value = "files", required = false) MultipartFile[] files
    ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RiskDto dto = objectMapper.readValue(riskData, RiskDto.class);

        return ApiResponse.<RiskDto>builder()
                .message("Success")
                .data(riskService.updateRisk(dto, files))
                .build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<RiskDto> updateRisk(
            @RequestBody @Valid RiskDto dto
    ) {
        return ApiResponse.<RiskDto>builder()
                .message("Success")
                .data(riskService.updateRisk(dto, null))
                .build();
    }

    @GetMapping()
    public ApiResponse<RiskDto> getRiskById(@RequestParam("id") Long id) {
        return ApiResponse.<RiskDto>builder()
                .message("Success")
                .data(riskService.getRiskById(id))
                .build();
    }


    @DeleteMapping()
    public ApiResponse<Void> deleteRisk(@RequestParam("id") Long id) {
        riskService.deleteRisk(id);
        return ApiResponse.<Void>builder()
                .message("Deleted successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponseDto<RiskDto>> searchRisk(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "systemId", required = false) Long systemId,
            @RequestParam(value = "isActive", required = false) Boolean isActive,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return  riskService.search(code, systemId, isActive, pageRequest);

    }

}
