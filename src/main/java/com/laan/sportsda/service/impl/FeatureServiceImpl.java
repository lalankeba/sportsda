package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.request.FeatureUpdateRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
import com.laan.sportsda.entity.FeatureEntity;
import com.laan.sportsda.entity.PossibleValueEntity;
import com.laan.sportsda.entity.SportEntity;
import com.laan.sportsda.mapper.FeatureMapper;
import com.laan.sportsda.mapper.PossibleValueMapper;
import com.laan.sportsda.repository.FeatureRepository;
import com.laan.sportsda.repository.PossibleValueRepository;
import com.laan.sportsda.repository.SportRepository;
import com.laan.sportsda.service.FeatureService;
import com.laan.sportsda.validator.FeatureValidator;
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
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    private final FeatureValidator featureValidator;

    private final FeatureMapper featureMapper;

    private final PossibleValueRepository possibleValueRepository;

    private final PossibleValueMapper possibleValueMapper;

    private final SportRepository sportRepository;

    private final SportValidator sportValidator;

    @Override
    public FeatureResponse getFeature(String id) {
        Optional<FeatureEntity> optionalFeatureEntity = featureRepository.findById(id);
        featureValidator.validateNonExistingFeatureEntity(id, optionalFeatureEntity);

        FeatureResponse featureResponse = null;
        if (optionalFeatureEntity.isPresent()) {
            featureResponse = featureMapper.mapEntityToResponse(optionalFeatureEntity.get());
        }
        return featureResponse;
    }

    @Override
    public List<FeatureResponse> getFeatures() {
        List<FeatureEntity> featureEntities = featureRepository.findAll();
        return featureMapper.mapEntitiesToResponses(featureEntities);
    }

    @Override
    @Transactional
    public FeatureResponse addFeature(final FeatureAddRequest featureAddRequest) {
        Optional<SportEntity> optionalSportEntity = sportRepository.findById(featureAddRequest.getSportId());
        sportValidator.validateNonExistingSportEntity(featureAddRequest.getSportId(), optionalSportEntity);

        FeatureResponse featureResponse = null;
        if (optionalSportEntity.isPresent()) {
            SportEntity sportEntity = optionalSportEntity.get();

            Optional<FeatureEntity> optionalFeatureEntity = featureRepository.findByNameAndSportEntity(featureAddRequest.getName(), sportEntity);
            featureValidator.validateDuplicateFeatureEntity(optionalFeatureEntity, sportEntity.getName());

            if (optionalFeatureEntity.isEmpty()) {
                featureValidator.validateFeatureAddRequestWithTypeAndValues(featureAddRequest);

                FeatureEntity featureEntity = featureMapper.mapAddRequestToEntity(featureAddRequest, sportEntity);
                List<PossibleValueEntity> possibleValueEntities = possibleValueMapper.mapStringsAndEntityToEntities(featureAddRequest.getPossibleValues(), featureEntity);
                featureEntity.setPossibleValueEntities(possibleValueEntities);

                FeatureEntity savedFeatureEntity = featureRepository.save(featureEntity);
                featureResponse = featureMapper.mapEntityToResponse(savedFeatureEntity);
            }
        }

        return featureResponse;
    }

    @Override
    @Transactional
    public FeatureResponse updateFeature(final String id, final FeatureUpdateRequest featureUpdateRequest) {
        Optional<FeatureEntity> optionalFeatureEntity = featureRepository.findById(id);
        featureValidator.validateNonExistingFeatureEntity(id, optionalFeatureEntity);

        FeatureResponse featureResponse = null;

        if (optionalFeatureEntity.isPresent()) {
            FeatureEntity existingFeatureEntity = optionalFeatureEntity.get();
            SportEntity sportEntity = existingFeatureEntity.getSportEntity();

            Optional<FeatureEntity> optionalFeatureEntityByName = featureRepository.findByNameAndSportEntityAndIdNotContains(featureUpdateRequest.getName(), sportEntity, id);
            featureValidator.validateDuplicateFeatureEntity(optionalFeatureEntityByName, sportEntity.getName());

            if (optionalFeatureEntityByName.isEmpty()) {
                featureValidator.validateFeatureUpdateRequestWithTypeAndValues(featureUpdateRequest);

                if (existingFeatureEntity.getPossibleValueEntities() != null) {
                    possibleValueRepository.deleteAll(existingFeatureEntity.getPossibleValueEntities());
                }

                FeatureEntity featureEntity = featureMapper.mapUpdateRequestToEntity(featureUpdateRequest, id, sportEntity);
                List<PossibleValueEntity> possibleValueEntities = possibleValueMapper.mapStringsAndEntityToEntities(featureUpdateRequest.getPossibleValues(), featureEntity);
                featureEntity.setPossibleValueEntities(possibleValueEntities);

                FeatureEntity updatedFeatureEntity = featureRepository.saveAndFlush(featureEntity);
                featureResponse = featureMapper.mapEntityToResponse(updatedFeatureEntity);
            }
        }

        return featureResponse;
    }

    @Override
    @Transactional
    public void deleteFeature(final String id) {
        Optional<FeatureEntity> optionalFeatureEntity = featureRepository.findById(id);
        featureValidator.validateNonExistingFeatureEntity(id, optionalFeatureEntity);
        featureRepository.deleteById(id);
    }
}
