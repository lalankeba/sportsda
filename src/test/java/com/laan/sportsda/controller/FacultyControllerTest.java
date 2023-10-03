package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.util.ConstantsUtil;
import com.laan.sportsda.util.MessagesUtil;
import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void initBefore() {
        testUtils.deleteAllDepartments();
        testUtils.deleteAllFaculties();
    }

    @AfterEach
    void initAfter() {
        testUtils.deleteAllDepartments();
        testUtils.deleteAllFaculties();
    }

    @Test
    void getFaculty() throws Exception {
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Applied Sciences");
        testUtils.createDepartments(Arrays.asList("Computer Science", "Statistics", "Food Science"), facultyShortResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", facultyShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty")),
                                responseFields(
                                        fieldWithPath("id").description("Id for the faculty"))
                                        .and(fieldWithPath("name").description("Name of the faculty"))
                                        .and(fieldWithPath("name").description("Name of the faculty"))
                                        .and(subsectionWithPath("departments").description("List of departments attached to the faculty"))
                                        .and(fieldWithPath("departments[].id").description("Id of the department"))
                                        .and(fieldWithPath("departments[].name").description("Name of the department"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );
    }

    @Test
    void getFacultyWithNoExistence() throws Exception {
        String id = "invalid_id";

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", id)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty"))
                        )
                );
    }

    @Test
    void getFacultyWithNoExistenceInSi() throws Exception {
        String id = "invalid_id";

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", id)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "si")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty"))
                        )
                );
    }

    @Test
    void getFaculties() throws Exception {
        testUtils.createFaculties(Arrays.asList("Humanities and Social Sciences", "Management Studies and Commerce"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
                .andExpect(jsonPath("$.[*].version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void addFaculty() throws Exception {
        String facultyName = "Medical Sciences";
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(facultyName)))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the faculty. Should be unique throughout the system.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the faculty"))
                                        .and(fieldWithPath("name").description("Name of the faculty"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );
    }

    @Test
    void addFacultyWithNullName() throws Exception {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(null);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(containsString(messageSource.getMessage(MessagesUtil.MANDATORY_FACULTY_NAME, null, LocaleContextHolder.getLocale()))))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void addFacultyWithInvalidName() throws Exception {
        String facultyName = "A";
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void addFacultyWithDuplicateName() throws Exception {
        String facultyName = "Applied Sciences";
        testUtils.createFaculty(facultyName);

        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        String responseMessage = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.detail").value(containsString(responseMessage)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void updateFaculty() throws Exception {
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Graduate Studies");

        String updatedName = "Engineering";
        FacultyUpdateRequest facultyUpdateRequest = new FacultyUpdateRequest();
        facultyUpdateRequest.setName(updatedName);
        facultyUpdateRequest.setVersion(facultyShortResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.FACULTIES + "/{id}", facultyShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(facultyShortResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the faculty that needs to be updated")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the faculty. Should be unique throughout the system."))
                                        .and(fieldWithPath("version").description("Version of the existing faculty")),
                                responseFields(fieldWithPath("id").description("Id of the faculty"))
                                        .and(fieldWithPath("name").description("Updated name of the faculty"))
                                        .and(fieldWithPath("departments").description("Departments of the faculty"))
                                        .and(fieldWithPath("version").description("Updated version number").optional())
                        )
                );
    }

    @Test
    void deleteFaculty() throws Exception {
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Designing");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.FACULTIES + "/{id}", facultyShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + "<token_data>")
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the faculty that needs to be deleted"))
                        )
                );
    }

    @Test
    void getDepartmentsByFaculty() throws Exception {
        FacultyShortResponse facultyShortResponse = testUtils.createFaculty("Allied Health Sciences");
        List<DepartmentResponse> departmentResponses = testUtils.createDepartments(Arrays.asList("Nursing and Midwifery", "Medical Laboratory Sciences"), facultyShortResponse);

        Optional<DepartmentResponse> optionalDepartmentResponse = departmentResponses.stream().findFirst();
        String facultyId = null;
        if (optionalDepartmentResponse.isPresent()) {
            facultyId = optionalDepartmentResponse.get().getFacultyId();
        }

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}/departments", facultyId)
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
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty"))
                        ));
    }

}