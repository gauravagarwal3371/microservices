package com.codergv.studentms.repository;

import com.codergv.studentms.entity.StudentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentDAO, Long> {
}