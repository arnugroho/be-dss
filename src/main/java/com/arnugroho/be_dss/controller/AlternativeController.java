package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.DefaultResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.AlternativeDto;
import com.arnugroho.be_dss.service.AlternativeService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/alternative")
public class AlternativeController {

    private final AlternativeService alternativeService;

    public AlternativeController(AlternativeService alternativeService) {
        this.alternativeService = alternativeService;
    }

    @PostMapping("/paged")
    public DefaultPageResponse<List<JsonNode>> getPaged(@RequestBody PageableRequest<AlternativeDto> request) {
        Page<AlternativeDto> pagedData = alternativeService.findPages(request);
        List<JsonNode> result = pagedData.getContent().stream().map(AlternativeController::assemblyMapping).collect(Collectors.toList());

        return DefaultPageResponse.ok(result, pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

    @PostMapping()
    public DefaultResponse<String> save(@RequestBody JsonNode dto) {
        AlternativeDto alternativeDto = new AlternativeDto();
        alternativeDto.setDataValue(dto);
        alternativeService.save(alternativeDto);
        return DefaultResponse.ok();
    }

    @PutMapping()
    public DefaultResponse<String> update(@RequestBody AlternativeDto alternativeDto) {
        alternativeDto.setStatusDelete(!alternativeDto.isStatusDelete());
        alternativeService.update(alternativeDto);
        return DefaultResponse.ok();
    }

    @GetMapping("/{id}")
    public DefaultResponse<AlternativeDto> getById(@PathVariable Long id) {

        return DefaultResponse.ok(alternativeService.findById(id));
    }

    @GetMapping("/uuid/{uuid}")
    public DefaultResponse<AlternativeDto> getByUuid(@PathVariable String uuid) {

        return DefaultResponse.ok(alternativeService.findByUuid(uuid));
    }

    @DeleteMapping("/uuid/{uuid}")
    public DefaultResponse<String> deleteByUuid(@PathVariable String uuid) {
        alternativeService.delete(alternativeService.findByUuid(uuid));
        return DefaultResponse.ok(uuid);
    }

    static public JsonNode assemblyMapping(AlternativeDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonNode assembly = dto.getDataValue() ;

        ((ObjectNode) assembly).put("uuid", dto.getUuid());
        ((ObjectNode) assembly).put("statusDelete", !dto.isStatusDelete());
        ((ObjectNode) assembly).put("alternativeName", dto.getAlternativeName());
        ((ObjectNode) assembly).put("description", dto.getDescription());

        return assembly;
    }

}
