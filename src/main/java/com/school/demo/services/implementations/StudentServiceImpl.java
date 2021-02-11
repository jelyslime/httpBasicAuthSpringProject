package com.school.demo.services.implementations;

import com.school.demo.converter.GenericConverter;
import com.school.demo.data.entity.Course;
import com.school.demo.data.entity.Grade;
import com.school.demo.data.entity.Student;
import com.school.demo.data.repository.StudentRepository;
import com.school.demo.dto.CourseDTO;
import com.school.demo.dto.GradeDTO;
import com.school.demo.dto.StudentDTO;
import com.school.demo.exception.NoSuchDataException;
import com.school.demo.models.CreatePersonModel;
import com.school.demo.services.StudentService;
import com.school.demo.util.Roles;
import com.school.demo.validator.Validator;
import com.school.demo.views.CourseIdAndGradesView;
import com.school.demo.views.SimpleGradeView;
import com.school.demo.views.TeacherView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final PasswordEncoder encoder;
    private final GenericConverter converter;
    private final Validator validator;

    private final StudentRepository repository;

    @Override
    public StudentDTO get(long studentId) {
        log.info("Calling student repository");
        return converter.convert(repository.findById(studentId)
                        .orElseThrow(() ->
                                new NoSuchDataException(String.format("Student %s does not exists in records.", studentId)))
                , StudentDTO.class);
    }

    @Override
    public StudentDTO create(CreatePersonModel model) {
        log.debug("Creating new student.");
        String role = Roles.STUDENT;
        validateModel(model, role);

        StudentDTO studentDTO = initialPopulationStudentDTO(model);
        studentDTO.setRoles(role);

        Student entity = converter.convert(studentDTO, Student.class);
        return converter.convert(repository.save(entity), StudentDTO.class);

    }

    @Override
    public StudentDTO edit(long id, CreatePersonModel model) {
        log.debug("Editing student " + id);
        String role = Roles.STUDENT;
        validateModel(model, role);

        StudentDTO studentDTO = populateStudentDTO(this.get(id), model);

        Student entity = converter.convert(studentDTO, Student.class);
        return converter.convert(repository.save(entity), StudentDTO.class);
    }

    @Override
    public boolean delete(long id) {
        log.debug("Deleting student " + id);
        boolean result = repository.existsById(id);
        if (!result) {
            return false;
        }
        repository.deleteById(id);
        result = repository.existsById(id);
        return !result;
    }

    @Override
    public boolean removeSchool(long id) {
        log.debug("Removing school " + id);
        StudentDTO studentDTO = this.get(id);

        studentDTO.setSchool(null);

        repository.saveAndFlush(converter.convert(studentDTO, Student.class));
        return true;
    }

    @Override
    public List<CourseIdAndGradesView> getAllGrades(long studentId) {
        log.debug("Getting all grades to student " + studentId);
        StudentDTO student = this.get(studentId);

        Set<GradeDTO> grades = converter.convertSet(student.getGrades(), GradeDTO.class);

        Set<CourseDTO> courseDTOS = converter.convertSet(student.getCourses(), CourseDTO.class);

        return getCourseIdAndGradesViews(grades, courseDTOS);
    }

    @Override
    public List<TeacherView> getAllTeachers(long studentId) {
        log.debug("Getting all teachers to student " + studentId);
        StudentDTO student = this.get(studentId);

        return student.getCourses()
                .stream()
                .map(Course::getTeacher)
                .map(teacher -> converter.convert(teacher, TeacherView.class))
                .collect(Collectors.toList());
    }

    @Override
    public double getAvgGrade(long studentId) {
        log.debug("Getting average grade to student " + studentId);
        StudentDTO student = this.get(studentId);

        return student.getGrades()
                .stream()
                .mapToDouble(Grade::getGrade)
                .average().orElse(2.0);
    }

    private StudentDTO initialPopulationStudentDTO(CreatePersonModel model) {
        log.debug("Initial population of student.");
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setCourses(new HashSet<>());
        studentDTO.setGrades(new HashSet<>());
        studentDTO.setSchool(null);
        studentDTO.setParents(new HashSet<>());

        studentDTO.setFirstName(model.getFirstName());
        studentDTO.setLastName(model.getLastName());
        studentDTO.setUsername(model.getUsername());
        studentDTO.setPassword(encoder.encode(model.getPassword()));
        return studentDTO;
    }

    private void validateModel(CreatePersonModel model, String role) {
        log.debug("Validating model.");
        validator.validateRole(role);
        validator.validateUsername(model.getUsername());
        validator.validatePassword(model.getPassword());
    }

    private StudentDTO populateStudentDTO(StudentDTO studentDTO, CreatePersonModel model) {
        log.debug("Populating student with data from model.");
        studentDTO.setFirstName(model.getFirstName());
        studentDTO.setLastName(model.getLastName());
        studentDTO.setUsername(model.getUsername());
        studentDTO.setPassword(encoder.encode(model.getPassword()));
        return studentDTO;
    }

    private List<CourseIdAndGradesView> getCourseIdAndGradesViews(Set<GradeDTO> grades, Set<CourseDTO> courseDTOS) {
        log.debug("Getting courses and grades.");
        List<CourseIdAndGradesView> courseIdAndGradesViews = new ArrayList<>();

        for (CourseDTO courseDTO : courseDTOS) {
            CourseIdAndGradesView view = new CourseIdAndGradesView();
            view.setId(courseDTO.getId());
            view.setGrades(grades
                    .stream()
                    .filter(x -> x.getCourse().getId() == courseDTO.getId())
                    .map(x -> converter.convert(x, SimpleGradeView.class))
                    .collect(Collectors.toList()));
            courseIdAndGradesViews.add(view);
        }
        return courseIdAndGradesViews;
    }

}
