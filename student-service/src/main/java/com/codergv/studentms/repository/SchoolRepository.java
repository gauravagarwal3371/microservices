package com.codergv.studentms.repository;

import com.codergv.studentms.entity.SchoolDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolDAO, Long> {
    SchoolDAO findByName(String name);
}
