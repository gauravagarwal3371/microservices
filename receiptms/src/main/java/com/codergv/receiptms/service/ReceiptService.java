package com.codergv.receiptms.service;

import com.codergv.receiptms.entity.Receipt;
import com.codergv.receiptms.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Receipt generateReceipt(Long studentId) {
        Receipt receipt = new Receipt();
        receipt.setStudentId(studentId);
        receipt.setTimestamp(LocalDateTime.now());
        return receiptRepository.save(receipt);
    }

    public Receipt getReceiptById(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));
    }

}