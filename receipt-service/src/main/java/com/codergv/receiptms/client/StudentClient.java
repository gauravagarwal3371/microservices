package com.codergv.receiptms.client;

import com.codergv.receiptms.dto.StudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface StudentClient {

    @GetExchange("students/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable String id);

}
