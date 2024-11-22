package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.configuration.CommonException;
import com.arnugroho.be_dss.mapper.CriteriaMapper;
import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.CriteriaAllTreeDto;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.dto.CriteriaTreeDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.model.projection.SumWeightProjection;
import com.arnugroho.be_dss.repository.CriteriaRepository;
import com.arnugroho.be_dss.service.CriteriaService;
import com.arnugroho.be_dss.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/criteria")
public class CriteriaController {

    private final CriteriaService criteriaService;
    private final CriteriaRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    public CriteriaController(CriteriaService criteriaService, CriteriaRepository criteriaRepository, CriteriaMapper criteriaMapper) {
        this.criteriaService = criteriaService;
        this.criteriaRepository = criteriaRepository;
        this.criteriaMapper = criteriaMapper;
    }

    @PostMapping("/paged")
    public DefaultPageResponse<List<CriteriaDto>> getPaged(@RequestBody PageableRequest<CriteriaDto> request) {
        Page<CriteriaDto> pagedData = criteriaService.findPages(request);
        List<CriteriaDto> result = pagedData.getContent().stream().peek(criteriaDto -> criteriaDto.setStatusDelete(!criteriaDto.isStatusDelete())
        ).toList();
        return DefaultPageResponse.ok(result, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping("/paged/tree")
    public DefaultPageResponse<List<CriteriaTreeDto>> getPagedTree(@RequestBody PageableRequest<CriteriaTreeDto> request) {
        Page<CriteriaTreeDto> pagedData = criteriaService.findPagesTree(request);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping("/paged/all/tree")
    public DefaultPageResponse<List<CriteriaAllTreeDto>> getPagedCriteriaTree(@RequestBody PageableRequest<CriteriaAllTreeDto> request) {
        Page<CriteriaAllTreeDto> pagedData = criteriaService.findPagesCriteriaTree(request);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping("/paged/child")
    public DefaultPageResponse<List<CriteriaDto>> getPagedChildCriteria(@RequestBody PageableRequest<CriteriaDto> request) {
        Page<CriteriaDto> pagedData = criteriaService.findPagesChild(request);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping()
    public DefaultResponse<String> save(@RequestBody CriteriaDto criteriaDto) {
        //cek sum weight
        cekSumWeightAll(criteriaDto);
        criteriaDto.setStatusDelete(!criteriaDto.isStatusDelete());
        criteriaService.save(criteriaDto);
        return DefaultResponse.ok();
    }

    @PutMapping()
    public DefaultResponse<String> update(@RequestBody CriteriaDto criteriaDto) {
        //cek sum weight
        cekSumWeightAll(criteriaDto);
        criteriaDto.setStatusDelete(!criteriaDto.isStatusDelete());
        criteriaService.update(criteriaDto);
        return DefaultResponse.ok();
    }

    @GetMapping("/{id}")
    public DefaultResponse<CriteriaDto> getById(@PathVariable Long id) {

        return DefaultResponse.ok(criteriaService.findById(id));
    }

    @GetMapping("/uuid/{uuid}")
    public DefaultResponse<CriteriaDto> getByUuid(@PathVariable String uuid) {
        CriteriaDto dto = criteriaService.findByUuid(uuid);
        dto.setStatusDelete(!dto.isStatusDelete());
        return DefaultResponse.ok(dto);
    }

    @DeleteMapping("/uuid/{uuid}")
    public DefaultResponse<String> deleteByUuid(@PathVariable String uuid) {
        CriteriaDto dto = criteriaService.findByUuid(uuid);
        if (dto.getHasChild().equalsIgnoreCase("TIDAK")) {
            criteriaService.delete(dto);
        }
        return DefaultResponse.ok(uuid);
    }

    @DeleteMapping("/ahp/uuid/{uuid}")
    public DefaultResponse<String> deleteAhpByUuid(@PathVariable String uuid) {
        CriteriaDto dto = criteriaService.findByUuid(uuid);
        dto.setIsAhp(!((dto.getIsAhp() == null) ? Boolean.FALSE : dto.getIsAhp()));
        if (dto.getHasChild().equalsIgnoreCase("TIDAK")) {
            criteriaService.update(dto);
        }
        return DefaultResponse.ok(uuid);
    }

    @PostMapping("/paged/parent")
    public DefaultPageResponse<List<CriteriaDto>> getPagedParent(@RequestBody PageableRequest<CriteriaDto> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Page<CriteriaEntity> pagedData = criteriaRepository.findAllByHasChildAndStatusDeleteFalse("YA", pageable);
        List<CriteriaDto> criteriaDtoList = criteriaMapper.toDtoList(pagedData.getContent());
        return DefaultPageResponse.ok(criteriaDtoList, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @GetMapping("/sumweight")
    public DefaultResponse<List<SumWeightProjection>> getSumWeight() {
        return DefaultResponse.ok(criteriaRepository.sumWeight());
    }

    @GetMapping("/sumweight/all")
    public DefaultResponse<Double> getSumWeightAll() {
        return DefaultResponse.ok(criteriaRepository.sumWeightAll());
    }


    public void cekSumWeightAll(CriteriaDto criteriaDto) {
        try {
            CriteriaDto currentCriteriaDto = criteriaService.findById(criteriaDto.getId());

            List<SumWeightProjection> sumWeightProjections = criteriaRepository.sumWeight();
            sumWeightProjections.forEach(sumWeightProjection -> {
                //cek yg parent utama
                if (criteriaDto.getCriteriaParentId() == null && sumWeightProjection.getParentid() == null) {
                    if (sumWeightProjection.getSumweight() + criteriaDto.getCriteriaWeight() - currentCriteriaDto.getCriteriaWeight() > 100) {
                        throw new CommonException("Bobot lebih dari 100%");
                    }
                }
                // carii parent yang sesuai
                else if ( (sumWeightProjection.getParentid() == null ? 0D : Long.valueOf(sumWeightProjection.getParentid())) == (criteriaDto.getCriteriaParentId() == null ? 0D : criteriaDto.getCriteriaParentId())) {
                    // jumlah sekarang > jumlah dari parent
                    CriteriaDto parentCriteriaDto = new CriteriaDto();
                    if (criteriaDto.getCriteriaParentId() != null) {
                        parentCriteriaDto = criteriaService.findById(criteriaDto.getCriteriaParentId());
                    }
                    if (sumWeightProjection.getSumweight() + criteriaDto.getCriteriaWeight() - currentCriteriaDto.getCriteriaWeight() > parentCriteriaDto.getCriteriaWeight()) {
                        throw new CommonException("Maksimal Bobot dari parent " + parentCriteriaDto.getCriteriaName() + " Adalah : " + parentCriteriaDto.getCriteriaWeight() + "%");
                    }
                }
            });
        } catch (Exception e) {

            List<SumWeightProjection> sumWeightProjections = criteriaRepository.sumWeight();
            sumWeightProjections.forEach(sumWeightProjection -> {
                //cek yg parent utama
                if (criteriaDto.getCriteriaParentId() == null && sumWeightProjection.getParentid() == null) {
                    if (sumWeightProjection.getSumweight() + criteriaDto.getCriteriaWeight() > 100) {
                        throw new CommonException("Bobot lebih dari 100%");
                    }
                }
                // carii parent yang sesuai
                else if ( (sumWeightProjection.getParentid() == null ? 0D : Long.valueOf(sumWeightProjection.getParentid())) == (criteriaDto.getCriteriaParentId() == null ? 0D : criteriaDto.getCriteriaParentId())) {
                    // jumlah sekarang > jumlah dari parent
                    CriteriaDto parentCriteriaDto = new CriteriaDto();
                    if (criteriaDto.getCriteriaParentId() != null) {
                        parentCriteriaDto = criteriaService.findById(criteriaDto.getCriteriaParentId());
                    }
                    if (sumWeightProjection.getSumweight() + criteriaDto.getCriteriaWeight() > parentCriteriaDto.getCriteriaWeight()) {
                        throw new CommonException("Maksimal Bobot dari parent " + parentCriteriaDto.getCriteriaName() + " Adalah : " + parentCriteriaDto.getCriteriaWeight() + "%");
                    }
                }
            });
        }


    }
}
