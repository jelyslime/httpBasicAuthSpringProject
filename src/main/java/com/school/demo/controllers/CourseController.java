package com.school.demo.controllers;

import com.school.demo.converter.GenericConverter;
import com.school.demo.models.CreateCourseModel;
import com.school.demo.services.implementations.CourseServiceImpl;
import com.school.demo.views.CourseView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/courses")
@AllArgsConstructor
@Slf4j
public class CourseController {


    private final GenericConverter converter;
    CourseServiceImpl service;



    @GetMapping("/{courseId}")
    public ResponseEntity<CourseView> getCourse(@PathVariable("courseId") long id) {
        log.info("Endpoint '/{courseId}' reached.");

        log.debug("Calling service.");
        CourseView view = converter.convert(service.get(id), CourseView.class);

        return ResponseEntity.ok().body(view);
    }



    @PostMapping("/create")
    public ResponseEntity<CourseView> createCourse(@RequestBody CreateCourseModel model) {
        log.info("Endpoint '/create' reached.");

        log.debug("Calling service.");
        CourseView view = converter.convert(service.create(model), CourseView.class);

        return ResponseEntity.ok().body(view);
    }



    @DeleteMapping("/{courseId}/delete")
    public ResponseEntity<Void> deleteCourse(@PathVariable("courseId") long id) {
        log.info("Endpoint '/{courseId}/delete' reached.");

        log.debug("Calling service.");
        boolean flag = service.delete(id);
        if (flag) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{courseId}/assign/teacher/{teacherId}")
    public ResponseEntity<Void> addTeacherToCourse(@PathVariable long courseId, @PathVariable long teacherId) {
        log.info("Endpoint '/{courseId}/assign/teacher/{teacherId}' reached.");

        log.debug("Calling service.");
        service.assignTeacher(courseId, teacherId);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/{courseId}/assign/student/{studentID}")
    public ResponseEntity<Void> addStudentToCourse(@PathVariable long courseId, @PathVariable long studentID) {
        log.info("Endpoint '/{courseId}/assign/student/{studentID}' reached.");

        log.debug("Calling service.");
        service.addStudent(courseId, studentID);

        return ResponseEntity.ok().build();
    }


    @PutMapping("/{courseId}/remove/student/{studentID}")
    public ResponseEntity<Void> removeStudentToCourse(@PathVariable long courseId, @PathVariable long studentID) {
        log.info("Endpoint '/{courseId}/remove/student/{studentID}' reached.");

        log.debug("Calling service.");
        service.removeStudent(courseId, studentID);

        return ResponseEntity.ok().build();
    }

}
