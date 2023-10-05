package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.util.ConstantsUtil;
import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void initAfter() {
        testUtils.deleteAllDepartments();
        testUtils.deleteAllFaculties();
    }

    @Test
    void getDepartment() throws Exception {
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Dental Sciences");
        String id = testUtils.createDepartment("Community Dental health", facultyShortResponse).getId();

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.DEPARTMENTS + PathUtil.ID_PLACEHOLDER, id)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
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
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Allied Health Sciences");
        testUtils.createDepartments(Arrays.asList("Nursing and Midwifery", "Medical Laboratory Sciences"), facultyShortResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.DEPARTMENTS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
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
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Applied Sciences");

        String departmentName = "Food Science";
        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.DEPARTMENTS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(departmentAddRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(departmentName)))
                .andExpect(jsonPath("$.facultyId").value(containsString(facultyShortResponse.getId())))
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
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Applied Sciences");
        DepartmentResponse departmentResponse = testUtils.createDepartment("Zoology", facultyShortResponse);

        String updatedName = "Forestry";
        DepartmentUpdateRequest departmentUpdateRequest = new DepartmentUpdateRequest();
        departmentUpdateRequest.setName(updatedName);
        departmentUpdateRequest.setFacultyId(departmentResponse.getFacultyId());
        departmentUpdateRequest.setVersion(departmentResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.DEPARTMENTS + PathUtil.ID_PLACEHOLDER, departmentResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
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
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Applied Sciences");
        DepartmentResponse departmentResponse = testUtils.createDepartment("Chemistry", facultyShortResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.DEPARTMENTS + PathUtil.ID_PLACEHOLDER, departmentResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the department that needs to be deleted"))
                        )
                );
    }

}