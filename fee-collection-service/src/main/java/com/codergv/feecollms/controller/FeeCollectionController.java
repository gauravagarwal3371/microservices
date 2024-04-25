package com.codergv.feecollms.controller;

import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.service.FeeCollectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fees")
public class FeeCollectionController {
    private final FeeCollectionService feeCollectionService;

    @Autowired
    public FeeCollectionController(FeeCollectionService feeCollectionService) {
        this.feeCollectionService = feeCollectionService;
    }

    @PostMapping("/collect")
    public ResponseEntity<FeeCollectionResponseDTO> collectFee(@RequestBody @Valid FeeCollectionRequestDTO feeCollectionRequestDTO) {
        FeeCollectionResponseDTO collectedFee = feeCollectionService.collectFee(feeCollectionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(collectedFee);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<FeeCollectionResponseDTO> getCollectedFeeByStudentId(@PathVariable String studentId) {
        return feeCollectionService.getCollectedFeeByStudentId(studentId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
