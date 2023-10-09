package com.laan.sportsda.controller;

import com.laan.sportsda.dto.request.FacultyAddRequest;
import com.laan.sportsda.dto.request.FacultyUpdateRequest;
import com.laan.sportsda.dto.response.DepartmentResponse;
import com.laan.sportsda.dto.response.DepartmentShortResponse;
import com.laan.sportsda.dto.response.FacultyShortResponse;
import com.laan.sportsda.dto.response.FacultyResponse;
import com.laan.sportsda.service.DepartmentService;
import com.laan.sportsda.service.FacultyService;
import com.laan.sportsda.util.PathUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(PathUtil.FACULTIES)
@RequiredArgsConstructor
@Slf4j
public class FacultyController {

    private final FacultyService facultyService;

    private final DepartmentService departmentService;

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getFaculty(@PathVariable("id") String id) {
        log.info("getting faculty for id: {}", id);
        FacultyResponse facultyResponse = facultyService.getFaculty(id);
        log.info("get faculty {}", facultyResponse);
        return new ResponseEntity<>(facultyResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getFaculties() {
        log.info("getting faculties");
        List<FacultyShortResponse> facultyShortResponses = facultyService.getFaculties();
        log.info("get faculties");
        return new ResponseEntity<>(facultyShortResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addFaculty(@Valid @RequestBody FacultyAddRequest facultyAddRequest) {
        log.info("adding faculty");
        FacultyResponse facultyResponse = facultyService.addFaculty(facultyAddRequest);
        log.info("added new faculty");
        return new ResponseEntity<>(facultyResponse, HttpStatus.CREATED);
    }

    @PutMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> updateFaculty(@PathVariable("id") String id, @Valid @RequestBody FacultyUpdateRequest facultyUpdateRequest) {
        log.info("updating faculty: {}", id);
        FacultyResponse facultyResponse = facultyService.updateFaculty(id, facultyUpdateRequest);
        log.info("updated faculty");
        return new ResponseEntity<>(facultyResponse, HttpStatus.OK);
    }

    @DeleteMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> deleteFaculty(@PathVariable("id") String id) {
        log.info("deleting faculty with id: {}", id);
        facultyService.deleteFaculty(id);
        log.info("deleted faculty with id: {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(PathUtil.ID_PLACEHOLDER + PathUtil.DEPARTMENTS)
    public ResponseEntity<Object> getDepartmentsByFaculty(@PathVariable("id") String id) {
        log.info("getting departments by faculty id: {}", id);
        List<DepartmentShortResponse> departmentShortResponses = departmentService.getDepartmentsByFaculty(id);
        log.info("get departments by faculty");
        return new ResponseEntity<>(departmentShortResponses, HttpStatus.OK);
    }

}
