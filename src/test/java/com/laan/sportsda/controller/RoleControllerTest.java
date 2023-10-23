package com.laan.sportsda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.PermissionResponse;
import com.laan.sportsda.dto.response.RoleResponse;
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

import java.util.List;

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
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void initBefore() {
        testUtils.deleteAllRoles();
    }

    @AfterEach
    void initAfter() {
        testUtils.deleteAllRoles();
    }

    @Test
    void getRole() throws Exception {
        List<PermissionResponse> permissionResponses = testUtils.getAllPermissions();
        List<String> permissionIds = permissionResponses.stream().map(PermissionResponse::getId).limit(3).toList();
        RoleResponse roleResponse = testUtils.createRole("Moderator", "Can moderate tasks", permissionIds);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.ROLES + PathUtil.ID_PLACEHOLDER, roleResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.description").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("id of the role")),
                                responseFields(fieldWithPath("id").description("Id of the role"))
                                        .and(fieldWithPath("name").description("Name of the role"))
                                        .and(fieldWithPath("description").description("Description of the role"))
                                        .and(subsectionWithPath("permissions").description("List of permissions granted for the role"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );
    }

    @Test
    void getRoles() throws Exception {
        testUtils.createRole("Moderator", "Can moderate tasks", null);
        testUtils.createRole("Helper", "Can help to moderate tasks", null);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get(PathUtil.ROLES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].description").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void addRole() throws Exception {
        List<PermissionResponse> permissionResponses = testUtils.getAllPermissions();
        List<String> permissionIds = permissionResponses.stream().map(PermissionResponse::getId).limit(2).toList();

        String roleName = "Moderator", roleDescription = "Can moderate tasks";
        RoleAddRequest roleAddRequest = new RoleAddRequest();
        roleAddRequest.setName(roleName);
        roleAddRequest.setDescription(roleDescription);
        roleAddRequest.setPermissionIds(permissionIds);

        this.mockMvc.perform(RestDocumentationRequestBuilders.post(PathUtil.ROLES)
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(roleAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(roleName)))
                .andExpect(jsonPath("$.description").value(containsString(roleDescription)))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("Name to be saved for the role. Should be unique throughout the system."))
                                        .and(fieldWithPath("description").description("Description to be saved for the role"))
                                        .and(fieldWithPath("permissionIds").description("List of permission IDs to be attached with role")),
                                responseFields(
                                        fieldWithPath("id").description("Created Id for the role"))
                                        .and(fieldWithPath("name").description("Name of the role"))
                                        .and(fieldWithPath("description").description("Description for the role"))
                                        .and(subsectionWithPath("permissions").description("List of permissions granted for the role"))
                                        .and(fieldWithPath("version").description("Version number").optional())
                        )
                );

    }

    @Test
    void updateRole() throws Exception {
        List<PermissionResponse> permissionResponses = testUtils.getAllPermissions();
        RoleResponse roleResponse = testUtils.createRole("Moderator", "Can moderate tasks", null);

        String updatedRoleName = "Higher Moderator", updatedRoleDescription = "Can moderate many more tasks";
        List<String> updatedPermissionIds = permissionResponses.stream().map(PermissionResponse::getId).skip(1).limit(3).toList();
        RoleUpdateRequest roleUpdateRequest = new RoleUpdateRequest();
        roleUpdateRequest.setName(updatedRoleName);
        roleUpdateRequest.setDescription(updatedRoleDescription);
        roleUpdateRequest.setPermissionIds(updatedPermissionIds);
        roleUpdateRequest.setVersion(roleResponse.getVersion());

        this.mockMvc.perform(RestDocumentationRequestBuilders.put(PathUtil.ROLES + PathUtil.ID_PLACEHOLDER, roleResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(roleUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(containsString(updatedRoleName)))
                .andExpect(jsonPath("$.description").value(containsString(updatedRoleDescription)))
                .andExpect(jsonPath("$.version").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("name").description("Name to be updated for the role. Should be unique throughout the system."))
                                        .and(fieldWithPath("description").description("Description to be updated for the role"))
                                        .and(fieldWithPath("permissionIds").description("Updated list of permission IDs to be attached with role"))
                                        .and(fieldWithPath("version").description("Version of the existing role")),
                                responseFields(
                                        fieldWithPath("id").description("Id for the role"))
                                        .and(fieldWithPath("name").description("Updated name of the role"))
                                        .and(fieldWithPath("description").description("Updated description for the role"))
                                        .and(subsectionWithPath("permissions").description("Updated list of permissions granted for the role"))
                                        .and(fieldWithPath("version").description("New version number").optional())
                        )
                );
    }

    @Test
    void deleteRole() throws Exception {
        List<PermissionResponse> permissionResponses = testUtils.getAllPermissions();
        List<String> permissionIds = permissionResponses.stream().map(PermissionResponse::getId).limit(2).toList();
        RoleResponse roleResponse = testUtils.createRole("Moderator", "Can moderate tasks", permissionIds);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete(PathUtil.ROLES + PathUtil.ID_PLACEHOLDER, roleResponse.getId())
                        .header(ConstantsUtil.AUTH_TOKEN_HEADER, ConstantsUtil.AUTH_TOKEN_PREFIX + ConstantsUtil.TOKEN_VALUE_SAMPLE)
                )
                .andExpect(status().isAccepted())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the role that needs to be deleted"))
                        )
                );
    }
}
