package com.codergv.feecollms.controller;

import com.codergv.feecollms.client.ReceiptClient;
import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.service.FeeCollectionService;
import jakarta.validation.Valid;
import jakarta.ws.rs.WebApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Transaction successful, but receipt not generated. Please fetch from the app in some time");
        }
        if (receiptResponseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(receiptResponseEntity.getBody());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while collecting fees.");
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<FeeCollectionResponseDTO> getCollectedFeeByStudentId(@PathVariable String studentId) {
        return feeCollectionService.getCollectedFeeByStudentId(studentId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
