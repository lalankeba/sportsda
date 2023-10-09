package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.RoleAddRequest;
import com.laan.sportsda.dto.request.RoleUpdateRequest;
import com.laan.sportsda.dto.response.RoleResponse;
import com.laan.sportsda.dto.response.RoleShortResponse;
import com.laan.sportsda.service.RoleService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.ROLES)
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getRole(@PathVariable("id") String id) {
        log.info("getting role for id: {}", id);
        RoleResponse roleResponse = roleService.getRole(id);
        log.info("get role {}", roleResponse);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getRoles() {
        log.info("getting roles");
        List<RoleShortResponse> roleShortResponses = roleService.getRoles();
        log.info("get roles");
        return new ResponseEntity<>(roleShortResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addRole(@Valid @RequestBody RoleAddRequest roleAddRequest) {
        log.info("adding role");
        RoleResponse roleResponse = roleService.addRole(roleAddRequest);
        log.info("added new role");
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @PutMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> updateRole(@PathVariable("id") String id, @Valid @RequestBody RoleUpdateRequest roleUpdateRequest) {
        log.info("updating role: {}", id);
        RoleResponse roleResponse = roleService.updateRole(id, roleUpdateRequest);
        log.info("updated role");
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @DeleteMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> deleteRole(@PathVariable("id") String id) {
        log.info("deleting role with id: {}", id);
        roleService.deleteRole(id);
        log.info("deleted role with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
