package com.laan.sportsda.service.impl;

import com.laan.sportsda.converter.FacultyConverter;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.repository.FacultyRepository;
import com.laan.sportsda.service.FacultyService;
import com.laan.sportsda.validator.FacultyValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final FacultyConverter facultyConverter;

    private final FacultyValidator facultyValidator;

    public FacultyServiceImpl(FacultyRepository facultyRepository, FacultyConverter facultyConverter, FacultyValidator facultyValidator) {
        this.facultyRepository = facultyRepository;
        this.facultyConverter = facultyConverter;
        this.facultyValidator = facultyValidator;
    }

    @Override
    public FacultyResponse getFaculty(final String id) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);

        FacultyResponse facultyResponse = null;
        if (optionalFacultyEntity.isPresent()) {
            facultyResponse = facultyConverter.convertEntityToResponse(optionalFacultyEntity.get());
        }
        return facultyResponse;
    }

    @Override
    public List<FacultyResponse> getFaculties() {
        List<FacultyEntity> facultyEntities = facultyRepository.findAll();
        return facultyConverter.convertEntitiesToResponses(facultyEntities);
    }

    @Override
    public FacultyResponse addFaculty(final FacultyAddRequest facultyAddRequest) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findByName(facultyAddRequest.getName());
        facultyValidator.validateDuplicateFacultyEntity(optionalFacultyEntity);

        FacultyResponse facultyResponse = null;
        if (optionalFacultyEntity.isEmpty()) {
            FacultyEntity facultyEntity = facultyConverter.convertAddRequestToEntity(facultyAddRequest);
            FacultyEntity savedFacultyEntity = facultyRepository.save(facultyEntity);
            facultyResponse = facultyConverter.convertEntityToResponse(savedFacultyEntity);
        }
        return facultyResponse;
    }

    @Override
    public FacultyResponse updateFaculty(final String id, final FacultyUpdateRequest facultyUpdateRequest) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);

        Optional<FacultyEntity> optionalFacultyEntityByName = facultyRepository.findByNameAndIdNotContains(facultyUpdateRequest.getName(), id);
        facultyValidator.validateDuplicateFacultyEntity(optionalFacultyEntityByName);

        FacultyEntity facultyEntity = facultyConverter.convertUpdateRequestToEntity(facultyUpdateRequest, id);
        FacultyEntity updatedFacultyEntity =  facultyRepository.save(facultyEntity);
        return facultyConverter.convertEntityToResponse(updatedFacultyEntity);
    }

    @Override
    public void deleteFaculty(final String id) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(id);
        facultyValidator.validateNonExistingFacultyEntity(id, optionalFacultyEntity);
        facultyRepository.deleteById(id);
    }

}
