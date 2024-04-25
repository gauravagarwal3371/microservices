package com.codergv.studentms.entity;

import jakarta.persistence.*;

@Entity
public class StudentDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="student_id")
    private Long id;

    private String name;
    private String grade;
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolDAO school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public SchoolDAO getSchool() {
        return school;
    }

    public void setSchool(SchoolDAO school) {
        this.school = school;
    }
}
