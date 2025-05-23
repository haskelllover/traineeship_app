package com.myy803.traineeship_app.config;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class WebSecurityConfigTest {

    private WebSecurityConfig config;

    @BeforeEach
    void setUp() {
        config = new WebSecurityConfig();
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder.matches("password", encoder.encode("password")));
    }

    @Test
    void testSuccessHandlerBean() {
        CustomSecuritySuccessHandler handler = config.customSecuritySuccessHandler();
        assertNotNull(handler);
    }
}
