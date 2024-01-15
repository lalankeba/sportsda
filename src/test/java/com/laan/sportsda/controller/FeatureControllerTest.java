package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.FeatureAddRequest;
import com.laan.sportsda.dto.request.FeatureUpdateRequest;
import com.laan.sportsda.dto.response.FeatureResponse;
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
class FeatureControllerTest {

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
    void getFeature() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Badminton");
        FeatureResponse featureResponse = testUtils.addFeatureWithNumericValue("Height", FeatureValueType.INTEGER, "50", "200", "cm", sportShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FEATURES + PathUtil.ID_PLACEHOLDER, featureResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the feature")),
                                responseFields(
                                        fieldWithPath("id").description("Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                                        .and(fieldWithPath("featureValueType").description("Type of the value"))
                                        .and(fieldWithPath("minValue").description("Minimum value that is allowed for the feature"))
                                        .and(fieldWithPath("maxValue").description("Maximum value that is allowed for the feature"))
                                        .and(fieldWithPath("measurement").description("Measurement of the value"))
                                        .and(fieldWithPath("sportId").description("Sport id of the feature"))
                                        .and(fieldWithPath("possibleValues").description("Possible values for the feature"))
                        )
                );
    }

    @Test
    void getFeatures() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Cricket");
        testUtils.addFeatureWithNumericValue("Height", FeatureValueType.INTEGER, "50", "200", "cm", sportShortResponse.getId());
        testUtils.addFeatureWithNumericValue("Weight", FeatureValueType.DECIMAL, "30", "200", "kg", sportShortResponse.getId());
        testUtils.addFeatureWithFixedValues("Player Type", Arrays.asList("Batsman", "Spinner", "Fast bowler", "Wicket keeper"), sportShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.FEATURES)
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
    void addFeature() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Carrom");
        String featureName = "Game Type";
        FeatureAddRequest featureAddRequest = new FeatureAddRequest();
        featureAddRequest.setName(featureName);
        featureAddRequest.setFeatureValueType(FeatureValueType.FIXED_VALUE);
        featureAddRequest.setPossibleValues(Arrays.asList("Single", "Double"));
        featureAddRequest.setSportId(sportShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.FEATURES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(featureAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(featureName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("Name to be saved for the feature. Should be unique per sport throughout the system."))
                                        .and(fieldWithPath("featureValueType").description("Type of the value to the feature."))
                                        .and(fieldWithPath("minValue").description("Minimum value that is allowed for the feature"))
                                        .and(fieldWithPath("maxValue").description("Maximum value that is allowed for the feature"))
                                        .and(fieldWithPath("measurement").description("Measurement of the value"))
                                        .and(fieldWithPath("sportId").description("Sport id of the feature"))
                                        .and(fieldWithPath("possibleValues").description("Possible values for the feature")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the sport"))
                                        .and(fieldWithPath("name").description("Name of the sport"))
                                        .and(fieldWithPath("featureValueType").description("Type of the value"))
                                        .and(fieldWithPath("minValue").description("Minimum value that is allowed for the feature"))
                                        .and(fieldWithPath("maxValue").description("Maximum value that is allowed for the feature"))
                                        .and(fieldWithPath("measurement").description("Measurement of the value"))
                                        .and(fieldWithPath("sportId").description("Sport id of the feature"))
                                        .and(subsectionWithPath("possibleValues").description("Possible values for the feature"))
                                        .and(fieldWithPath("possibleValues[].id").description("Id of the possible value"))
                                        .and(fieldWithPath("possibleValues[].attributeValue").description("Attribute name of the possible value"))
                        )
                );
    }

    @Test
    void updateFeature() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Badminton");
        FeatureResponse featureResponse = testUtils.addFeatureWithNumericValue("Height", FeatureValueType.INTEGER, "50", "200", "cm", sportShortResponse.getId());

        String updatedName = "Weight";
        FeatureUpdateRequest featureUpdateRequest = new FeatureUpdateRequest();
        featureUpdateRequest.setName(updatedName);
        featureUpdateRequest.setFeatureValueType(FeatureValueType.DECIMAL);
        featureUpdateRequest.setMinValue("40");
        featureUpdateRequest.setMaxValue("250");
        featureUpdateRequest.setMeasurement("kg");

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.FEATURES + PathUtil.ID_PLACEHOLDER, featureResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(featureUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(featureResponse.getId())))
                .andExpect(jsonPath("$.name").value(containsString(updatedName)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the sport that needs to be updated")),
                                requestFields(
                                        fieldWithPath("name").description("Name to be updated for the feature. Should be unique per sport throughout the system."))
                                        .and(fieldWithPath("featureValueType").description("Type of the value to the feature"))
                                        .and(fieldWithPath("minValue").description("Minimum value that is allowed for the feature"))
                                        .and(fieldWithPath("maxValue").description("Maximum value that is allowed for the feature"))
                                        .and(fieldWithPath("measurement").description("Measurement of the value"))
                                        .and(fieldWithPath("possibleValues").description("Possible values for the feature")),
                                responseFields(
                                        fieldWithPath("id").description("Id of the sport"))
                                        .and(fieldWithPath("name").description("Updated name of the feature"))
                                        .and(fieldWithPath("featureValueType").description("Type of the value to the feature"))
                                        .and(fieldWithPath("minValue").description("Minimum value that is allowed for the feature"))
                                        .and(fieldWithPath("maxValue").description("Maximum value that is allowed for the feature"))
                                        .and(fieldWithPath("measurement").description("Measurement of the value"))
                                        .and(fieldWithPath("sportId").description("Sport id of the feature"))
                                        .and(fieldWithPath("possibleValues").description("Possible values for the feature"))
                        )
                );
    }

    @Test
    void deleteFeature() throws Exception {
        SportShortResponse sportShortResponse = testUtils.addSport("Chess");
        FeatureResponse featureResponse = testUtils.addFeatureWithNumericValue("Height", FeatureValueType.INTEGER, "50", "200", "cm", sportShortResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.FEATURES + PathUtil.ID_PLACEHOLDER, featureResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the feature that needs to be deleted"))
                        )
                );
    }
}
