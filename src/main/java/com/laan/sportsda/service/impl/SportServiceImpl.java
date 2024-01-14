package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.dto.response.SportShortResponse;
import com.laan.sportsda.entity.SportEntity;
import com.laan.sportsda.mapper.SportMapper;
import com.laan.sportsda.repository.SportRepository;
import com.laan.sportsda.service.SportService;
import com.laan.sportsda.validator.SportValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;

    private final SportValidator sportValidator;

    private final SportMapper sportMapper;

    @Override
    public SportResponse getSport(final String id) {
        Optional<SportEntity> optionalSportEntity = sportRepository.findById(id);
        sportValidator.validateNonExistingSportEntity(id, optionalSportEntity);

        SportResponse sportResponse = null;
        if (optionalSportEntity.isPresent()) {
            sportResponse = sportMapper.mapEntityToResponse(optionalSportEntity.get());
        }
        return sportResponse;
    }

    @Override
    public List<SportShortResponse> getSports() {
        List<SportEntity> sportEntities = sportRepository.findAll();
        return sportMapper.mapEntitiesToShortResponses(sportEntities);
    }

    @Override
    @Transactional
    public SportShortResponse addSport(final SportAddRequest sportAddRequest) {
        Optional<SportEntity> optionalSportEntity = sportRepository.findByName(sportAddRequest.getName());
        sportValidator.validateDuplicateSportEntity(optionalSportEntity);

        SportShortResponse sportShortResponse = null;
        if (optionalSportEntity.isEmpty()) {
            SportEntity sportEntity = sportMapper.mapAddRequestToEntity(sportAddRequest);
            SportEntity savedSportEntity = sportRepository.save(sportEntity);
            sportShortResponse = sportMapper.mapEntityToShortResponse(savedSportEntity);
        }
        return sportShortResponse;
    }

    @Override
    @Transactional
    public SportShortResponse updateSport(final String id, final SportUpdateRequest sportUpdateRequest) {
        Optional<SportEntity> optionalSportEntity = sportRepository.findById(id);
        sportValidator.validateNonExistingSportEntity(id, optionalSportEntity);

        Optional<SportEntity> optionalSportEntityByName = sportRepository.findByNameAndIdNotContains(sportUpdateRequest.getName(), id);
        sportValidator.validateDuplicateSportEntity(optionalSportEntityByName);

        SportShortResponse sportShortResponse = null;
        if (optionalSportEntity.isPresent()) {
            SportEntity sportEntity = sportMapper.updateEntityFromUpdateRequest(sportUpdateRequest, optionalSportEntity.get());
            SportEntity updatedSportEntity = sportRepository.save(sportEntity);
            sportShortResponse = sportMapper.mapEntityToShortResponse(updatedSportEntity);
        }
        return sportShortResponse;
    }

    @Override
    @Transactional
    public void deleteSport(final String id) {
        Optional<SportEntity> optionalSportEntity = sportRepository.findById(id);
        sportValidator.validateNonExistingSportEntity(id, optionalSportEntity);
        sportRepository.deleteById(id);
    }
}
