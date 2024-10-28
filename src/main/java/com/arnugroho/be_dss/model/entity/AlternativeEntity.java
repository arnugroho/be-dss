package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Setter
@Getter
public class AlternativeEntity extends CommonModel {
    @Type(JsonType.class)
    @Column(name = "data_value", columnDefinition = "jsonb")
    private JsonNode dataValue;
}
