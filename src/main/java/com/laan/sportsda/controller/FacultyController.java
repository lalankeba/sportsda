package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.service.DepartmentService;
import com.laan.sportsda.service.FacultyService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.FACULTIES)
public class FacultyController {

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    private final FacultyService facultyService;

    private final DepartmentService departmentService;

    public FacultyController(FacultyService facultyService, DepartmentService departmentService) {
        this.facultyService = facultyService;
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFaculty(@PathVariable("id") String id) {
        logger.info("getting faculty for id: {}", id);
        FacultyResponse facultyResponse = facultyService.getFaculty(id);
        logger.info("get faculty {}", facultyResponse);
        return new ResponseEntity<>(facultyResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getFaculties() {
        logger.info("getting faculties");
        List<FacultyResponse> facultyResponses = facultyService.getFaculties();
        logger.info("get faculties");
        return new ResponseEntity<>(facultyResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addFaculty(@Valid @RequestBody FacultyAddRequest facultyAddRequest) {
        logger.info("adding faculty");
        FacultyResponse facultyResponse = facultyService.addFaculty(facultyAddRequest);
        logger.info("added new faculty");
        return new ResponseEntity<>(facultyResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFaculty(@PathVariable("id") String id, @Valid @RequestBody FacultyUpdateRequest facultyUpdateRequest) {
        logger.info("updating faculty: {}", id);
        FacultyResponse facultyResponse = facultyService.updateFaculty(id, facultyUpdateRequest);
        logger.info("updated faculty");
        return new ResponseEntity<>(facultyResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFaculty(@PathVariable("id") String id) {
        logger.info("deleting faculty with id: {}", id);
        facultyService.deleteFaculty(id);
        logger.info("deleted faculty with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}/departments")
    public ResponseEntity<Object> getDepartmentsByFaculty(@PathVariable("id") String id) {
        logger.info("getting departments by faculty id: {}", id);
        List<DepartmentResponse> departmentResponses = departmentService.getDepartmentsByFaculty(id);
        logger.info("get departments by faculty");
        return new ResponseEntity<>(departmentResponses, HttpStatus.OK);
    }

}
