package com.myy803.traineeship_app.dao;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myy803.traineeship_app.domainmodel.User;

public interface UserDAO extends JpaRepository<User, Integer> {
	
	Optional<User> findByUsername(String username);
}
