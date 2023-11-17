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
        log.info("saved {} new faculty/ies", savedFacultyEntities.size());
    }

    private List<FacultyEntity> saveFaculties() {
        List<FacultyEntity> savedFacultyEntities = new ArrayList<>();
        String basicFacultyName = propertyUtil.getBasicFacultyName();

        Optional<FacultyEntity> optionalFacultyEntity = facultyRepository.findByName(basicFacultyName);
        if (optionalFacultyEntity.isEmpty()) {
            FacultyEntity basicFacultyEntity = facultyMapper.mapDetailsToEntity(basicFacultyName);
            FacultyEntity savedBasicFacultyEntity = facultyRepository.save(basicFacultyEntity);
            savedFacultyEntities.add(savedBasicFacultyEntity);
        }

        return savedFacultyEntities;
    }

    private void initializeRolesAndPermissions() {
        List<PermissionEntity> savedPermissionEntities = savePermissions();
        log.info("saved {} new permission/s", savedPermissionEntities.size());

        List<RoleEntity> savedRoleEntities = saveRoles();
        log.info("saved {} new role/s", savedRoleEntities.size());
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
                log.info("New permission: {} added to the database.", savedPermissionEntity);
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

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByName(adminRoleName);
        if (optionalRoleEntity.isEmpty()) {
            RoleEntity adminRoleEntity = roleMapper.mapDetailsToEntity(adminRoleName, "Can do all tasks", permissionRepository.findAll());
            RoleEntity savedAdminRoleEntity = roleRepository.save(adminRoleEntity);
            savedRoleEntities.add(savedAdminRoleEntity);
        } else {
            RoleEntity existingRoleEntity = optionalRoleEntity.get();
            List<PermissionEntity> permissionEntities = permissionRepository.findAll();
            existingRoleEntity.setPermissionEntities(permissionEntities);
            RoleEntity updatedAdminRoleEntity = roleRepository.save(existingRoleEntity);
            savedRoleEntities.add(updatedAdminRoleEntity);
        }

        return savedRoleEntities;
    }
}
