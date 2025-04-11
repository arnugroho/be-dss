package com.arnugroho.be_dss.service;

import com.arnugroho.be_dss.mapper.PredictiveMapper;
import com.arnugroho.be_dss.model.dto.PredictiveDto;
import com.arnugroho.be_dss.model.entity.PredictiveEntity;
import com.arnugroho.be_dss.repository.PredictiveRepository;
import com.arnugroho.be_dss.service.common.CommonBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PredictiveServiceImpl extends CommonBaseServiceImpl<PredictiveEntity, Long, PredictiveDto> implements PredictiveService {
    private final PredictiveRepository repository;

    public PredictiveServiceImpl(PredictiveRepository repository, PredictiveMapper mapper) {
        super(repository, mapper);
        this.repository = repository;

    }


    @Override
    public Optional<PredictiveEntity> findByAlternativeId(Long alternativeId) {
        return repository.findByAlternativeId(alternativeId);
    }
}
