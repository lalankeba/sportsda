package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.LoginRequest;
import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.request.MemberRoleUpdateRequest;
import com.laan.sportsda.dto.request.MemberUpdateRequest;
import com.laan.sportsda.dto.response.*;
import com.laan.sportsda.enums.PermissionDescription;
import com.laan.sportsda.util.ConstantsUtil;
import com.laan.sportsda.util.PathUtil;
import com.laan.sportsda.util.PropertyUtil;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.*;
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

    @Autowired
    private PropertyUtil propertyUtil;

    private final static String ADMIN_DESCRIPTION = "Admin has all permissions";

    private final static String BASIC_DESCRIPTION = "Student has limited permissions";

    @BeforeEach
    void initBefore(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        testUtils.deleteAllSessions();
        testUtils.deleteAllMembers();
        testUtils.deleteAllRoles();
        testUtils.deleteAllDepartments();
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
        testUtils.deleteAllDepartments();
        testUtils.deleteAllFaculties();
    }

    @Test
    void register() throws Exception {
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();

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
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String firstName = "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember(firstName, lastName, username, password, facultyId);

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
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String firstName = "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember(firstName, lastName, username, password, facultyId);

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

    @Test
    void getMembers() throws Exception {
        PermissionResponse permissionResponse = testUtils.getPermission(PermissionDescription.GET_MEMBERS);
        List<String> permissionIds = Collections.singletonList(permissionResponse.getId());
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, permissionIds);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember("John", "Doe", username, password, facultyId);
        testUtils.registerMember("Mary", "Anne", "mary.anne@testing.com", "efgh4321", facultyId);

        LoginResponse loginResponse = loginMember(username, password);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.MEMBERS)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].firstName").exists())
                .andExpect(jsonPath("$.[*].lastName").exists())
                .andExpect(jsonPath("$.[*].username").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void getMember() throws Exception {
        PermissionResponse permissionResponse = testUtils.getPermission(PermissionDescription.GET_MEMBER);
        List<String> permissionIds = Collections.singletonList(permissionResponse.getId());
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, permissionIds);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String firstName= "Mary", lastName = "Anne", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember("John", "Doe", username, password, facultyId);
        MemberRegistrationResponse memberRegistrationResponse = testUtils.registerMember(firstName, lastName, "mary.anne@testing.com", "efgh4321", facultyId);

        LoginResponse loginResponse = loginMember(username, password);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.MEMBERS + PathUtil.MEMBER + PathUtil.ID_PLACEHOLDER, memberRegistrationResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(containsString(firstName)))
                .andExpect(jsonPath("$.lastName").value(containsString(lastName)))
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void getCurrentMember() throws Exception {
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String firstName= "John", lastName = "Doe", username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember(firstName, lastName, username, password, facultyId);

        LoginResponse loginResponse = loginMember(username, password);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.MEMBERS + PathUtil.CURRENT)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(containsString(firstName)))
                .andExpect(jsonPath("$.lastName").value(containsString(lastName)))
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void updateCurrentMember() throws Exception {
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);

        FacultyResponse facultyResponse = testUtils.addFaculty("Humanities and Social Sciences");
        List<DepartmentResponse> departmentResponses = testUtils.addDepartments(Arrays.asList("Anthropology", "Economics"), facultyResponse);

        String username = "lucy.gill@testing.com", password = "abcd1234";
        testUtils.registerMember("Lucy", "Gill", username, password, facultyResponse.getId());

        LoginResponse loginResponse = loginMember(username, password);

        String firstName= "Mary", middleName = "Nik", lastName = "Sanders", nic = "199905271234", phone = "0771234567",
                universityEmail = "sanders@sjp.ac.lk", personalEmail = "sandersmary321@gmail.com", address = "123, Nugegoda Place, Wijerama", district = "Colombo";

        Calendar calendar = Calendar.getInstance();
        calendar.set(2001, Calendar.APRIL, 21);
        Date birthDate = calendar.getTime();

        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.setFirstName(firstName);
        memberUpdateRequest.setMiddleName(middleName);
        memberUpdateRequest.setLastName(lastName);
        memberUpdateRequest.setDateOfBirth(birthDate);
        memberUpdateRequest.setNic(nic);
        memberUpdateRequest.setPhone(phone);
        memberUpdateRequest.setUniversityEmail(universityEmail);
        memberUpdateRequest.setPersonalEmail(personalEmail);
        memberUpdateRequest.setAddress(address);
        memberUpdateRequest.setDistrict(district);
        memberUpdateRequest.setFacultyId(facultyResponse.getId());
        memberUpdateRequest.setDepartmentIds(departmentResponses.stream().map(DepartmentResponse::getId).toList());
        memberUpdateRequest.setVersion(0L);

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.MEMBERS + PathUtil.CURRENT)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(memberUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(containsString(firstName)))
                .andExpect(jsonPath("$.middleName").value(containsString(middleName)))
                .andExpect(jsonPath("$.lastName").value(containsString(lastName)))
                .andExpect(jsonPath("$.nic").value(containsString(nic)))
                .andExpect(jsonPath("$.phone").value(containsString(phone)))
                .andExpect(jsonPath("$.universityEmail").value(containsString(universityEmail)))
                .andExpect(jsonPath("$.personalEmail").value(containsString(personalEmail)))
                .andExpect(jsonPath("$.address").value(containsString(address)))
                .andExpect(jsonPath("$.username").value(containsString(username)))
                .andExpect(jsonPath("$.district").value(containsString(district)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("firstName").description("First name to be updated for the member."))
                                        .and(fieldWithPath("middleName").description("Middle name to be updated for the member."))
                                        .and(fieldWithPath("lastName").description("Last name to be updated for the member."))
                                        .and(fieldWithPath("dateOfBirth").description("Date of birth to be updated for the member."))
                                        .and(fieldWithPath("nic").description("National Identity Card number to be updated for the member."))
                                        .and(fieldWithPath("phone").description("Phone number to be updated for the member."))
                                        .and(fieldWithPath("universityEmail").description("University email to be updated for the member."))
                                        .and(fieldWithPath("personalEmail").description("Personal email to be updated for the member."))
                                        .and(fieldWithPath("address").description("Address to be updated for the member."))
                                        .and(fieldWithPath("district").description("District to be updated for the member."))
                                        .and(fieldWithPath("facultyId").description("Faculty id which is attached to the member."))
                                        .and(fieldWithPath("departmentIds").description("Department ids which is attached to the member."))
                                        .and(fieldWithPath("version").description("Version of the existing member"))
                        )
                );
    }

    @Test
    void updateMemberRole() throws Exception {
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        // create admin role
        PermissionResponse permissionResponse = testUtils.getPermission(PermissionDescription.UPDATE_MEMBER_ROLE);
        List<String> permissionIds = Collections.singletonList(permissionResponse.getId());
        RoleResponse adminRoleResponse = testUtils.addRole(propertyUtil.getAdminRoleName(), ADMIN_DESCRIPTION, permissionIds);

        // create basic role
        testUtils.addRole(propertyUtil.getBasicRoleName(), "Student has less permissions", null);

        String ownerUsername = "john.doe@testing.com", ownerPassword = "abcd1234";
        MemberRegistrationResponse ownerRegistrationResponse = testUtils.registerMember("John", "Doe", ownerUsername, ownerPassword, facultyId);

        String firstName= "Lucy", lastName = "Anne", username = "lucy.anne@testing.com", password = "efgh4321";
        MemberRegistrationResponse memberRegistrationResponse = testUtils.registerMember(firstName, lastName, username, password, facultyId);

        testUtils.updateMemberRole(ownerRegistrationResponse.getId(), adminRoleResponse.getId(), username);

        LoginResponse loginResponse = loginMember(ownerUsername, ownerPassword);

        // create new role
        RoleResponse newRoleResponse = testUtils.addRole("INSTRUCTOR", "Instructor has much permissions", null);
        MemberRoleUpdateRequest memberRoleUpdateRequest = new MemberRoleUpdateRequest();
        memberRoleUpdateRequest.setRoleId(newRoleResponse.getId());

        this.mockMvc.perform(RestDocumentationRequestBuilders.patch(PathUtil.MEMBERS + PathUtil.MEMBER + PathUtil.ID_PLACEHOLDER, memberRegistrationResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(memberRoleUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(containsString(firstName)))
                .andExpect(jsonPath("$.lastName").value(containsString(lastName)))
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void playSport() throws Exception {
        testUtils.addRole(propertyUtil.getBasicRoleName(), BASIC_DESCRIPTION, null);
        String facultyId = testUtils.addFaculty(propertyUtil.getBasicFacultyName()).getId();
        String username = "john.doe@testing.com", password = "abcd1234";
        testUtils.registerMember("John", "Doe", username, password, facultyId);

        LoginResponse loginResponse = loginMember(username, password);

        String sportName = "Cricket";
        SportShortResponse sportShortResponse = testUtils.addSport(sportName);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.MEMBERS + PathUtil.CURRENT + PathUtil.PLAY_SPORT + PathUtil.ID_PLACEHOLDER, sportShortResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + loginResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sports.[*].id").exists())
                .andExpect(jsonPath("$.sports.[0].id").value(containsString(sportShortResponse.getId())))
                .andExpect(jsonPath("$.sports.[0].name").value(containsString(sportName)))
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    private LoginResponse loginMember(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post(PathUtil.MEMBERS + PathUtil.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest))
        ).andReturn();

        byte[] responseAsArray = mvcResult.getResponse().getContentAsByteArray();
        return objectMapper.readValue(responseAsArray, LoginResponse.class);
    }

}
