package com.codergv.receiptms.client;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface FeeCollectionClient {

    @GetExchange("fees/{studentId}")
    public ResponseEntity<FeeCollectionDTO> getCollectedFeeByStudentId(@PathVariable String studentId);
}
