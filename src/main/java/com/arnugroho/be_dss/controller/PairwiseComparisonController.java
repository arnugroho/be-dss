package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.dto.PairwiseComparisonDto;
import com.arnugroho.be_dss.repository.PairwiseComparisonRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pairwise")
public class PairwiseComparisonController {

    private final PairwiseComparisonRepository pairwiseComparisonRepository;

    public PairwiseComparisonController(PairwiseComparisonRepository pairwiseComparisonRepository) {
        this.pairwiseComparisonRepository = pairwiseComparisonRepository;
    }

//    @PostMapping("/paged")
//    public DefaultPageResponse<List<JsonNode>> getPaged(@RequestBody PageableRequest<AlternativeDto> request) {
//        Page<AlternativeDto> pagedData = alternativeService.findPages(request);
//        List<JsonNode> result = pagedData.getContent().stream().map(PairwiseComparisonController::assemblyMapping).collect(Collectors.toList());
//
//        return DefaultPageResponse.ok(result, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
//    }

    @PostMapping()
    public DefaultResponse<String> save(@RequestBody PairwiseComparisonDto dto) {

        return DefaultResponse.ok();
    }

//    @PutMapping()
//    public DefaultResponse<String> update(@RequestBody JsonNode dto) {
//        AlternativeDto alternativeDto = new AlternativeDto();
//        alternativeDto.setDataValue(dto);
//        alternativeService.update(alternativeDto);
//        return DefaultResponse.ok();
//    }
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
