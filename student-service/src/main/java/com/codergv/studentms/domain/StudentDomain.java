package com.codergv.studentms.domain;

public class StudentDomain {
    private String name;
    private String studentId;
    private String grade;
    private String mobileNumber;
    private SchoolDomain school;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public SchoolDomain getSchool() {
        return school;
    }

    public void setSchool(SchoolDomain schoolDomain) {
        this.school = schoolDomain;
    }

    public void performBusinessLogic(StudentDomain studentDomain) {

        studentDomain.setName(studentDomain.getName().toUpperCase());
    }

    public void performBusinessLogic1(StudentDomain studentDomain) {

        studentDomain.setName(studentDomain.getName().toLowerCase());
    }

    @Override
    public String toString() {
        return "StudentDomain{" +
                "name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", grade='" + grade + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", school=" + school +
                '}';
    }
}

