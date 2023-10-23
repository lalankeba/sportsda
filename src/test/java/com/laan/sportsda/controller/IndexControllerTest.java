package com.laan.sportsda.controller;

import com.laan.sportsda.util.PathUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void init() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.INIT))
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void initInSi() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.INIT)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "si")
                )
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        )
                );
    }
}
