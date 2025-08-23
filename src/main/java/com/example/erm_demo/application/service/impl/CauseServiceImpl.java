package com.example.erm_demo.application.service.impl;

import com.example.erm_demo.adapter.in.rest.dto.CauseDto;
import com.example.erm_demo.adapter.out.persistence.entity.Cause;
import com.example.erm_demo.adapter.out.persistence.mapper.CauseMapper;
import com.example.erm_demo.adapter.out.persistence.repository.CauseRepository;
import com.example.erm_demo.application.service.CauseService;
import com.example.erm_demo.domain.enums.ErrorCode;
import com.example.erm_demo.domain.exception.AppException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CauseServiceImpl implements CauseService {

    private final CauseRepository causeRepository;
    private final CauseMapper causeMapper;


    @Override
    public CauseDto getCauseById(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        return causeRepository.findById(id)
                .map(causeMapper::mapToCauseDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

    }

    @Override
    public CauseDto createCause(CauseDto causeDto) {
        if (causeDto.getId() == null) {
            Cause cause = causeMapper.maptoCause(causeDto);
            return causeMapper.mapToCauseDto(causeRepository.save(cause));
        }
        return null;
    }

    @Override
    public CauseDto updateCause(CauseDto dto) {
        if (dto.getId() == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        return causeRepository.findById(dto.getId())
                .map(cause -> causeMapper.mapUpdateCause(cause, dto))
                .map(causeRepository::save)
                .map(causeMapper::mapToCauseDto)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));

//        Cause cause = causeRepository.findById(causeDto.getId()).
//                orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND));
//        cause = causeMap.mapUpdateCause(cause, causeDto);
//        return causeMap.mapToCauseDto(causeRepository.save(cause));
    }

    @Override
    public void deleteCause(Long id) {
        if (id == null) {
            throw new AppException(ErrorCode.ID_CANNOT_NULL);
        }
        if (!causeRepository.existsById(id)) {
            throw new AppException(ErrorCode.ENTITY_NOT_FOUND);
        }
        causeRepository.deleteById(id);
    }

    @Override
    public List<CauseDto> getAllCauses() {
        List<Cause> causeList = causeRepository.findAll();
        if (!causeList.isEmpty()) {
            return causeList.stream()
                    .map(causeMapper::mapToCauseDto)
                    .toList();
        }
        return List.of();
    }
}
