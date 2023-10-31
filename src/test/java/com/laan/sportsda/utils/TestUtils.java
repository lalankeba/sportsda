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

    public RoleResponse addRole(String roleName, String roleDescription, List<String> permissionIds) {
        RoleAddRequest roleAddRequest = new RoleAddRequest();
        roleAddRequest.setName(roleName);
        roleAddRequest.setDescription(roleDescription);
        roleAddRequest.setPermissionIds(permissionIds);
        return roleService.addRole(roleAddRequest);
    }

    public RoleResponse addBasicRole() {
        return addRole(propertyUtil.getBasicRoleName(), "Sample role description", null);
    }

    public void deleteAllRoles() {
        List<RoleShortResponse> roleShortResponses = roleService.getRoles();
        List<String> ids = roleShortResponses.stream().map(RoleShortResponse::getId).toList();
        ids.forEach(roleService::deleteRole);
    }

    public List<FacultyResponse> addFaculties(List<String> facultyNames) {
        List<FacultyResponse> facultyResponses = new ArrayList<>();
        facultyNames.forEach(name -> facultyResponses.add(addFaculty(name)) );
        return facultyResponses;
    }

    public FacultyResponse addFaculty(String name) {
        FacultyAddRequest facultyAddRequest = new FacultyAddRequest();
        facultyAddRequest.setName(name);
        return facultyService.addFaculty(facultyAddRequest);
    }

    public FacultyResponse addBasicFaculty() {
        return addFaculty(propertyUtil.getBasicFacultyName());
    }

    public void deleteAllFaculties() {
        List<FacultyShortResponse> facultyShortResponses = facultyService.getFaculties();
        List<String> ids = facultyShortResponses.stream().map(FacultyShortResponse::getId).toList();
        ids.forEach(facultyService::deleteFaculty);
    }

    public MemberRegistrationResponse registerMember(String firstName, String lastName, String username, String password) {
        addRole(propertyUtil.getBasicRoleName(), "Sample role description", null);
        String facultyId = addBasicFaculty().getId();

        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest();
        memberRegistrationRequest.setFirstName(firstName);
        memberRegistrationRequest.setLastName(lastName);
        memberRegistrationRequest.setUsername(username);
        memberRegistrationRequest.setPassword(password);
        memberRegistrationRequest.setFacultyId(facultyId);

        return memberService.registerMember(memberRegistrationRequest);
    }

    public DepartmentResponse addDepartment(String departmentName, FacultyResponse facultyResponse) {
        DepartmentAddRequest departmentAddRequest = new DepartmentAddRequest();
        departmentAddRequest.setName(departmentName);
        departmentAddRequest.setFacultyId(facultyResponse.getId());
        return departmentService.addDepartment(departmentAddRequest);
    }

    public List<DepartmentResponse> addDepartments(List<String> departmentNames, FacultyResponse facultyResponse) {
        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        departmentNames.forEach(name -> departmentResponses.add(addDepartment(name, facultyResponse)) );
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

    public SportShortResponse addSport(String name) {
        SportAddRequest sportAddRequest = new SportAddRequest();
        sportAddRequest.setName(name);
        return sportService.addSport(sportAddRequest);
    }

    public void deleteAllSports() {
        List<SportShortResponse> sportShortResponses = sportService.getSports();
        List<String> ids = sportShortResponses.stream().map(SportShortResponse::getId).toList();
        ids.forEach(sportService::deleteSport);
    }

}
