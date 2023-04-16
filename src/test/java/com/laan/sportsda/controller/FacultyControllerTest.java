package com.laan.sportsda.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.util.PathUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFaculty() throws Exception {
        String id = createFaculty("Applied Sciences").getId();

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FACULTIES + "/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
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

        this.mockMvc.perform(get(PathUtil.FACULTIES).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].name").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void addFaculty() throws Exception {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName("Medical Sciences");

        this.mockMvc.perform(post(PathUtil.FACULTIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyAddRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
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
    void updateFaculty() throws Exception {
        FacultyResponse facultyResponse = createFaculty("Graduate Studies");

        String updatedName = "Allied Health Sciences";
        FacultyUpdateRequest facultyUpdateRequest = new FacultyUpdateRequest();
        facultyUpdateRequest.setName(updatedName);
        facultyUpdateRequest.setVersion(facultyResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.FACULTIES + "/{id}", facultyResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(facultyUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
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

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.FACULTIES + "/{id}", facultyResponse.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the faculty that needs to be deleted"))
                        )
                );
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
}