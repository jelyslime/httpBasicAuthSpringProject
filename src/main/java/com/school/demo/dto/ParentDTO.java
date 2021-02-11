package com.school.demo.dto;

import com.school.demo.data.entity.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ParentDTO {
    private long id;
    private String username;
    private String password;
    private String roles;
    private String firstName;
    private String lastName;
    private Set<Student> kids;
}
