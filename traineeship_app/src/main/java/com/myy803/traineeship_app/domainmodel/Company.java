package com.myy803.traineeship_app.domainmodel;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "username", unique = true)
	private String username;
   
    @Column(name = "company_name")
	private String companyName;
    
    @Column(name = "company_location")
	private String companyLocation;
	
    @OneToMany(mappedBy = "company")
    private List<TraineeshipPosition> positions = new ArrayList<>();
    
	//getters
    
    public Integer getId() {
        return id;
    }
    
	public String getUsername() {
		return username;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public String getCompanyLocation() {
		return companyLocation;
	}
	
    public List<TraineeshipPosition> getPositions() {
        return positions;
    }
		
    
	//setters
	public void setId(int id) {
		this.id = id;
		
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}
	
    public void setPositions(List<TraineeshipPosition> positions) {
        this.positions = positions;
    }
    

    public void addPosition(TraineeshipPosition position) {
        positions.add(position);
        position.setCompany(this);
    }

    public void removePosition(TraineeshipPosition position) {
        positions.remove(position);
        position.setCompany(null);
    }

}