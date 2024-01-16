package com.laan.sportsda.config;

import com.laan.sportsda.entity.FacultyEntity;
import com.laan.sportsda.entity.PermissionEntity;
import com.laan.sportsda.entity.RoleEntity;
import com.laan.sportsda.enums.PermissionDescription;
import com.laan.sportsda.mapper.FacultyMapper;
import com.laan.sportsda.mapper.PermissionMapper;
import com.laan.sportsda.mapper.RoleMapper;
import com.laan.sportsda.repository.FacultyRepository;
import com.laan.sportsda.repository.PermissionRepository;
import com.laan.sportsda.repository.RoleRepository;
import com.laan.sportsda.util.PropertyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppInitRunner implements ApplicationRunner {

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    private final PropertyUtil propertyUtil;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---- Application runner is running ----");

        initializeRolesAndPermissions();
        initializeFaculties();

        log.info("---- Application runner executed successfully ----");
    }

    private void initializeFaculties() {
        List<FacultyEntity> savedFacultyEntities = saveFaculties();
        int size = savedFacultyEntities.size();
        String postFix = (size == 1) ? "y" : "ies";
        log.info("Saved {} new facult{}", size, postFix);
    }

    private List<FacultyEntity> saveFaculties() {
        List<FacultyEntity> savedFacultyEntities = new ArrayList<>();
        String basicFacultyName = propertyUtil.getBasicFacultyName();

        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findByName(basicFacultyName);
        if (optionalFacultyEntity.isEmpty()) {
            FacultyEntity basicFacultyEntity = facultyMapper.mapDetailsToEntity(basicFacultyName);
            basicFacultyEntity.setCreatedBy("SYSTEM");
            FacultyEntity savedBasicFacultyEntity = facultyRepository.save(basicFacultyEntity);
            savedFacultyEntities.add(savedBasicFacultyEntity);
        }

        return savedFacultyEntities;
    }

    private void initializeRolesAndPermissions() {
        List<PermissionEntity> savedPermissionEntities = savePermissions();
        int listSize = savedPermissionEntities.size();
        String postFix = (listSize == 1) ? "" : "s";
        log.info("Saved {} new permission{}", listSize, postFix);

        List<RoleEntity> savedRoleEntities = saveRoles();
        listSize = savedRoleEntities.size();
        postFix = (listSize == 1) ? "" : "s";
        log.info("Saved {} new role{}", listSize, postFix);
    }

    private List<PermissionEntity> savePermissions() {
        List<PermissionDescription> permissionDescriptions = Arrays.stream(PermissionDescription.values()).toList();
        List<PermissionEntity> savedPermissionEntities = new ArrayList<>();
        List<String> permissionIdsInEnum = new ArrayList<>();

        permissionDescriptions.forEach(permissionDescription -> {
            Optional<PermissionEntity> optionalPermissionEntity = permissionRepository.findByDescription(permissionDescription);
            if (optionalPermissionEntity.isEmpty()) {
                PermissionEntity permissionEntity = permissionMapper.mapEnumToEntity(permissionDescription);
                PermissionEntity savedPermissionEntity = permissionRepository.save(permissionEntity);
                savedPermissionEntities.add(savedPermissionEntity);
                permissionIdsInEnum.add(savedPermissionEntity.getId());
                log.info("New permission: {} added to the database.", savedPermissionEntity.getDescription());
            } else {
                permissionIdsInEnum.add(optionalPermissionEntity.get().getId());
            }
        });

        List<String> permissionIdsInDb = permissionRepository.getAllIds();
        List<String> permissionIdsOnlyInDb = new ArrayList<>(permissionIdsInDb);
        permissionIdsOnlyInDb.removeAll(permissionIdsInEnum);

        permissionIdsOnlyInDb.forEach(id -> {
            permissionRepository.deleteByPermissionId(id);
            log.info("Permission only in database was deleted. Id: {}", id);
        });

        return savedPermissionEntities;
    }

    private List<RoleEntity> saveRoles() {
        List<RoleEntity> savedRoleEntities = new ArrayList<>();

        String adminRoleName = propertyUtil.getAdminRoleName();
        Optional<RoleEntity> optionalAdminRoleEntity = saveRole(adminRoleName, "Has all access", permissionRepository.findAll());
        optionalAdminRoleEntity.ifPresent(savedRoleEntities::add);

        String basicRoleName = propertyUtil.getBasicRoleName();
        Optional<RoleEntity> optionalBasicRoleEntity = saveRole(basicRoleName, "Has limited access", List.of());
        optionalBasicRoleEntity.ifPresent(savedRoleEntities::add);

        return savedRoleEntities;
    }

    private Optional<RoleEntity> saveRole(String roleName, String roleDescription, List<PermissionEntity> permissionEntities) {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByName(roleName);

        if (optionalRoleEntity.isEmpty()) {
            RoleEntity roleEntity = roleMapper.mapDetailsToEntity(roleName, roleDescription, permissionEntities);
            roleEntity.setCreatedBy("SYSTEM");
            RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
            log.info("New role: {} added to the database.", savedRoleEntity.getName());
            return Optional.of(savedRoleEntity);
        }
        return Optional.empty();
    }
}
