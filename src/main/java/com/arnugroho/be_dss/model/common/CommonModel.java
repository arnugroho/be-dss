package com.arnugroho.be_dss.model.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

@MappedSuperclass
@Setter
@Getter
public abstract class CommonModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "createdon", nullable = false)
    private ZonedDateTime createdOn;
    @Column(name = "createdby", nullable = false)
    private String createdBy;
    @Column(name = "modifiedon")
    private ZonedDateTime modifiedOn;
    @Column(name = "modifiedby")
    private String modifiedBy;
    @Column(name = "deletedon")
    private ZonedDateTime deletedOn;
    @Column(name = "deletedby")
    private String deletedBy;
    @Column(name = "status_delete", nullable = false)
    private Boolean statusDelete = false;
    @Column(name="uuid", nullable = false, unique = true)
    private String uuid;
}
