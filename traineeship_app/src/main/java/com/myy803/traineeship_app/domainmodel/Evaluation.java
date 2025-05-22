package com.myy803.traineeship_app.domainmodel;

import jakarta.persistence.*;


@Entity
@Table(name = "evaluations")
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type")
    private EvaluationType evaluationType;
    
    @ManyToOne
    @JoinColumn(name = "traineeship_position")  	
    private TraineeshipPosition traineeshipPosition;  
    
    @Column(name = "student_motivation")
    private Integer studentMotivation;
    
    @Column(name = "student_efficiency")
    private Integer studentEfficiency;
    
    @Column(name = "student_effectiveness")
    private Integer studentEffectiveness;
    
    @Column(name = "company_facilities")
    private Integer companyFacilities; 
    
    @Column(name = "company_guidance")
    private Integer companyGuidance; 
    
    @Column(name = "comments")
    private String comments;

    //getters
    
    public Integer getId() {
        return id;
    }
    
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }
    
    public Integer getStudentMotivation() {
        return studentMotivation;
    }
    
    public Integer getStudentEfficiency() {
        return studentEfficiency;
    }
    
    public Integer getStudentEffectiveness() {
        return studentEffectiveness;
    }
    
    public Integer getCompanyFacilities() {
        return companyFacilities;
    }
    
    public Integer getCompanyGuidance() {
        return companyGuidance;
    }
    
    public String getComments() { 
    	return comments; 
    }
    
    public TraineeshipPosition getTraineeshipPosition() { 
    	return traineeshipPosition; 
    }

    
    
    //setters
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }
    
    public void setStudentMotivation(Integer studentMotivation) {
        this.studentMotivation = studentMotivation;
    }
    
	public void setStudentEffectiveness(Integer studentEffectiveness) {
		this.studentEffectiveness = studentEffectiveness;
	}
    
    public void setStudentEfficiency(Integer studentEfficiency) {
        this.studentEfficiency = studentEfficiency;
    }
    
    public void setCompanyFacilities(Integer companyFacilities) {
        this.companyFacilities = companyFacilities;
    }
    
    public void setCompanyGuidance(Integer companyGuidance) {
        this.companyGuidance = companyGuidance;
    }
    
    public void setComments(String comments) { 
    	this.comments = comments; 
    }
    public void setTraineeshipPosition(TraineeshipPosition traineeshipPosition) { 
        this.traineeshipPosition = traineeshipPosition; 
    }

}