package com.laan.sportsda.service;

import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.entity.DepartmentEntity;
import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.exception.ElementNotFoundException;
import com.laan.sportsda.mapper.DepartmentMapper;
import com.laan.sportsda.repository.DepartmentRepository;
import com.laan.sportsda.repository.FacultyRepository;
import com.laan.sportsda.service.impl.DepartmentServiceImpl;
import com.laan.sportsda.validator.DepartmentValidator;
import com.laan.sportsda.validator.FacultyValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class DepartmentServiceTest {

    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private DepartmentValidator departmentValidator;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyValidator facultyValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentService = new DepartmentServiceImpl(departmentRepository, departmentMapper, departmentValidator,
                facultyRepository, facultyValidator);
    }

    @Test
    void shouldReturnDepartmentForValidId() {
        // Arrange
        String id = "b46a7e04-6999-4d36-a36d-ea0d3bfe9a71";
        String name = "Computer Science";
        FacultyEntity facultyEntity = createDummyFacultyEntity("98765434-6999-4d36-a36d-ea232446", "Applied Sciences");
        DepartmentEntity departmentEntity = createDummyDepartmentEntity(id, name, facultyEntity);
        Optional<DepartmentEntity> optionalDepartmentEntity = Optional.of(departmentEntity);
        DepartmentResponse departmentResponse = createDummyDepartmentResponse(departmentEntity.getId(), departmentEntity.getName(), facultyEntity.getId());

        given(departmentRepository.findById(id)).willReturn(optionalDepartmentEntity);
        doNothing().when(departmentValidator).validateNonExistingDepartmentEntity(id, optionalDepartmentEntity);
        given(departmentMapper.mapEntityToResponse(departmentEntity)).willReturn(departmentResponse);

        // Act
        DepartmentResponse result = departmentService.getDepartment(id);

        // Assert
        assertThat(result, is(departmentResponse));
        assertThat(result.getId(), is(id));
        assertThat(result.getName(), is(name));
        assertThat(result.getFacultyId(), is(facultyEntity.getId()));
    }

    @Test
    void shouldReturnExceptionForInvalidId() {
        String id = "b46a7e04-6999-4d36-a36d-ea0d3bfe9a71";
        Optional<DepartmentEntity> optionalDepartmentEntity = Optional.empty();

        given(departmentRepository.findById(id)).willReturn(optionalDepartmentEntity);
        doThrow(new ElementNotFoundException("No Department found for the id: " + id))
                .when(departmentValidator).validateNonExistingDepartmentEntity(id, optionalDepartmentEntity);

        Assertions.assertThrows(ElementNotFoundException.class, () -> departmentService.getDepartment(id));
    }

    @Test
    void shouldReturnAllDepartments() {
        // Arrange
        FacultyEntity facultyEntity = createDummyFacultyEntity("98765434-6999-4d36-a36d-ea232447", "Management and Commerce");
        DepartmentEntity departmentEntity1 = createDummyDepartmentEntity("b46a7e04-6999-4d36-a36d-ea0d3bfe9a71", "Economics", facultyEntity);
        DepartmentEntity departmentEntity2 = createDummyDepartmentEntity("b46a7e04-6999-4d36-a36d-ea0d3bfe9a72", "Logic", facultyEntity);
        List<DepartmentEntity> departmentEntities = List.of(departmentEntity1, departmentEntity2);

        DepartmentResponse departmentResponse1 = createDummyDepartmentResponse(departmentEntity1.getId(), departmentEntity1.getName(), facultyEntity.getId());
        DepartmentResponse departmentResponse2 = createDummyDepartmentResponse(departmentEntity2.getId(), departmentEntity2.getName(), facultyEntity.getId());
        List<DepartmentResponse> departmentResponses = List.of(departmentResponse1, departmentResponse2);

        given(departmentRepository.findAll()).willReturn(departmentEntities);
        given(departmentMapper.mapEntitiesToResponses(departmentEntities)).willReturn(departmentResponses);

        // Act
        List<DepartmentResponse> result = departmentService.getDepartments();

        // Assert
        assertThat(result, is(departmentResponses));
    }

    @Test
    void shouldReturnNoDepartments() {
        // Arrange
        given(departmentRepository.findAll()).willReturn(List.of());
        given(departmentMapper.mapEntitiesToResponses(List.of())).willReturn(List.of());

        // Act
        List<DepartmentResponse> result = departmentService.getDepartments();

        // Assert
        assertThat(result, is(List.of()));
    }

    private DepartmentResponse createDummyDepartmentResponse(String id, String name, String facultyId) {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(id);
        departmentResponse.setName(name);
        departmentResponse.setFacultyId(facultyId);
        return departmentResponse;
    }

    private DepartmentEntity createDummyDepartmentEntity(String id, String name, FacultyEntity facultyEntity) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(id);
        departmentEntity.setName(name);
        departmentEntity.setFacultyEntity(facultyEntity);
        return departmentEntity;
    }

    private FacultyEntity createDummyFacultyEntity(String id, String name) {
        FacultyEntity facultyEntity = new FacultyEntity();
        facultyEntity.setId(id);
        facultyEntity.setName(name);
        return facultyEntity;
    }

}
