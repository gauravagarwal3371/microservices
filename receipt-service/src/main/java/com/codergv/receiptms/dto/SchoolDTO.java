package com.codergv.receiptms.dto;

public class SchoolDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SchoolDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
