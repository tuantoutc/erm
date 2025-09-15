package com.example.erm_demo.adapter.out.persistence.mapper;


import com.example.erm_demo.adapter.in.rest.dto.*;
import com.example.erm_demo.adapter.out.feign.client.DepartmentServiceClient;
import com.example.erm_demo.adapter.out.feign.client.EmployeeServiceClient;
import com.example.erm_demo.adapter.out.feign.client.SystemServiceClient;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExternalServiceValue {
    private final DepartmentServiceClient departmentServiceClient;
    private final EmployeeServiceClient employeeServiceClient;
    private final SystemServiceClient systemServiceClient;

    @Value("${access-token}")
    private String token;
    @Value("${current-domain}")
    private String currentDomain;

    public DepartmentDto getDepartmentById(Long id) {
        DepartmentDto departmentDto = new DepartmentDto();
        try {
            Set<Long> ids = Set.of(id);
            ApiResponse<PageResponseDto<DepartmentDto>> response = departmentServiceClient
                    .getDepartmentByIds(ids, 201L, "Bearer " + token, 0, 20);
            if (response.getData().getContent() != null &&
                    !response.getData().getContent().isEmpty()) {
                departmentDto = response.getData().getContent().get(0);
            }
            return  departmentDto;
        } catch (AppException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
    public EmployeeDto getEmployeeById(Long id) {
        EmployeeDto employeeDto = new EmployeeDto();
        try {
            Set<Long> ids = Set.of(id);
            ApiResponse<PageResponseDto<EmployeeDto>> responseEmployee = employeeServiceClient
                    .getEmployeeByIds(ids, currentDomain, "Bearer " + token, 0, 20);
            if (responseEmployee.getData().getContent() != null &&
                    !responseEmployee.getData().getContent().isEmpty()) {
                employeeDto = responseEmployee.getData().getContent().get(0);
            }
            return  employeeDto;
        } catch (AppException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    public SystemDto getSystemId(Long id) {
        SystemDto systemDto = new SystemDto();
        try {
            Set<Long> ids = Set.of(id);
            ApiResponse<PageResponseDto<SystemDto>> response = systemServiceClient
                    .getSystemsByIds(ids, 0, 20);
            if (response.getData().getContent() != null &&
                    !response.getData().getContent().isEmpty()) {
                systemDto = response.getData().getContent().get(0);
            }
            return  systemDto;
        } catch (AppException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }
    public Map<Long,SystemDto> getSystemIds(Set<Long> ids) {
        Map<Long,SystemDto> systems = new HashMap<>();
        try {
            ApiResponse<PageResponseDto<SystemDto>> response =
                    systemServiceClient.getSystemsByIds(ids, 0, 20);
            if (response.getData().getContent() != null &&
                    !response.getData().getContent().isEmpty())
            {
                systems = response.getData().getContent().stream()
                        .collect(Collectors.toMap(SystemDto::getId, Function.identity()));
            }
            return systems;
        } catch (AppException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


}
