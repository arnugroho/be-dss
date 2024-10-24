package com.arnugroho.be_dss.service.common;

import com.arnugroho.be_dss.model.common.CommonDto;
import com.arnugroho.be_dss.model.common.CommonModel;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

public interface CommonBaseService<T extends CommonModel, ID extends Serializable, DTO extends CommonDto<ID>> {
    T save(DTO param);
    void update(DTO param);
    void delete(DTO param);
    List<DTO> findAll();
    DTO findById(ID id);
    Page<DTO> findPages(PageableRequest<DTO> request);
    Page<DTO> findPagesJsonNode(PageableRequest<JsonNode> request);
    Specification<T> createSpecification(String propertyName, Object value);
    Specification<T> createSpecification(String propertyName, Object value, String operation);

    DTO findByUuid(String uuid);
    Specification<T> extraSpecification(DTO dto);
    Specification<T> extraSpecification(JsonNode dto);



}
