package com.arnugroho.be_dss.controller;

import com.arnugroho.be_dss.model.common.DefaultPageResponse;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.entity.SawRankEntity;
import com.arnugroho.be_dss.model.entity.TopsisRankEntity;
import com.arnugroho.be_dss.model.entity.WpRankEntity;
import com.arnugroho.be_dss.repository.SawRankRepository;
import com.arnugroho.be_dss.repository.TopsisRankRepository;
import com.arnugroho.be_dss.repository.WpRankRepository;
import com.arnugroho.be_dss.utils.PageableUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dss_rank")
public class DssRankController {
    private final SawRankRepository sawRankRepository;
    private final WpRankRepository wpRankRepository;
    private final TopsisRankRepository topsisRankRepository;

    public DssRankController(SawRankRepository sawRankRepository, WpRankRepository wpRankRepository, TopsisRankRepository topsisRankRepository) {
        this.sawRankRepository = sawRankRepository;
        this.wpRankRepository = wpRankRepository;
        this.topsisRankRepository = topsisRankRepository;
    }


    @PostMapping("/paged/saw")
    public DefaultPageResponse<List<SawRankEntity>> getPagedSaw(@RequestBody PageableRequest<JsonNode> request) {

        if (request.getSort()== null){
            request.setSort("rank");
            request.setIsSortAsc(Boolean.TRUE);
        }
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Page<SawRankEntity> pagedData = sawRankRepository.findAll(pageable);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }
    @PostMapping("/paged/wp")
    public DefaultPageResponse<List<WpRankEntity>> getPagedWp(@RequestBody PageableRequest<JsonNode> request) {

        if (request.getSort()== null){
            request.setSort("rank");
            request.setIsSortAsc(Boolean.TRUE);
        }
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Page<WpRankEntity> pagedData = wpRankRepository.findAll(pageable);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }
    @PostMapping("/paged/topsis")
    public DefaultPageResponse<List<TopsisRankEntity>> getPagedTopsis(@RequestBody PageableRequest<JsonNode> request) {

        if (request.getSort()== null){
            request.setSort("rank");
            request.setIsSortAsc(Boolean.TRUE);
        }
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Page<TopsisRankEntity> pagedData = topsisRankRepository.findAll(pageable);

        return DefaultPageResponse.ok(pagedData.getContent(), pagedData.getNumber() + 1, pagedData.getSize(), pagedData.getTotalElements());
    }

}
