package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.util.PathUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void init() throws Exception {
        deleteAllDepartments();
        deleteAllFaculties();
    }

    @Test
    void getDepartment() throws Exception {
        String id = createDepartment("Community Dental health", "Dental Sciences").getId();

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.DEPARTMENTS + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.facultyId").exists())
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the department"))
                        )
                );
    }

    @Test
    void getDepartments() throws Exception {
        createDepartments(Arrays.asList("Nursing and Midwifery", "Medical Laboratory Sciences"), "Allied Health Sciences");

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.DEPARTMENTS))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
                .andExpect(jsonPath("$.[*].facultyId").exists())
                .andExpect(jsonPath("$.[*].version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void addDepartment() throws Exception {
        FacultyResponse facultyResponse = createFaculty("Applied Sciences");

        String departmentName = "Food Science";
        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.DEPARTMENTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentAddRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(departmentName)))
                .andExpect(jsonPath("$.facultyId").value(containsString(facultyResponse.getId())))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the department. Should be unique throughout the system."))
                                        .and(fieldWithPath("facultyId").description("Faculty id which department is bound with.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the department"))
                                        .and(fieldWithPath("name").description("Name of the department"))
                                        .and(fieldWithPath("facultyId").description("Faculty id bound to department"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );

    }

    @Test
    void updateDepartment() throws Exception {
        DepartmentResponse departmentResponse = createDepartment("Zoology", "Applied Sciences");

        String updatedName = "Forestry";
        DepartmentUpdateRequest departmentUpdateRequest = new DepartmentUpdateRequest();
        departmentUpdateRequest.setName(updatedName);
        departmentUpdateRequest.setFacultyId(departmentResponse.getFacultyId());
        departmentUpdateRequest.setVersion(departmentResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.DEPARTMENTS + "/{id}", departmentResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(departmentResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andExpect(jsonPath("$.facultyId").value(containsString(departmentResponse.getFacultyId())))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the department that needs to be updated")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the department. Should be unique throughout the system."))
                                        .and(fieldWithPath("facultyId").description("Updated faculty id of the existing department"))
                                        .and(fieldWithPath("version").description("Version of the existing department")),
                                responseFields(fieldWithPath("id").description("Id of the department"))
                                        .and(fieldWithPath("name").description("Updated name of the department"))
                                        .and(fieldWithPath("facultyId").description("Updated faculty id of the department"))
                                        .and(fieldWithPath("version").description("Updated version number").optional())
                        )
                );
    }

    @Test
    void deleteDepartment() throws Exception {
        DepartmentResponse facultyResponse = createDepartment("Chemistry", "Applied Sciences");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.DEPARTMENTS + "/{id}", facultyResponse.getId()))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the department that needs to be deleted"))
                        )
                );
    }

    private List<DepartmentResponse> createDepartments(List<String> departmentNames, String facultyName) throws Exception {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();

        FacultyResponse facultyResponse = createFaculty(facultyName);

        for (String departmentName: departmentNames) {
            DepartmentAddRequest facultyAddRequest = new DepartmentAddRequest();
            facultyAddRequest.setName(departmentName);
            facultyAddRequest.setFacultyId(facultyResponse.getId());

            MvcResult mvcResult = this.mockMvc.perform(post(PathUtil.DEPARTMENTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(facultyAddRequest))
            ).andReturn();

            byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
            DepartmentResponse departmentResponse = objectMapper.readValue(responseAsArray, DepartmentResponse.class);

            departmentResponses.add(departmentResponse);
        }
        return departmentResponses;
    }

    private DepartmentResponse createDepartment(String departmentName, String facultyName) throws Exception {
        FacultyResponse facultyResponse = createFaculty(facultyName);

        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyResponse.getId());

        MvcResult mvcResult = this.mockMvc.perform(post(PathUtil.DEPARTMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(departmentAddRequest))
        ).andReturn();

        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        return objectMapper.readValue(responseAsArray, DepartmentResponse.class);
    }

    private FacultyResponse createFaculty(String name) throws Exception {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(name);

        MvcResult mvcResult = this.mockMvc.perform(post(PathUtil.FACULTIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(facultyAddRequest))
        ).andReturn();

        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        return objectMapper.readValue(responseAsArray, FacultyResponse.class);
    }

    private void deleteAllDepartments() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(PathUtil.DEPARTMENTS)).andReturn();
        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        List<DepartmentResponse> departmentsResponses = Arrays.asList(objectMapper.readValue(responseAsArray, DepartmentResponse[].class));

        departmentsResponses.forEach(departmentResponse -> {
            try {
                this.mockMvc.perform(delete(PathUtil.DEPARTMENTS + "/{id}", departmentResponse.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void deleteAllFaculties() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(PathUtil.FACULTIES)).andReturn();
        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        List<FacultyResponse> facultyResponses = Arrays.asList(objectMapper.readValue(responseAsArray, FacultyResponse[].class));

        facultyResponses.forEach(facultyResponse -> {
            try {
                this.mockMvc.perform(delete(PathUtil.FACULTIES + "/{id}", facultyResponse.getId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}