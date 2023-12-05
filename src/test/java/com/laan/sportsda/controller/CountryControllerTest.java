package com.laan.sportsda.controller;

import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.utils.CountryLimitingContentModifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountryLimitingContentModifier countryLimitingContentModifier;

    @Test
    void getCountries() throws Exception {

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.COUNTRIES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].cca3").exists())
                .andExpect(jsonPath("$.[*].cca2").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(limitContentAndPrettyPrint()),
                                responseFields(
                                        subsectionWithPath("[]").description("List of countries"),
                                        fieldWithPath("[*].commonName").description("Common name of the country"),
                                        fieldWithPath("[*].officialName").description("Official name of the country"),
                                        fieldWithPath("[*].cca2").description("Country code alpha 2"),
                                        fieldWithPath("[*].cca3").description("Country code alpha 3"),
                                        fieldWithPath("[*].ccn3").description("Country code numeric 3"),
                                        fieldWithPath("[*].capitals").description("Capital cities of the country"),
                                        fieldWithPath("[*].area").description("Area of the country in square kilo meters"),
                                        fieldWithPath("[*].population").description("Population of the country"),
                                        fieldWithPath("[*].region").description("Region of the country"),
                                        fieldWithPath("[*].subregion").description("Sub region of the country"),
                                        fieldWithPath("[*].continents").description("Continents of the country"),
                                        fieldWithPath("[*].flagPng").description("File path for png flag"),
                                        fieldWithPath("[*].flagSvg").description("File path for svg flag")
                                )
                        )
                );
    }

    private OperationPreprocessor limitContentAndPrettyPrint() {
        return new ContentModifyingOperationPreprocessor(countryLimitingContentModifier);
    }

}