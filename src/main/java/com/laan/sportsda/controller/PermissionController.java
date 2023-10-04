package com.laan.sportsda.controller;

import com.laan.sportsda.dto.response.PermissionResponse;
import com.laan.sportsda.service.PermissionService;
import com.laan.sportsda.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PathUtil.PERMISSIONS)
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getPermission(@PathVariable("id") String id) {
        log.info("getting permission for id: {}", id);
        PermissionResponse permissionResponse = permissionService.getPermission(id);
        log.info("get permission {}", permissionResponse);
        return new ResponseEntity<>(permissionResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getPermissions() {
        log.info("getting permissions");
        List<PermissionResponse> permissionsResponse = permissionService.getPermissions();
        log.info("get permissions");
        return new ResponseEntity<>(permissionsResponse, HttpStatus.OK);
    }

}
