package com.laan.sportsda.utils;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.MemberRegistrationRequest;
import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.response.*;
import com.laan.sportsda.entity.MemberEntity;
import com.laan.sportsda.entity.SessionEntity;
import com.laan.sportsda.repository.MemberRepository;
import com.laan.sportsda.repository.SessionRepository;
import com.laan.sportsda.service.*;
import com.laan.sportsda.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestUtils {

    private final RoleService roleService;

    private final FacultyService facultyService;

    private final DepartmentService departmentService;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final SessionRepository sessionRepository;

    private final PropertyUtil propertyUtil;

    private final PermissionService permissionService;

    public RoleResponse createRole(String roleName, String roleDescription, List<String> permissionIds) {
        RoleAddRequest roleAddRequest = new RoleAddRequest();
        roleAddRequest.setName(roleName);
        roleAddRequest.setDescription(roleDescription);
        roleAddRequest.setPermissionIds(permissionIds);
        return roleService.addRole(roleAddRequest);
    }

    public RoleResponse createBasicRole() {
        return createRole(propertyUtil.getBasicRoleName(), "Sample role description", null);
    }

    public void deleteAllRoles() {
        List<RolesResponse> rolesResponses = roleService.getRoles();
        List<String> ids = rolesResponses.stream().map(RolesResponse::getId).toList();
        ids.forEach(roleService::deleteRole);
    }

    public List<FacultyShortResponse> createFaculties(List<String> facultyNames) {
        List<FacultyShortResponse> facultyShortResponses = new ArrayList<>();
        facultyNames.forEach(name -> facultyShortResponses.add(createFaculty(name)) );
        return facultyShortResponses;
    }

    public FacultyShortResponse createFaculty(String name) {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(name);
        return facultyService.addFaculty(facultyAddRequest);
    }

    public FacultyShortResponse createBasicFaculty() {
        return createFaculty(propertyUtil.getBasicFacultyName());
    }

    public void deleteAllFaculties() {
        List<FacultyShortResponse> facultyShortResponses = facultyService.getFaculties();
        List<String> ids = facultyShortResponses.stream().map(FacultyShortResponse::getId).toList();
        ids.forEach(facultyService::deleteFaculty);
    }

    public MemberRegistrationResponse createMember(String firstName, String lastName, String username, String password) {
        createRole(propertyUtil.getBasicRoleName(), "Sample role description", null);
        String facultyId = createBasicFaculty().getId();

        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest();
        memberRegistrationRequest.setFirstName(firstName);
        memberRegistrationRequest.setLastName(lastName);
        memberRegistrationRequest.setUsername(username);
        memberRegistrationRequest.setPassword(password);
        memberRegistrationRequest.setFacultyId(facultyId);

        return memberService.registerMember(memberRegistrationRequest);
    }

    public DepartmentResponse createDepartment(String departmentName, FacultyShortResponse facultyShortResponse) {
        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyShortResponse.getId());
        return departmentService.addDepartment(departmentAddRequest);
    }

    public List<DepartmentResponse> createDepartments(List<String> departmentNames, FacultyShortResponse facultyShortResponse) {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        departmentNames.forEach(name -> departmentResponses.add(createDepartment(name, facultyShortResponse)) );
        return departmentResponses;
    }

    public void deleteAllDepartments() {
        List<DepartmentResponse> departmentResponses = departmentService.getDepartments();
        List<String> ids = departmentResponses.stream().map(DepartmentResponse::getId).toList();
        ids.forEach(departmentService::deleteDepartment);
    }

    public void deleteAllSessions() {
        List<SessionEntity> sessionEntities = sessionRepository.findAll();
        List<String> sessionIds = sessionEntities.stream().map(SessionEntity::getId).toList();
        sessionIds.forEach(sessionRepository::deleteById);
    }

    public void deleteAllMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<String> memberIds = memberEntities.stream().map(MemberEntity::getId).toList();
        memberIds.forEach(memberRepository::deleteById);
    }

    public List<PermissionResponse> getAllPermissions() {
        return permissionService.getPermissions();
    }

}
