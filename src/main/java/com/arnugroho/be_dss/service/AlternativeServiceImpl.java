package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.mapper.AlternativeMapper;
import com.arnugroho.be_dss.model.dto.AlternativeDto;
import com.arnugroho.be_dss.model.entity.AlternativeEntity;
import com.arnugroho.be_dss.model.entity.AlternativeEntity_;
import com.arnugroho.be_dss.repository.AlternativeRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
public class AlternativeServiceImpl extends CommonBaseServiceImpl<AlternativeEntity, Long, AlternativeDto> implements AlternativeService {


    public AlternativeServiceImpl(AlternativeRepository repository, AlternativeMapper mapper) {
        super(repository, mapper);

    }

    @Override
    public AlternativeEntity save(AlternativeDto param) {

        if(param.getDataValue().has("statusDelete")) {
            boolean statusDelete = param.getDataValue().get("statusDelete").asBoolean();
            param.setStatusDelete(!statusDelete);
        } else {
            param.setStatusDelete(true);
        }

        String alternativeName = param.getDataValue().get("alternativeName").asText();
        param.setAlternativeName(alternativeName);

        String description = param.getDataValue().has("description")? param.getDataValue().get("description").asText() : "";
        param.setDescription(description);

        return super.save(param);
    }

    @Override
    public Specification<AlternativeEntity> extraSpecification(JsonNode filter) {
        Specification<AlternativeEntity> specification = createDeleteStatusSpecification();
        if (!filter.isEmpty()) {
            Iterator<Map.Entry<String, JsonNode>> fields = filter.fields();


            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> jsonField = fields.next();
                if (!jsonField.getValue().toString().isEmpty()) {
                    String jsonFilterPath = "$." + jsonField.getKey() + " ? (@ like_regex \"" + jsonField.getValue() + "\" flag \"i\")";
                    specification = specification.and((root, query, criteriaBuilder) ->
                            criteriaBuilder.function("jsonb_path_exists",
                                    Boolean.class,
                                    root.get(AlternativeEntity_.DATA_VALUE),
                                    criteriaBuilder.literal(jsonFilterPath)
                            ).in(Boolean.TRUE));
                }
            }
        }
        return specification;
    }
}
