package com.myy803.traineeship_app.service;
import org.springframework.stereotype.Service;

import com.myy803.traineeship_app.domainmodel.UserDetails;

@Service
public interface UserDetailsService {
	UserDetails loadUserByUsername(String username);
}
