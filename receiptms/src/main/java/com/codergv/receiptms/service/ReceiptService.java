package com.codergv.receiptms.service;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.dto.StudentDTO;
import com.codergv.receiptms.entity.Receipt;
import com.codergv.receiptms.repository.ReceiptRepository;
import com.codergv.receiptms.util.ReferenceNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private static final String STUDENT_SERVICE_URL = "http://localhost:9001/students/{id}";
    private static final String FEE_COLLECTION_SERVICE_URL = "http://localhost:9000/fees/{id}";

    private final RestTemplate restTemplate;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository,RestTemplate restTemplate) {
        this.receiptRepository = receiptRepository;
        this.restTemplate = restTemplate;
    }

    public ReceiptDTO generateReceipt(String studentId) {

        StudentDTO studentDTO = restTemplate.getForObject(STUDENT_SERVICE_URL, StudentDTO.class, studentId);
        FeeCollectionDTO feeCollectionDTO = restTemplate.getForObject(FEE_COLLECTION_SERVICE_URL, FeeCollectionDTO.class, studentId);
        if(feeCollectionDTO ==null)
            return null;
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setStudentId(studentId);
        receiptDTO.setName(studentDTO.getName());
        receiptDTO.setGrade(studentDTO.getGrade());
        receiptDTO.setSchool(studentDTO.getSchool().getName());
        receiptDTO.setReference(ReferenceNumberUtil.getRandomReferenceNumber());
        receiptDTO.setTimestamp(feeCollectionDTO.getTimestamp());
        receiptDTO.setAmountPaid(feeCollectionDTO.getAmountPaid());
        receiptDTO.setCard(feeCollectionDTO.getCardNumber());
        receiptDTO.setCardType(feeCollectionDTO.getCardType());

        return receiptDTO;
    }

    public Receipt getReceiptById(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found with id: " + receiptId));
    }

}