package com.codergv.receiptms.mapper;

import com.codergv.receiptms.domain.ReceiptDomain;
import com.codergv.receiptms.dto.ReceiptDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptDtoAndDomainMapper {

    ReceiptDomain toDomain(ReceiptDTO receiptDTO);

    ReceiptDTO toDTO(ReceiptDomain feeCollectionDomain);

}
