package com.myy803.traineeship_app.service;

import com.myy803.traineeship_app.dao.UserDAO;
import com.myy803.traineeship_app.domainmodel.Role;
import com.myy803.traineeship_app.domainmodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_encodesPasswordAndSavesUser() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("plaintext");

        when(passwordEncoder.encode("plaintext")).thenReturn("encodedPassword");

        userService.saveUser(user);

        assertEquals("encodedPassword", user.getPassword());
        verify(userDAO).save(user);
    }

    @Test
    void isUserPresent_returnsTrueIfUserExists() {
        User user = new User();
        user.setUsername("john");

        when(userDAO.findByUsername("john")).thenReturn(Optional.of(user));

        assertTrue(userService.isUserPresent(user));
    }

    @Test
    void isUserPresent_returnsFalseIfUserDoesNotExist() {
        User user = new User();
        user.setUsername("jane");

        when(userDAO.findByUsername("jane")).thenReturn(Optional.empty());

        assertFalse(userService.isUserPresent(user));
    }

    @Test
    void loadUserByUsername_returnsUserIfExists() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("encodedPassword");
        // Set a role from the enum
        user.setRole(Role.USER);  // or any other role from the enum

        when(userDAO.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails loadedUser = userService.loadUserByUsername("john");

        assertEquals("john", loadedUser.getUsername());
        assertEquals("encodedPassword", loadedUser.getPassword());
    }

    @Test
    void loadUserByUsername_throwsExceptionIfUserNotFound() {
        when(userDAO.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("unknown");
        });
    }
}
