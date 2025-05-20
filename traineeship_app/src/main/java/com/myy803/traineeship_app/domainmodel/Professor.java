package com.myy803.traineeship_app.domainmodel;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "professors")
public class Professor {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "username", unique = true)
	private String username;
    
    @Column(name = "professor_name")
	private String professorName;
    
    @Column(name = "interests", columnDefinition = "TEXT")
    private String interests;

	
    @OneToMany(mappedBy = "supervisor", fetch = FetchType.EAGER) 
    private List<TraineeshipPosition> supervisedPositions;

	//getters
    public Integer getId() { 
    	return id; 
    }

	public String getUsername() {
		return username;
	}
	
	public String getProfessorName() {
		return professorName;
	}
	
	public String getInterests() {
		return interests;
	}
	
	public List<String> getInterestsList() {
	    if (interests == null) return Collections.emptyList();
	    return Arrays.stream(interests.split(",\\s*"))
	               .map(String::trim)
	               .filter(s -> !s.isEmpty())
	               .collect(Collectors.toList());
	}

	
    public List<TraineeshipPosition> getSupervisedPositions() {
        return supervisedPositions;
    }

	
	//setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
	
    public void setInterests(String interests) {
        this.interests = interests;
    }
	
    public void setInterestsList(List<String> interestsList) {
        this.interests = interestsList != null ? 
            String.join(", ", interestsList) : null;
    }
	
    public void setSupervisedPositions(List<TraineeshipPosition> supervisedPositions) {
        this.supervisedPositions = supervisedPositions;
    }
}
