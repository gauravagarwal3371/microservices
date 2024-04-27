package com.codergv.feecollms.dto;

import java.time.LocalDateTime;

public class FeeCollectionResponseDTO {
    private String studentId;
    private double amountPaid;
    private LocalDateTime timestamp;
    private String cardType;
    private String cardNumber;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "FeeCollectionResponseDTO{" +
                "studentId='" + studentId + '\'' +
                ", amountPaid=" + amountPaid +
                ", timestamp=" + timestamp +
                ", cardType='" + cardType + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
