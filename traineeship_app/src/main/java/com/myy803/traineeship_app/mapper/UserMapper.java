package com.myy803.traineeship_app.mapper;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myy803.traineeship_app.domainmodel.User;

@Repository
public interface UserMapper extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
