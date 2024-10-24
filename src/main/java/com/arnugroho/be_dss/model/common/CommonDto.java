package com.arnugroho.be_dss.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class CommonDto<ID> {
    ID id;
    private String createdBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdOn;
    private String modifiedBy;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime modifiedOn;
    private String uuid;
    private String nomenklatur;
}
