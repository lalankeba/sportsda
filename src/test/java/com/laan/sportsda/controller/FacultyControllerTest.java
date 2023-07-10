package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.util.MessagesUtil;
import com.laan.sportsda.util.PathUtil;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @AfterEach
    void init() throws Exception {
        deleteAllDepartments();
        deleteAllFaculties();
    }

    @Test
    void getFaculty() throws Exception {
        String id = createFaculty("Applied Sciences").getId();

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the faculty"))
                        )
                );
    }

    @Test
    void getFacultyWithNoExistence() throws Exception {
        String id = "invalid_id";

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", id))
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
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "si"))
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
        createFaculty("Humanities and Social Sciences");
        createFaculty("Management Studies and Commerce");

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
        String facultyName= "Medical Sciences";
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
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
        String facultyName= "A";
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
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
        String facultyName= "Applied Sciences";
        createFaculty(facultyName);

        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(facultyName);

        String responseMessage = String.format(messageSource.getMessage(MessagesUtil.DUPLICATE_FACULTY_EXCEPTION, null, LocaleContextHolder.getLocale()), facultyName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FACULTIES)
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
        FacultyResponse facultyResponse = createFaculty("Graduate Studies");

        String updatedName = "Engineering";
        FacultyUpdateRequest facultyUpdateRequest = new FacultyUpdateRequest();
        facultyUpdateRequest.setName(updatedName);
        facultyUpdateRequest.setVersion(facultyResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.FACULTIES + "/{id}", facultyResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(facultyResponse.getId())))
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
                                        .and(fieldWithPath("version").description("Updated version number").optional())
                        )
                );
    }

    @Test
    void deleteFaculty() throws Exception {
        FacultyResponse facultyResponse = createFaculty("Designing");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.FACULTIES + "/{id}", facultyResponse.getId()))
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
        List<DepartmentResponse> departmentResponses = createDepartments(Arrays.asList("Nursing and Midwifery", "Medical Laboratory Sciences"), "Allied Health Sciences");
        Optional<DepartmentResponse> optionalDepartmentResponse = departmentResponses.stream().findFirst();
        String facultyId = null;
        if (optionalDepartmentResponse.isPresent()) {
            facultyId = optionalDepartmentResponse.get().getFacultyId();
        }

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}/departments", facultyId))
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

        if (facultyResponses != null) {
            facultyResponses.forEach(facultyResponse -> {
                try {
                    this.mockMvc.perform(delete(PathUtil.FACULTIES + "/{id}", facultyResponse.getId()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
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
}