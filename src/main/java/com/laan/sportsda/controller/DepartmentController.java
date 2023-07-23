package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.service.DepartmentService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.DEPARTMENTS)
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getDepartment(@PathVariable("id") String id) {
        log.info("getting department for id: {}", id);
        DepartmentResponse departmentResponse = departmentService.getDepartment(id);
        log.info("get department {}", departmentResponse);
        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getDepartments() {
        log.info("getting departments");
        List<DepartmentResponse> departmentResponses = departmentService.getDepartments();
        log.info("get departments");
        return new ResponseEntity<>(departmentResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addDepartment(@Valid @RequestBody DepartmentAddRequest departmentAddRequest) {
        log.info("adding department");
        DepartmentResponse departmentResponse = departmentService.addDepartment(departmentAddRequest);
        log.info("added new department");
        return new ResponseEntity<>(departmentResponse, HttpStatus.CREATED);
    }

    @PutMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> updateDepartment(@PathVariable("id") String id, @Valid @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        log.info("updating department: {}", id);
        DepartmentResponse departmentResponse = departmentService.updateDepartment(id, departmentUpdateRequest);
        log.info("updated department");
        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @DeleteMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> deleteDepartment(@PathVariable("id") String id) {
        log.info("deleting department with id: {}", id);
        departmentService.deleteDepartment(id);
        log.info("deleted department with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
