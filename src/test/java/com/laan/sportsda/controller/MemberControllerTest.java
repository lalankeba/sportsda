package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.LoginRequest;
import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.response.LoginResponse;
import com.laan.sportsda.util.ConstantsUtil;
import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@Slf4j
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void initBefore(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        testUtils.deleteAllSessions();
        testUtils.deleteAllMembers();
        testUtils.deleteAllRoles();
        testUtils.deleteAllFaculties();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @AfterEach
    void initAfter() {
        testUtils.deleteAllSessions();
        testUtils.deleteAllMembers();
        testUtils.deleteAllRoles();
        testUtils.deleteAllFaculties();
    }

    @Test
    void register() throws Exception {
        testUtils.addBasicRole();
        String facultyId = testUtils.addBasicFaculty().getId();

        String firstName = "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest();
        memberRegistrationRequest.setFirstName(firstName);
        memberRegistrationRequest.setLastName(lastName);
        memberRegistrationRequest.setUsername(username);
        memberRegistrationRequest.setPassword(password);
        memberRegistrationRequest.setFacultyId(facultyId);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.MEMBERS + PathUtil.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(memberRegistrationRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(containsString(firstName)))
                .andExpect(jsonPath("$.lastName").value(containsString(lastName)))
                .andExpect(jsonPath("$.username").value(containsString(username)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("firstName").description("First name to be saved for the member."))
                                        .and(fieldWithPath("lastName").description("Last name to be saved for the member."))
                                        .and(fieldWithPath("username").description("Username to be saved for the member. Should be a valid email which is unique through out the system."))
                                        .and(fieldWithPath("password").description("Password to be saved for the member."))
                                        .and(fieldWithPath("facultyId").description("Faculty id which is attached to the member.")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the member"))
                                        .and(fieldWithPath("firstName").description("First name of the member"))
                                        .and(fieldWithPath("lastName").description("Last name of the member"))
                                        .and(fieldWithPath("username").description("Username of the member"))
                                        .and(fieldWithPath("faculty").description("Attached faculty of the member"))
                                        .and(fieldWithPath("faculty.id").description("Id of the faculty"))
                                        .and(fieldWithPath("faculty.name").description("Name of the faculty"))
                                        .and(fieldWithPath("accountNonExpired").description("Account non expired of the member"))
                                        .and(fieldWithPath("accountNonLocked").description("Account non locked of the member"))
                                        .and(fieldWithPath("credentialsNonExpired").description("Credentials non expired of the member"))
                                        .and(fieldWithPath("enabled").description("Enabled of the member"))
                        )
                );
    }

    @Test
    void login() throws Exception {
        String firstName = "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember(firstName, lastName, username, password);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.MEMBERS + PathUtil.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tokenType").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("username").description("Username of the member"))
                                        .and(fieldWithPath("password").description("Password of the member")),
                                responseFields(
                                        fieldWithPath("token").description("Generated token for the session"))
                                        .and(fieldWithPath("tokenType").description("Type of the token"))
                        )
                );
    }

    @Test
    void logout() throws Exception {
        String firstName = "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember(firstName, lastName, username, password);
        LoginResponse loginResponse = loginMember(username, password);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.MEMBERS + PathUtil.LOGOUT)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    private LoginResponse loginMember(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        MvcResult mvcResult = this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.MEMBERS + PathUtil.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest))
        ).andReturn();

        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        return objectMapper.readValue(responseAsArray, LoginResponse.class);
    }

}