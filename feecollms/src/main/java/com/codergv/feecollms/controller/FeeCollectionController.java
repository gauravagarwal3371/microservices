package com.codergv.feecollms.controller;

import com.codergv.feecollms.dto.FeeCollectionRequest;
import com.codergv.feecollms.entity.FeeCollection;
import com.codergv.feecollms.service.FeeCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fees")
public class FeeCollectionController {
    private final FeeCollectionService feeCollectionService;

    @Autowired
    public FeeCollectionController(FeeCollectionService feeCollectionService) {
        this.feeCollectionService = feeCollectionService;
    }

    @PostMapping("/collect")
    public ResponseEntity<FeeCollection> collectFee(@RequestBody FeeCollectionRequest request) {
        FeeCollection collectedFee = feeCollectionService.collectFee(
                request.getStudentId(),
                request.getStudentName(),
                request.getAmountPaid(),
                request.getPaymentMethod(),
                request.getSchoolName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(collectedFee);
    }
}
