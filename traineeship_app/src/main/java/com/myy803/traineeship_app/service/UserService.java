package com.myy803.traineeship_app.service;

import org.springframework.stereotype.Service;

import com.myy803.traineeship_app.domainmodel.User;

@Service
public interface UserService {
	public void saveUser(User user);
    public boolean isUserPresent(User user);
    
}
