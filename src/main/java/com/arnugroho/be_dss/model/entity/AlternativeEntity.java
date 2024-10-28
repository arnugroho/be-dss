package com.arnugroho.be_dss.model.entity;

import com.arnugroho.be_dss.model.common.CommonModel;
import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Setter
@Getter
@Entity
@Table(name = "alternative")
public class AlternativeEntity extends CommonModel {
    @Column(name = "alternative_name", unique = true, nullable = false)
    private String alternativeName;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Type(JsonType.class)
    @Column(name = "data_value", columnDefinition = "jsonb")
    private JsonNode dataValue;
}
