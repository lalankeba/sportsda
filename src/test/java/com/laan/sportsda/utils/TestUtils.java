package com.laan.sportsda.utils;

import com.laan.sportsda.dto.request.*;
import com.laan.sportsda.dto.response.*;
import com.laan.sportsda.entity.MemberEntity;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.entity.SessionEntity;
import com.laan.sportsda.enums.FeatureValueType;
import com.laan.sportsda.enums.PermissionDescription;
import com.laan.sportsda.mapper.PermissionMapper;
import com.laan.sportsda.repository.MemberRepository;
import com.laan.sportsda.repository.PermissionRepository;
import com.laan.sportsda.repository.SessionRepository;
import com.laan.sportsda.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestUtils {

    private final RoleService roleService;

    private final FacultyService facultyService;

    private final DepartmentService departmentService;

    private final MemberService memberService;

    private final SportService sportService;

    private final FeatureService featureService;

    private final MemberRepository memberRepository;

    private final SessionRepository sessionRepository;

    private final PermissionService permissionService;

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public RoleResponse addRole(String roleName, String roleDescription, List<String> permissionIds) {
        RoleAddRequest roleAddRequest = new RoleAddRequest();
        roleAddRequest.setName(roleName);
        roleAddRequest.setDescription(roleDescription);
        roleAddRequest.setPermissionIds(permissionIds);
        return roleService.addRole(roleAddRequest);
    }

    public PermissionResponse getPermission(PermissionDescription permissionDescription) {
        Optional<PermissionEntity> optionalPermissionEntity = permissionRepository.findByDescription(permissionDescription);
        PermissionEntity permissionEntity = null;
        if (optionalPermissionEntity.isPresent()) {
            permissionEntity = optionalPermissionEntity.get();
        }
        return permissionMapper.mapEntityToResponse(permissionEntity);
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

    public void deleteAllFaculties() {
        List<FacultyShortResponse> facultyShortResponses = facultyService.getFaculties();
        List<String> ids = facultyShortResponses.stream().map(FacultyShortResponse::getId).toList();
        ids.forEach(facultyService::deleteFaculty);
    }

    public MemberRegistrationResponse registerMember(String firstName, String lastName, String username, String password, String facultyId) {
        MemberRegistrationRequest memberRegistrationRequest = new MemberRegistrationRequest();
        memberRegistrationRequest.setFirstName(firstName);
        memberRegistrationRequest.setLastName(lastName);
        memberRegistrationRequest.setUsername(username);
        memberRegistrationRequest.setPassword(password);
        memberRegistrationRequest.setFacultyId(facultyId);

        return memberService.registerMember(memberRegistrationRequest);
    }

    public MemberResponse updateMemberRole(String memberId, String roleId, String currentUsername) {
        MemberRoleUpdateRequest ownerRoleUpdateRequest = new MemberRoleUpdateRequest();
        ownerRoleUpdateRequest.setRoleId(roleId);
        return memberService.updateMemberRole(memberId, ownerRoleUpdateRequest, currentUsername);
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

    public FeatureResponse addFeatureWithNumericValue(String name, FeatureValueType featureValueType, String minValue, String maxValue, String measurement, String sportId) {
        FeatureAddRequest featureAddRequest = new FeatureAddRequest();
        featureAddRequest.setName(name);
        featureAddRequest.setFeatureValueType(featureValueType);
        featureAddRequest.setMinValue(minValue);
        featureAddRequest.setMaxValue(maxValue);
        featureAddRequest.setMeasurement(measurement);
        featureAddRequest.setSportId(sportId);
        return featureService.addFeature(featureAddRequest);
    }

    public FeatureResponse addFeatureWithFixedValues(String name, List<String> possibleValues, String sportId) {
        FeatureAddRequest featureAddRequest = new FeatureAddRequest();
        featureAddRequest.setName(name);
        featureAddRequest.setFeatureValueType(FeatureValueType.FIXED_VALUE);
        featureAddRequest.setPossibleValues(possibleValues);
        featureAddRequest.setSportId(sportId);
        return featureService.addFeature(featureAddRequest);
    }

    public String readJson(String filePath) {
        try {
            var file = ResourceUtils.getFile(filePath);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
