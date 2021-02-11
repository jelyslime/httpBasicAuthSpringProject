package com.school.demo.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Date: 1/28/2021 Time: 2:31 PM
 * <p>
 *
 * @author Vladislav_Zlatanov
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")
public class User extends BaseEntity {

    @NotNull
    @Column(name = "user_name")
    private String userName;

    @NotNull

    private String password;

    @NotNull
    private String roles;

    private String firstName;
    private String lastName;

}
