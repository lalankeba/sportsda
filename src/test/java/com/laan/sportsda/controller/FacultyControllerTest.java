package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
        FacultyResponse facultyResponse = testUtils.addFaculty("Applied Sciences");
        testUtils.addDepartments(Arrays.asList("Computer Science", "Statistics", "Food Science"), facultyResponse);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER, facultyResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
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
                        )
                );
    }

    @Test
    void getFacultyWithNoExistence() throws Exception {
        String id = "invalid_id";

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER, id)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
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

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER, id)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "si")
                )
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
        testUtils.addFaculties(Arrays.asList("Humanities and Social Sciences", "Management Studies and Commerce"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
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
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(facultyName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the faculty. Should be unique throughout the system.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the faculty"))
                                        .and(fieldWithPath("name").description("Name of the faculty"))
                                        .and(fieldWithPath("departments").description("Departments of the faculty"))
                        )
                );
    }

    @Test
    void addFacultyWithNullName() throws Exception {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(null);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
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
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
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
        testUtils.addFaculty(facultyName);

        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        String responseMessage = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
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
        FacultyResponse facultyResponse = testUtils.addFaculty("Graduate Studies");

        String updatedName = "Engineering";
        FacultyUpdateRequest facultyUpdateRequest = new FacultyUpdateRequest();
        facultyUpdateRequest.setName(updatedName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER, facultyResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(facultyUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(facultyResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the faculty that needs to be updated")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the faculty. Should be unique throughout the system.")),
                                responseFields(fieldWithPath("id").description("Id of the faculty"))
                                        .and(fieldWithPath("name").description("Updated name of the faculty"))
                                        .and(fieldWithPath("departments").description("Departments of the faculty"))
                        )
                );
    }

    @Test
    void deleteFaculty() throws Exception {
        FacultyResponse facultyResponse = testUtils.addFaculty("Designing");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER, facultyResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
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
        FacultyResponse facultyResponse = testUtils.addFaculty("Allied Health Sciences");
        List<DepartmentResponse> departmentResponses = testUtils.addDepartments(Arrays.asList("Nursing and Midwifery", "Medical Laboratory Sciences"), facultyResponse);

        Optional<DepartmentResponse> optionalDepartmentResponse = departmentResponses.stream().findFirst();
        String facultyId = null;
        if (optionalDepartmentResponse.isPresent()) {
            facultyId = optionalDepartmentResponse.get().getFacultyId();
        }

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + PathUtil.ID_PLACEHOLDER + PathUtil.DEPARTMENTS, facultyId)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty"))
                        ));
    }

}