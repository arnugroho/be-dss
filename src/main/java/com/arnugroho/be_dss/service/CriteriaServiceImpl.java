package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.mapper.CriteriaAllTreeMapper;
import com.arnugroho.be_dss.mapper.CriteriaMapper;
import com.arnugroho.be_dss.mapper.CriteriaTreeMapper;
import com.arnugroho.be_dss.model.common.CommonModel_;
import com.arnugroho.be_dss.model.common.PageableRequest;
import com.arnugroho.be_dss.model.dto.CriteriaAllTreeDto;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.dto.CriteriaTreeDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.model.entity.CriteriaEntity_;
import com.arnugroho.be_dss.repository.CriteriaRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import com.arnugroho.be_dss.utils.PageableUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CriteriaServiceImpl extends CommonBaseServiceImpl<CriteriaEntity, Long, CriteriaDto> implements CriteriaService {
    final CriteriaTreeMapper criteriaTreeMapper;
    final CriteriaRepository repository;
    final CriteriaMapper mapper;
    final CriteriaAllTreeMapper allTreeMapper;

    public CriteriaServiceImpl(CriteriaRepository repository, CriteriaMapper mapper, CriteriaTreeMapper criteriaTreeMapper, CriteriaAllTreeMapper allTreeMapper) {
        super(repository, mapper);
        this.criteriaTreeMapper = criteriaTreeMapper;
        this.repository = repository;
        this.mapper = mapper;
        this.allTreeMapper = allTreeMapper;
    }

    @Override
    public Page<CriteriaTreeDto> findPagesTree(PageableRequest<CriteriaTreeDto> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Specification<CriteriaEntity> specification = createDeleteStatusSpecification();
        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(CriteriaEntity_.CRITERIA_PARENT)
        ));
//        specification = specification.and((root, query, criteriaBuilder) ->
//                {
//                    Join<CriteriaEntity, CriteriaEntity> childrenJoin = root.join("children", JoinType.INNER);
//
//                    return criteriaBuilder.and(criteriaBuilder.isNull(root.get(CriteriaEntity_.CRITERIA_PARENT))
//                            , criteriaBuilder.isFalse(childrenJoin.get(CommonModel_.statusDelete)));
//                }
//        );
        Page<CriteriaEntity> productEntityPage;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CriteriaEntity entity = criteriaTreeMapper.fromDto(request.getFilter());
        JsonNode filter = objectMapper.valueToTree(entity);

        ((ObjectNode) filter).remove("statusDelete");

        Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();

        String operation = LIKE;
        Object value;
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            if (!jsonField.getValue().isObject()) {
                if (jsonField.getKey().equalsIgnoreCase(CommonModel_.ID)) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();

                    // Convert long column to string for pattern matching
//                        Expression<String> castedColumn = criteriaBuilder.function("CAST", String.class, joinEntityB.get("longColumn"), criteriaBuilder.literal(String.class));

                } else if (jsonField.getValue().isNumber()) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();
                } else {
                    operation = LIKE;
                    value = jsonField.getValue().asText();
                }

                specification = specification.and(createSpecification(jsonField.getKey().toString(), value, operation));
//
            }
        }

        productEntityPage = repository.findAll(specification, pageable);


        return new PageImpl<>(productEntityPage.getContent().stream().map(criteriaTreeMapper::toDto).collect(Collectors.toList()), pageable, productEntityPage.getTotalElements());

    }

    @Override
    public Page<CriteriaDto> findPagesChild(PageableRequest<CriteriaDto> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Specification<CriteriaEntity> specification = createDeleteStatusSpecification();
        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(CriteriaEntity_.HAS_CHILD), "TIDAK"));
        Page<CriteriaEntity> productEntityPage;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CriteriaEntity entity = mapper.fromDto(request.getFilter());
        JsonNode filter = objectMapper.valueToTree(entity);

        ((ObjectNode) filter).remove("statusDelete");

        Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();

        String operation = LIKE;
        Object value;
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            if (!jsonField.getValue().isObject()) {
                if (jsonField.getKey().equalsIgnoreCase(CommonModel_.ID)) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();

                    // Convert long column to string for pattern matching
//                        Expression<String> castedColumn = criteriaBuilder.function("CAST", String.class, joinEntityB.get("longColumn"), criteriaBuilder.literal(String.class));

                } else if (jsonField.getValue().isNumber()) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();
                } else {
                    operation = LIKE;
                    value = jsonField.getValue().asText();
                }

                specification = specification.and(createSpecification(jsonField.getKey().toString(), value, operation));
//
            }
        }

        productEntityPage = repository.findAll(specification, pageable);


        return new PageImpl<>(productEntityPage.getContent().stream().map(mapper::toDto).collect(Collectors.toList()), pageable, productEntityPage.getTotalElements());

    }

    @Override
    public Page<CriteriaAllTreeDto> findPagesCriteriaTree(PageableRequest<CriteriaAllTreeDto> request) {
        Pageable pageable = PageableUtil.createPageableRequest(request);
        Specification<CriteriaEntity> specification = ((root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get(CriteriaEntity_.CRITERIA_PARENT)
                ));
//        specification = specification.and((root, query, criteriaBuilder) ->
//                {
//                    Join<CriteriaEntity, CriteriaEntity> childrenJoin = root.join("children", JoinType.INNER);
//
//                    return criteriaBuilder.and(criteriaBuilder.isNull(root.get(CriteriaEntity_.CRITERIA_PARENT))
//                            , criteriaBuilder.isFalse(childrenJoin.get(CommonModel_.statusDelete)));
//                }
//        );
        Page<CriteriaEntity> productEntityPage;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        CriteriaEntity entity = allTreeMapper.fromDto(request.getFilter());
        JsonNode filter = objectMapper.valueToTree(entity);

        ((ObjectNode) filter).remove("statusDelete");

        Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();

        String operation = LIKE;
        Object value;
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> jsonField = fields.next();
            if (!jsonField.getValue().isObject()) {
                if (jsonField.getKey().equalsIgnoreCase(CommonModel_.ID)) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();

                    // Convert long column to string for pattern matching
//                        Expression<String> castedColumn = criteriaBuilder.function("CAST", String.class, joinEntityB.get("longColumn"), criteriaBuilder.literal(String.class));

                } else if (jsonField.getValue().isNumber()) {
                    operation = EQUALS;
                    value = jsonField.getValue().longValue();
                } else {
                    operation = LIKE;
                    value = jsonField.getValue().asText();
                }

                specification = specification.and(createSpecification(jsonField.getKey().toString(), value, operation));
//
            }
        }

        productEntityPage = repository.findAll(specification, pageable);


        return new PageImpl<>(productEntityPage.getContent().stream().map(allTreeMapper::toDto).collect(Collectors.toList()), pageable, productEntityPage.getTotalElements());

    }

    @Override
    public void delete(CriteriaDto param) {
        param.setStatusDelete(!param.isStatusDelete());
        super.update(param);
    }
}
