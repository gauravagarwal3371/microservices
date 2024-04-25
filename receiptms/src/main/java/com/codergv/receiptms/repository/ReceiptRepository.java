package com.codergv.receiptms.repository;

import com.codergv.receiptms.entity.ReceiptDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<ReceiptDAO, Long> {

    Optional<ReceiptDAO> findByReference(String reference);
}
