package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.SportAddRequest;
import com.laan.sportsda.dto.request.SportUpdateRequest;
import com.laan.sportsda.dto.response.SportShortResponse;
import com.laan.sportsda.enums.FeatureValueType;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
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
        SportShortResponse sportShortResponse = testUtils.addSport("Badminton");
        testUtils.addFeatureWithNumericValue("Height", FeatureValueType.INTEGER, "50", "200", "cm", sportShortResponse.getId());
        testUtils.addFeatureWithNumericValue("Weight", FeatureValueType.DECIMAL, "30", "200", "kg", sportShortResponse.getId());
        testUtils.addFeatureWithFixedValues("Game Type", Arrays.asList("Single", "Double", "Mix double"), sportShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the sport")),
                                responseFields(
                                        fieldWithPath("id").description("Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                                        .and(subsectionWithPath("features").description("Features of the sport"))
                                        .and(fieldWithPath("features[].id").description("Id of the feature"))
                                        .and(fieldWithPath("features[].name").description("Name of the feature"))
                                        .and(fieldWithPath("features[].featureValueType").description("Value type of the feature"))
                        )
                );
    }

    @Test
    void getSports() throws Exception {
        testUtils.addSport("Cricket");
        testUtils.addSport("Carrom");

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.SPORTS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].name").exists())
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(sportName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the sport. Should be unique throughout the system.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                        )
                );
    }

    @Test
    void updateSport() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Soccer");

        String updatedName = "Football";
        SportUpdateRequest sportUpdateRequest = new SportUpdateRequest();
        sportUpdateRequest.setName(updatedName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(sportUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(sportShortResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the sport that needs to be updated")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the sport. Should be unique throughout the system.")),
                                responseFields(fieldWithPath("id").description("Id of the sport"))
                                        .and(fieldWithPath("name").description("Updated name of the sport"))
                        )
                );
    }

    @Test
    void deleteSport() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Chess");

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.SPORTS + PathUtil.ID_PLACEHOLDER, sportShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the sport that needs to be deleted"))
                        )
                );
    }
}
