package com.myy803.traineeship_app.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;

public class CustomSecuritySuccessHandlerTest {

    private CustomSecuritySuccessHandler successHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;
    private DefaultRedirectStrategy redirectStrategy;

    @BeforeEach
    void setUp() {
        successHandler = new CustomSecuritySuccessHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authentication = mock(Authentication.class);
        redirectStrategy = mock(DefaultRedirectStrategy.class);
    }
    
    @Test
    void testDetermineTargetUrl_AdminRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(authentication.getAuthorities())
        	.thenReturn((Collection) authorities); // raw cast avoids wildcard conflict

        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/admin/dashboard", targetUrl);
    }

	@Test
    void testDetermineTargetUrl_StudentRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));

        when(authentication.getAuthorities())
            .thenReturn((Collection) authorities); 

        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/student/dashboard", targetUrl);
    }

    @Test
    void testDetermineTargetUrl_ProfessorRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
        when(authentication.getAuthorities())
        	.thenReturn((Collection) authorities); 

        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/professor/dashboard", targetUrl);
    }

	@Test
    void testDetermineTargetUrl_CompanyRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_COMPANY"));
        when(authentication.getAuthorities())
        	.thenReturn((Collection) authorities); 

        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/company/dashboard", targetUrl);
    }

	@Test
    void testDetermineTargetUrl_CommitteeMemberRole() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_COMMITTEE_MEMBER"));
        when(authentication.getAuthorities())
    		.thenReturn((Collection) authorities);
        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/committee/dashboard", targetUrl);
    }

    @Test
    void testDetermineTargetUrl_DefaultCase() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        when(authentication.getAuthorities())
    		.thenReturn((Collection) authorities);
        String targetUrl = successHandler.determineTargetUrl(authentication);
        assertEquals("/login?error=true", targetUrl);
    }
    
    @Test
    void testHandle_WhenResponseCommitted_DoesNotRedirect() throws IOException {
        when(response.isCommitted()).thenReturn(true);
        
        successHandler.handle(request, response, authentication);
        
        verify(response, never()).sendRedirect(anyString());
    }
}