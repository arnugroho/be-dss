package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.mapper.CriteriaMapper;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.repository.CriteriaRepository;
import com.arnugroho.be_dss.service.AhpService;
import com.arnugroho.be_dss.service.CriteriaService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/ahp")
public class AhpController {
    private final AhpService ahpService;
    private final CriteriaService criteriaService;
    private final CriteriaRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    public AhpController(AhpService ahpService, CriteriaService criteriaService, CriteriaRepository criteriaRepository, CriteriaMapper criteriaMapper) {
        this.ahpService = ahpService;
        this.criteriaService = criteriaService;
        this.criteriaRepository = criteriaRepository;
        this.criteriaMapper = criteriaMapper;
    }


    @GetMapping("/calculate/{parentId}")
    public DefaultResponse<Map<String, Object>> calculateAhp(@PathVariable Long parentId) {
        return DefaultResponse.ok(ahpService.calculateAhpForLevel(parentId));
    }

    @GetMapping("/calculate/all")
    public DefaultResponse<List<Map<String, Object>>> calculateAhpAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<CriteriaEntity> criteriaDtoList = criteriaRepository.findAllByHasChildAndStatusDeleteFalse("YA");

        criteriaDtoList.forEach(criteriaDto -> {
            Map<String, Object> clc = ahpService.calculateAhpForLevel(criteriaDto.getId());
            result.add(clc);
        });

        return DefaultResponse.ok(result);
    }

}
