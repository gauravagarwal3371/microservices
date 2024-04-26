package com.codergv.studentms.dto;

import jakarta.validation.constraints.NotEmpty;

public class SchoolDTO {
    @NotEmpty
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
