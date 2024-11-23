package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.PairwiseComparisonDto;
import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import com.arnugroho.be_dss.repository.PairwiseComparisonRepository;
import com.arnugroho.be_dss.service.PairwiseComparationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/pairwise")
public class PairwiseComparisonController {

    private final PairwiseComparisonRepository pairwiseComparisonRepository;
    private final PairwiseComparationService pairwiseComparationService;

    public PairwiseComparisonController(PairwiseComparisonRepository pairwiseComparisonRepository, PairwiseComparationService pairwiseComparationService) {
        this.pairwiseComparisonRepository = pairwiseComparisonRepository;
        this.pairwiseComparationService = pairwiseComparationService;
    }

    @PostMapping("/paged")
    public DefaultPageResponse<List<PairwiseComparisonDto>> getPaged(@RequestBody PageableRequest<PairwiseComparisonDto> request) {
       Page<PairwiseComparisonDto> pagedData = pairwiseComparationService.findPages(request);
        List<PairwiseComparisonDto> result = pagedData.getContent();//.stream().map(PairwiseComparisonController::assemblyMapping).collect(Collectors.toList());

        return DefaultPageResponse.ok(result, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping()
    public DefaultResponse<String> save(@RequestBody PairwiseComparisonDto dto) {

        return DefaultResponse.ok();
    }

    @PutMapping()
    public DefaultResponse<String> update(@RequestBody PairwiseComparisonDto dto) {
        dto.setCriteria1Id(dto.getCriteria1().getId());
        dto.setCriteria2Id(dto.getCriteria2().getId());
        Optional<PairwiseComparisonEntity> optional = pairwiseComparisonRepository.findByCriteria1IdAndCriteria2Id(dto.getCriteria1Id(), dto.getCriteria2Id());

        if (optional.isEmpty()) {
            pairwiseComparationService.save(dto);
        } else {
            PairwiseComparisonEntity pairwiseComparisonEntity = optional.get();
            dto.setId(pairwiseComparisonEntity.getId());
            dto.setUuid(pairwiseComparisonEntity.getUuid());
            pairwiseComparationService.update(dto);
        }
        return DefaultResponse.ok();
    }
//
//    @GetMapping("/{id}")
//    public DefaultResponse<AlternativeDto> getById(@PathVariable Long id) {
//
//        return DefaultResponse.ok(alternativeService.findById(id));
//    }
//
//    @GetMapping("/uuid/{uuid}")
//    public DefaultResponse<JsonNode> getByUuid(@PathVariable String uuid) {
//        AlternativeDto dto = alternativeService.findByUuid(uuid);
//        return DefaultResponse.ok(assemblyMapping(dto));
//    }
//
//    @DeleteMapping("/uuid/{uuid}")
//    public DefaultResponse<String> deleteByUuid(@PathVariable String uuid) {
//        alternativeService.delete(alternativeService.findByUuid(uuid));
//        return DefaultResponse.ok(uuid);
//    }

//    static public JsonNode assemblyMapping(AlternativeDto dto) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        JsonNode assembly = dto.getDataValue() ;
//
//        ((ObjectNode) assembly).put("uuid", dto.getUuid());
//        ((ObjectNode) assembly).put("statusDelete", !dto.isStatusDelete());
//        ((ObjectNode) assembly).put("alternativeName", dto.getAlternativeName());
//        ((ObjectNode) assembly).put("description", dto.getDescription());
//
//        return assembly;
//    }

}
