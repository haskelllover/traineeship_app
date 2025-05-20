package com.myy803.traineeship_app.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  com.myy803.traineeship_app.domainmodel.Company;

@Repository
public interface CompanyMapper extends JpaRepository<Company, Integer> {
	Optional<Company> findByUsername(String username);
    List<Company> findByCompanyLocation(String location);
}