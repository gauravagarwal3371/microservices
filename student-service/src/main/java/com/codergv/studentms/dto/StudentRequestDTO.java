package com.codergv.studentms.dto;

import jakarta.validation.constraints.NotEmpty;

public class StudentRequestDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    private String grade;
    private String mobileNumber;
    @NotEmpty
    private SchoolDTO school;

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

    public SchoolDTO getSchool() {
        return school;
    }

    public void setSchool(SchoolDTO school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "StudentRequestDTO{" +
                "name='" + name + '\'' +
                ", grade='" + grade + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", school=" + school +
                '}';
    }
}
