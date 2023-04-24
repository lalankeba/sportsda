package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.DepartmentAddRequest;
import com.laan.sportsda.dto.request.DepartmentUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.service.DepartmentService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.DEPARTMENTS)
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDepartment(@PathVariable("id") String id) {
        logger.info("getting department for id: {}", id);
        DepartmentResponse departmentResponse = departmentService.getDepartment(id);
        logger.info("get department {}", departmentResponse);
        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getDepartments() {
        logger.info("getting departments");
        List<DepartmentResponse> departmentResponses = departmentService.getDepartments();
        logger.info("get departments");
        return new ResponseEntity<>(departmentResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addDepartment(@Valid @RequestBody DepartmentAddRequest departmentAddRequest) {
        logger.info("adding department");
        DepartmentResponse departmentResponse = departmentService.addDepartment(departmentAddRequest);
        logger.info("added new department");
        return new ResponseEntity<>(departmentResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDepartment(@PathVariable("id") String id, @Valid @RequestBody DepartmentUpdateRequest departmentUpdateRequest) {
        logger.info("updating department: {}", id);
        DepartmentResponse departmentResponse = departmentService.updateDepartment(id, departmentUpdateRequest);
        logger.info("updated department");
        return new ResponseEntity<>(departmentResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDepartment(@PathVariable("id") String id) {
        logger.info("deleting department with id: {}", id);
        departmentService.deleteDepartment(id);
        logger.info("deleted department with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
