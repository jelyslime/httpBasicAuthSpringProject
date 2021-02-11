package com.school.demo.util;

import com.school.demo.data.entity.User;
import com.school.demo.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Date: 2/10/2021 Time: 9:35 AM
 * <p>
 *
 * @author Vladislav_Zlatanov
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("User with username " + username
                    + " not found.");
        }

        return new CustomUserDetails(user);
    }
}
