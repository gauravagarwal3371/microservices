package com.codergv.feecollms.repository;

import com.codergv.feecollms.entity.FeeCollectionDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeeCollectionRepository extends JpaRepository<FeeCollectionDAO, Long> {
    Optional<List<FeeCollectionDAO>> findByStudentId(String studentId);
}
