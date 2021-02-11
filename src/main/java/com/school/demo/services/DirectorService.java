package com.school.demo.services;

import com.school.demo.dto.DirectorDTO;
import com.school.demo.models.CreateDirectorModel;
import com.school.demo.views.CourseIdAndGradesView;
import com.school.demo.views.ParentDirectorView;
import com.school.demo.views.TeacherView;

import java.util.List;

public interface DirectorService {
    DirectorDTO get(long directorId);

    DirectorDTO create(CreateDirectorModel model);

    DirectorDTO edit(long id, CreateDirectorModel model);

    boolean delete(long id);

    List<CourseIdAndGradesView> getAllCoursesAndAllGrades(long directorId);

    List<TeacherView> getAllTeachers(long directorId);


    List<ParentDirectorView> getAllParents(long directorId);


}
