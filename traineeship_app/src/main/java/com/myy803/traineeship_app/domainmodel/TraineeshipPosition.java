package com.myy803.traineeship_app.domainmodel;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traineeship_positions")
public class TraineeshipPosition {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "from_date")
    private LocalDate fromDate;
    
    @Column(name = "to_date")
    private LocalDate toDate;
    
    @Column(name = "topics", columnDefinition = "TEXT")
    private String topics;  // Store as comma-separated string

    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;  // Store as comma-separated string
    
    @Column(name = "is_assigned")
    private Boolean isAssigned;
    
    @Column(name = "student_logbook")
    private String studentLogbook;
    
    @Column(name = "pass_fail_grade")
    private Boolean passFailGrade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER) // EAGER loading for supervisor
    @JoinColumn(name = "supervisor_id")
    private Professor supervisor;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToMany(mappedBy = "traineeshipPosition") 
    private List<Evaluation> evaluations = new ArrayList<>();
    
    @Column(name = "logbook_entries", columnDefinition = "TEXT")
    private String logbookEntries;
    
    @Column(name = "hours_completed")
    private int hoursCompleted;
    
    @Column(columnDefinition = "TEXT")
    private String weeklyReport;
    
    @Column(columnDefinition = "TEXT")
    private String achievements;
    
    @Column(columnDefinition = "TEXT")
    private String skillsGained;

    // Add getters and setters
    
    //getters
    public Integer getId() { 
    	return id; 
    }
    
    public String getTitle() { 
    	return title; 
    }
    
    public String getDescription() { 
    	return description; 
    }
    
    public LocalDate getFromDate() {
        return fromDate;
    }
    
    public LocalDate getToDate() {
        return toDate;
    }
    
    public String getTopics() { 
    	return topics; 
    }
    
    public String getSkills() { 
    	return skills; 
    }
    
    public boolean getIsAssigned() { 
    	return isAssigned; 
    }
    
    public String getStudentLogbook() { 
    	return studentLogbook; 
    }
    
    public boolean getPassFailGrade() {
        return passFailGrade;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public Professor getSupervisor() {
        return supervisor;
    }
    
    public Company getCompany() {
        return company;
    }
    
    public String getCompanyName() {
        if (company != null) {
            return company.getCompanyName();
        } else {
            return null;
        }
    }
    
    public String getCompanyLocation() {
        if (company != null) {
            return company.getCompanyLocation();
        } else {
            return null;
        }
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }
    
    public String getLogbookEntries() {
    	return logbookEntries;
    }
    
    public int getHoursCompleted() {
    	return hoursCompleted;
    }
    
    public String getWeeklyReport() {
    	return weeklyReport;
    }
    
    public String getAchievements() {
    	return achievements;
    }
    
    public String getSkillsGained() {
    	return skillsGained;
    }
    
    //setters
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    
    
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    
    public void setTopics(String topics) {
        this.topics = topics;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public void setIsAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }
    
    public void setStudentLogbook(String studentLogbook) {
        this.studentLogbook = studentLogbook;
    }

    public void setPassFailGrade(boolean passFailGrade) {
        this.passFailGrade = passFailGrade;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public void setSupervisor(Professor supervisor) {
    	this.supervisor = supervisor;
        if (supervisor != null && supervisor.getSupervisedPositions() != null 
                && !supervisor.getSupervisedPositions().contains(this)) {
            supervisor.getSupervisedPositions().add(this);
        }
    }
    
    public void setCompany(Company company) {
        this.company = company; 
    }
    
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    
    public void setLogbookEntries(String logbookEntries) {
    	this.logbookEntries = logbookEntries;
    }
    
    public void setHoursCompleted(int hoursCompleted) {
    	this.hoursCompleted = hoursCompleted;
    }
    
    public void setWeeklyReport(String weeklyReport) {
    	this.weeklyReport = weeklyReport;
    }
    
    public void setAchievements(String achievements) {
    	this.achievements = achievements;
    }
    
    public void setSkillsGained(String skillsGained) {
    	this.skillsGained = skillsGained;
    }
    
}   
