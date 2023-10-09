package com.laan.sportsda.utils;

import com.laan.sportsda.dto.request.*;
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

    private final SportService sportService;

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
        List<RoleShortResponse> roleShortResponses = roleService.getRoles();
        List<String> ids = roleShortResponses.stream().map(RoleShortResponse::getId).toList();
        ids.forEach(roleService::deleteRole);
    }

    public List<FacultyResponse> createFaculties(List<String> facultyNames) {
        List<FacultyResponse> facultyResponses = new ArrayList<>();
        facultyNames.forEach(name -> facultyResponses.add(createFaculty(name)) );
        return facultyResponses;
    }

    public FacultyResponse createFaculty(String name) {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(name);
        return facultyService.addFaculty(facultyAddRequest);
    }

    public FacultyResponse createBasicFaculty() {
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

    public DepartmentResponse createDepartment(String departmentName, FacultyResponse facultyResponse) {
        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyResponse.getId());
        return departmentService.addDepartment(departmentAddRequest);
    }

    public List<DepartmentResponse> createDepartments(List<String> departmentNames, FacultyResponse facultyResponse) {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        departmentNames.forEach(name -> departmentResponses.add(createDepartment(name, facultyResponse)) );
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

    public SportResponse createSport(String name) {
        SportAddRequest sportAddRequest = new SportAddRequest();
        sportAddRequest.setName(name);
        return sportService.addSport(sportAddRequest);
    }

    public void deleteAllSports() {
        List<SportResponse> sportResponses = sportService.getSports();
        List<String> ids = sportResponses.stream().map(SportResponse::getId).toList();
        ids.forEach(sportService::deleteSport);
    }

}
