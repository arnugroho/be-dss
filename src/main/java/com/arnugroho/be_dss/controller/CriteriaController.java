package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.mapper.CriteriaMapper;
import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.dto.CriteriaTreeDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
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

    @PostMapping("/paged/child")
    public DefaultPageResponse<List<CriteriaDto>> getPagedChildCriteria(@RequestBody PageableRequest<CriteriaDto> request) {
        Page<CriteriaDto> pagedData = criteriaService.findPagesChild(request);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping()
    public DefaultResponse<String> save(@RequestBody CriteriaDto criteriaDto) {
//        CategoryJournalDto categoryJournalDto = categoryJournalService.findByUuid(journalDto.getCategoryJournal().getUuid());
//        journalDto.setCategoryJournalId(categoryJournalDto.getId());
//        journalDto.setCategoryJournal(categoryJournalDto);
        criteriaDto.setStatusDelete(!criteriaDto.isStatusDelete());
        criteriaService.save(criteriaDto);
        return DefaultResponse.ok();
    }

    @PutMapping()
    public DefaultResponse<String> update(@RequestBody CriteriaDto criteriaDto) {
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
        criteriaService.delete(criteriaService.findByUuid(uuid));
        return DefaultResponse.ok(uuid);
    }

    @PostMapping("/paged/parent")
    public DefaultPageResponse<List<CriteriaDto>> getPagedParent(@RequestBody PageableRequest<CriteriaDto> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Page<CriteriaEntity> pagedData = criteriaRepository.findAllByHasChildAndStatusDeleteFalse("YA", pageable);
        List<CriteriaDto> criteriaDtoList = criteriaMapper.toDtoList(pagedData.getContent());
        return DefaultPageResponse.ok(criteriaDtoList, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

}
