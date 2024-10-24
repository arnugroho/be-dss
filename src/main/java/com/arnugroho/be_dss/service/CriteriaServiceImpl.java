package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.mapper.CriteriaMapper;
import com.arnugroho.be_dss.model.dto.CriteriaDto;
import com.arnugroho.be_dss.model.entity.CriteriaEntity;
import com.arnugroho.be_dss.repository.CriteriaRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CriteriaServiceImpl extends CommonBaseServiceImpl<CriteriaEntity, Long, CriteriaDto> implements CriteriaService {

    public CriteriaServiceImpl(CriteriaRepository repository, CriteriaMapper mapper) {
        super(repository, mapper);
    }

}
