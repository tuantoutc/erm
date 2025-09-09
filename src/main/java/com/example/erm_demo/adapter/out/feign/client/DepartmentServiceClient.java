package com.example.erm_demo.adapter.out.feign.client;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.DepartmentDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.config.AppConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "department-service",
        url = "${feign.client.department-service.url}",
        configuration = AppConfig.class)

public interface DepartmentServiceClient {

    @GetMapping("/api/v1/department/list")
    ApiResponse<PageResponseDto<DepartmentDto>> getDepartmentByIds(
            @RequestParam(value = "ids", required = false) Set<Long> ids,
            @RequestHeader("X-TenantId") Long xTenantId,
            @RequestHeader("Authorization") String authorization,
            @RequestParam("page") int page,
            @RequestParam("size") int size);

}
