package com.codergv.feecollms.service;


import com.codergv.feecollms.entity.FeeCollection;
import com.codergv.feecollms.repository.FeeCollectionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeeCollectionService {
    private final FeeCollectionRepository feeCollectionRepository;

    @Autowired
    public FeeCollectionService(FeeCollectionRepository feeCollectionRepository) {
        this.feeCollectionRepository = feeCollectionRepository;
    }

    public FeeCollection collectFee(Long studentId, String studentName, double amountPaid, String paymentMethod, String schoolName) {
        FeeCollection feeCollection = new FeeCollection();
        feeCollection.setStudentId(studentId);
        feeCollection.setStudentName(studentName);
        feeCollection.setAmountPaid(amountPaid);
        feeCollection.setTimestamp(LocalDateTime.now());
        feeCollection.setPaymentMethod(paymentMethod);
        feeCollection.setSchoolName(schoolName);

        return feeCollectionRepository.save(feeCollection);
    }
}
