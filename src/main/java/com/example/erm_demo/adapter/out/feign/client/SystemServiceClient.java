package com.example.erm_demo.adapter.out.feign.client;


import com.example.erm_demo.adapter.in.rest.dto.ApiResponse;
import com.example.erm_demo.adapter.in.rest.dto.PageResponseDto;
import com.example.erm_demo.adapter.in.rest.dto.SystemDto;
import com.example.erm_demo.config.AppConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(
        name = "system-service",
        url = "${feign.client.system-service.url}",
        configuration = AppConfig.class)

public interface SystemServiceClient {


    @GetMapping("/api/v1/system/list")
    ApiResponse<PageResponseDto<SystemDto>> getSystemsByIds(
            @RequestParam("ids") Set<Long> ids,
            @RequestParam int page,
            @RequestParam int size);

}
