package com.example.erm_demo.adapter.out.feign.client;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.DepartmentDto;
import com.example.erm_demo.adapter.in.rest.dto.EmployeeDto;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.config.AppConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "employee-service",
        url = "${feign.client.employee-service.url}",
        configuration = AppConfig.class)

public interface EmployeeServiceClient {

    @GetMapping("/services/resources/api/v1/employee/list")
    ApiResponse<PageResponseDto<EmployeeDto>> getEmployeeByIds(
            @RequestParam(value = "ids", required = false) Set<Long> ids,
            @RequestHeader("current-domain") String currentDomain,
            @RequestHeader("Authorization") String authorization,
            @RequestParam("page") int page,
            @RequestParam("size") int size);

}
