package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.mapper.FacultyMapper;
import com.laan.sportsda.repository.FacultyRepository;
import com.laan.sportsda.service.FacultyService;
import com.laan.sportsda.validator.FacultyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final FacultyValidator facultyValidator;

    private final FacultyMapper facultyMapper;

    @Override
    public FacultyResponse getFaculty(final String id) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);

        FacultyResponse facultyResponse = null;
        if (optionalFacultyEntity.isPresent()) {
            facultyResponse = facultyMapper.mapEntityToResponse(optionalFacultyEntity.get());
        }
        return facultyResponse;
    }

    @Override
    public List<FacultyResponse> getFaculties() {
        List<FacultyEntity> facultyEntities = facultyRepository.findAll();
        return facultyMapper.mapEntitiesToResponses(facultyEntities);
    }

    @Override
    @Transactional
    public FacultyResponse addFaculty(final FacultyAddRequest facultyAddRequest) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findByName(facultyAddRequest.getName());
        facultyValidator.validateDuplicateFacultyEntity(optionalFacultyEntity);

        FacultyResponse facultyResponse = null;
        if (optionalFacultyEntity.isEmpty()) {
            FacultyEntity facultyEntity = facultyMapper.mapAddRequestToEntity(facultyAddRequest);
            FacultyEntity savedFacultyEntity = facultyRepository.save(facultyEntity);
            facultyResponse = facultyMapper.mapEntityToResponse(savedFacultyEntity);
        }
        return facultyResponse;
    }

    @Override
    @Transactional
    public FacultyResponse updateFaculty(final String id, final FacultyUpdateRequest facultyUpdateRequest) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);

        Optional<FacultyEntity> optionalFacultyEntityByName = facultyRepository.findByNameAndIdNotContains(facultyUpdateRequest.getName(), id);
        facultyValidator.validateDuplicateFacultyEntity(optionalFacultyEntityByName);

        FacultyEntity facultyEntity = facultyMapper.mapUpdateRequestToEntity(facultyUpdateRequest, id);
        FacultyEntity updatedFacultyEntity =  facultyRepository.save(facultyEntity);
        return facultyMapper.mapEntityToResponse(updatedFacultyEntity);
    }

    @Override
    @Transactional
    public void deleteFaculty(final String id) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);
        facultyRepository.deleteById(id);
    }

}
