package com.laan.sportsda.service.impl;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.DepartmentShortResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.mapper.DepartmentMapper;
import com.laan.sportsda.repository.DepartmentRepository;
import com.laan.sportsda.repository.FacultyRepository;
import com.laan.sportsda.service.DepartmentService;
import com.laan.sportsda.validator.DepartmentValidator;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    private final DepartmentValidator departmentValidator;

    private final FacultyRepository facultyRepository;

    private final FacultyValidator facultyValidator;

    @Override
    public DepartmentResponse getDepartment(final String id) {
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(id);
        departmentValidator.validateNonExistingDepartmentEntity(id, optionalDepartmentEntity);

        DepartmentResponse departmentResponse = null;
        if (optionalDepartmentEntity.isPresent()) {
            departmentResponse = departmentMapper.mapEntityToResponse(optionalDepartmentEntity.get());
        }
        return departmentResponse;
    }

    @Override
    public List<DepartmentResponse> getDepartments() {
        List<DepartmentEntity> departmentEntities = departmentRepository.findAll();
        return departmentMapper.mapEntitiesToResponses(departmentEntities);
    }

    @Override
    @Transactional
    public DepartmentResponse addDepartment(final DepartmentAddRequest departmentAddRequest) {
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findByName(departmentAddRequest.getName());
        departmentValidator.validateDuplicateDepartmentEntity(optionalDepartmentEntity);

        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(departmentAddRequest.getFacultyId());
        facultyValidator.validateNonExistingFacultyEntity(departmentAddRequest.getFacultyId(), optionalFacultyEntity);

        DepartmentResponse departmentResponse = null;
        if (optionalFacultyEntity.isPresent()) {
            DepartmentEntity departmentEntity = departmentMapper.mapAddRequestToEntity(departmentAddRequest, optionalFacultyEntity.get());
            DepartmentEntity savedDepartmentEntity = departmentRepository.save(departmentEntity);
            departmentResponse = departmentMapper.mapEntityToResponse(savedDepartmentEntity);
        }

        return departmentResponse;
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(final String id, final DepartmentUpdateRequest departmentUpdateRequest) {
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(id);
        departmentValidator.validateNonExistingDepartmentEntity(id, optionalDepartmentEntity);

        Optional<DepartmentEntity> optionalDepartmentEntityByName = departmentRepository.findByNameAndIdNotContains(departmentUpdateRequest.getName(), id);
        departmentValidator.validateDuplicateDepartmentEntity(optionalDepartmentEntityByName);

        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(departmentUpdateRequest.getFacultyId());
        facultyValidator.validateNonExistingFacultyEntity(departmentUpdateRequest.getFacultyId(), optionalFacultyEntity);

        DepartmentResponse departmentResponse = null;
        if (optionalFacultyEntity.isPresent()) {
            DepartmentEntity departmentEntity = departmentMapper.mapUpdateRequestToEntity(departmentUpdateRequest, id, optionalFacultyEntity.get());
            DepartmentEntity updatedDepartmentEntity =  departmentRepository.saveAndFlush(departmentEntity);
            departmentResponse = departmentMapper.mapEntityToResponse(updatedDepartmentEntity);
        }

        return departmentResponse;
    }

    @Override
    @Transactional
    public void deleteDepartment(final String id) {
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findById(id);
        departmentValidator.validateNonExistingDepartmentEntity(id, optionalDepartmentEntity);
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentShortResponse> getDepartmentsByFaculty(final String facultyId) {
        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findById(facultyId);
        facultyValidator.validateNonExistingFacultyEntity(facultyId, optionalFacultyEntity);

        List<DepartmentEntity> departmentEntities = null;
        if (optionalFacultyEntity.isPresent()) {
            departmentEntities = departmentRepository.findByFacultyEntity(optionalFacultyEntity.get());
        }

        return departmentMapper.mapEntitiesToShortResponses(departmentEntities);
    }

}
