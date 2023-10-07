package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportResponse;
import com.laan.sportsda.util.ConstantsUtil;
import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
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
@Slf4j
class SportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void initBefore() {
        testUtils.deleteAllSports();
    }

    @AfterEach
    void initAfter() {
        testUtils.deleteAllSports();
    }

    @Test
    void getSport() throws Exception {
        SportResponse sportResponse = testUtils.createSport("Badminton");

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the sport")),
                                responseFields(
                                        fieldWithPath("id").description("Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );
    }

    @Test
    void getSports() throws Exception {
        testUtils.createSport("Cricket");
        testUtils.createSport("Carrom");

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.SPORTS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
                .andExpect(jsonPath("$.[*].version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void addSport() throws Exception {
        String sportName = "Rugby";
        SportAddRequest sportAddRequest = new SportAddRequest();
        sportAddRequest.setName(sportName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.SPORTS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(sportAddRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(sportName)))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the sport. Should be unique throughout the system.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );
    }

    @Test
    void updateSport() throws Exception {
        SportResponse sportResponse = testUtils.createSport("Soccer");

        String updatedName = "Football";
        SportUpdateRequest sportUpdateRequest = new SportUpdateRequest();
        sportUpdateRequest.setName(updatedName);
        sportUpdateRequest.setVersion(sportResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(sportUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(sportResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the sport that needs to be updated")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the sport. Should be unique throughout the system."))
                                        .and(fieldWithPath("version").description("Version of the existing sport")),
                                responseFields(fieldWithPath("id").description("Id of the sport"))
                                        .and(fieldWithPath("name").description("Updated name of the sport"))
                                        .and(fieldWithPath("version").description("Updated version number").optional())
                        )
                );
    }

    @Test
    void deleteSport() throws Exception {
        SportResponse sportResponse = testUtils.createSport("Chess");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andDo(print())
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the sport that needs to be deleted"))
                        )
                );
    }
}
