package com.myy803.traineeship_app.domainmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "username", unique = true)
	private String username;  
    
    @Column(name = "student_name")
	private String studentName;
    
    @Column(name = "am", unique = true)
	private String AM;
    
    @Column(name = "avg_grade")
	private double avgGrade;
    
    @Column(name = "preferred_location")
	private String preferredLocation;
    
    @Column(name = "interests", columnDefinition = "TEXT")
    private String interests;

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;

    @Column(name = "looking_for_traineeship")
	private boolean lookingForTraineeship;
	
	@OneToOne(mappedBy = "student")
	private TraineeshipPosition assignedTraineeship;
	
	@Column(name = "year_of_studies")
	private Integer yearOfStudies;

	@Column(name = "department")
	private String department;
	
	
	//getters
	
    public Integer getId() { 
    	return id; 
    }

	public String getUsername() {
		return username;
	}
	
	public String getStudentName() {
		return studentName;
	}
	
	public String getAM() {
		return AM;
	}
	
	public double getAvgGrade() {
		return avgGrade;
	}
	
	public String getPreferredLocation() {
		return preferredLocation;
	}
	
    public String getInterests() {
        return interests;
    }

    public List<String> getInterestsList() {
        if (interests == null || interests.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(interests.split(",\\s*"));
    }
    
    public String getSkills() {
        return skills;
    }
    
    public List<String> getSkillsList() {
        if (skills == null || skills.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(skills.split(",\\s*"));
    }
    
	public boolean isLookingForTraineeship() {
		return lookingForTraineeship;
	}
	
	public TraineeshipPosition getAssignedTraineeship() {
		return assignedTraineeship;
	}
	
	public Integer getYearOfStudies() {
	    return yearOfStudies;
	}

	public String getDepartment() {
	    return department;
	}
	
	//setters
	
	public void setId(int id) {
		this.id =id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public void setAM(String AM) {
		this.AM = AM;
	}
	
	public void setAvgGrade(double avgGrade) {
		this.avgGrade = avgGrade;
	}
	
	public void setPreferredLocation(String preferredLocation) {
		this.preferredLocation = preferredLocation;
	}
	
    public void setInterests(String interests) {
        this.interests = interests;
    }
	
    public void setInterestsList(List<String> interestsList) {
        this.interests = String.join(", ", interestsList);
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
	
    public void setSkillsList(List<String> skillsList) {
        this.skills = String.join(", ", skillsList);
    }

	public void setAssignedTraineeship(TraineeshipPosition assignedTraineeship) {
		this.assignedTraineeship = assignedTraineeship;
	}

	public void setLookingForTraineeship(boolean lookingForTraineeship) {
		this.lookingForTraineeship = lookingForTraineeship;
	}
	
	public void setSupervisedPositions(TraineeshipPosition supervisedPositions) {
		this.assignedTraineeship = supervisedPositions;
	}
	
	public void setYearOfStudies(Integer yearOfStudies) {
	    this.yearOfStudies = yearOfStudies;
	}
	
	public void setDepartment(String department) {
	    this.department = department;
	}
	
    // Helper methods
    public void addInterest(String interest) {
        List<String> interestsList = getInterestsList();
        interestsList.add(interest);
        setInterestsList(interestsList);
    }

    public void addSkill(String skill) {
        List<String> skillsList = getSkillsList();
        skillsList.add(skill);
        setSkillsList(skillsList);
    }

}
