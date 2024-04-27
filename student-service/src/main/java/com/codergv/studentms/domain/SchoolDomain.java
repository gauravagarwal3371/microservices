package com.codergv.studentms.domain;

public class SchoolDomain {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SchoolDomain{" +
                "name='" + name + '\'' +
                '}';
    }
}
