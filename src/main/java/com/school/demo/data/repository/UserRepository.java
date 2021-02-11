package com.school.demo.data.repository;

import com.school.demo.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date: 1/29/2021 Time: 12:12 PM
 * <p>
 *
 * @author Vladislav_Zlatanov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
