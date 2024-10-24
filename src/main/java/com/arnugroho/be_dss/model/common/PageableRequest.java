package com.arnugroho.be_dss.model.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageableRequest<DTO> {
//    @NotNull
//    @Min(value = 1)
//    private Integer page;
    @NotNull
    @Min(value = 1)
    private Integer pageSize;
    @NotNull
    @Min(value = 1)
    private Integer current;
    private String sort;
    private Boolean isSortAsc = Boolean.TRUE;
//    private String search;
    private DTO filter;
}
