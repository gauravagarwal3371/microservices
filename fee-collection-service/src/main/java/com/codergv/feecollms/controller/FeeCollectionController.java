package com.codergv.feecollms.controller;

import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.exception.FeeCollectionException;
import com.codergv.feecollms.exception.NotFoundException;
import com.codergv.feecollms.service.FeeCollectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("/fees")
public class FeeCollectionController {
    private final FeeCollectionService feeCollectionService;

    @Autowired
    public FeeCollectionController(FeeCollectionService feeCollectionService) {
        this.feeCollectionService = feeCollectionService;
    }

    @PostMapping("/collect")
    public ResponseEntity<?> collectFeeAndGenerateReceipt(@RequestBody @Valid FeeCollectionRequestDTO feeCollectionRequestDTO) {
        ResponseEntity<?> receiptResponseEntity = null;
        try {
            feeCollectionService.setShouldExecuteGenerateReceipt(true);
            receiptResponseEntity = feeCollectionService.collectFeeAndGenerateReceipt(feeCollectionRequestDTO);
        }catch (WebClientResponseException e){
            throw new FeeCollectionException("Transaction successful, but receipt not generated. " +
                    "Please fetch from the app in some time");
        }
        if (receiptResponseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(receiptResponseEntity.getBody());
        }

        throw new FeeCollectionException("Fees not collected for studentId: " + feeCollectionRequestDTO.getStudentId());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<FeeCollectionResponseDTO>> getCollectedFeeByStudentId(@PathVariable String studentId) {
        return feeCollectionService.getCollectedFeeByStudentId(studentId).map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Fees not collected for studentId: " + studentId));
    }
}
