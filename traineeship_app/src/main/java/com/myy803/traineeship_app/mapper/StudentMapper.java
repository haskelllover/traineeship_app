package com.myy803.traineeship_app.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myy803.traineeship_app.domainmodel.Student;

@Repository
public interface StudentMapper extends JpaRepository<Student, Integer> {
    Optional<Student> findByUsername(String username);
    Optional<Student> findByAM(String am); 
    List<Student> findByLookingForTraineeship(boolean looking);
    List<Student> findByInterestsContaining(String interest);
    List<Student> findByPreferredLocation(String location);
}
