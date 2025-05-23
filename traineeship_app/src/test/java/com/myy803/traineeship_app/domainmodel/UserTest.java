package com.myy803.traineeship_app.domainmodel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGettersAndSetters() {
        user.setId(1);
        user.setUsername("user1");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);

        assertEquals(1, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.STUDENT, user.getRole());
    }

    @Test
    void testUserDetailsMethods() {
        user.setUsername("user1");
        user.setPassword("password123");
        user.setRole(Role.STUDENT);

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        
        assertEquals(1, user.getAuthorities().size());
        assertEquals(new SimpleGrantedAuthority("ROLE_STUDENT"), 
            user.getAuthorities().iterator().next());
    }
}