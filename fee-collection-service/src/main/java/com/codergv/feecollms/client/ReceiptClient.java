package com.codergv.feecollms.client;

import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.dto.ReceiptDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ReceiptClient {

    @PostExchange("receipts/fee/collect/receipt")
    public ResponseEntity<?> generateReceipt(@RequestBody FeeCollectionResponseDTO feeCollectionResponseDTO);

}
