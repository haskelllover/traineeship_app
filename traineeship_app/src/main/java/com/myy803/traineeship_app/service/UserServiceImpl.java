package com.myy803.traineeship_app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myy803.traineeship_app.dao.UserDAO;
import com.myy803.traineeship_app.domainmodel.User;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDAO userDAO;

    @Override
    public void saveUser(User user) {
        // Only encode password during registration
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDAO.save(user);
    }

    @Override
    public boolean isUserPresent(User user) {
        Optional<User> storedUser = userDAO.findByUsername(user.getUsername());
        return storedUser.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("==== DEBUG LOGIN ====");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password from DB: " + user.getPassword());
        System.out.println("Roles: " + user.getAuthorities());

        return user;
    }

}
