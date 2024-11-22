package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.mapper.PairwiseComparisonMapper;
import com.arnugroho.be_dss.model.dto.PairwiseComparisonDto;
import com.arnugroho.be_dss.model.entity.PairwiseComparisonEntity;
import com.arnugroho.be_dss.repository.PairwiseComparisonRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PairwiseComparationServiceImpl extends CommonBaseServiceImpl<PairwiseComparisonEntity, Long, PairwiseComparisonDto> implements PairwiseComparationService {


    public PairwiseComparationServiceImpl(PairwiseComparisonRepository repository, PairwiseComparisonMapper mapper) {
        super(repository, mapper);

    }
}
