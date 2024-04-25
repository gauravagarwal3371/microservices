package com.codergv.receiptms.mapper;

import com.codergv.receiptms.domain.ReceiptDomain;
import com.codergv.receiptms.entity.ReceiptDAO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptDomainAndDaoMapper {

    ReceiptDAO toEntity(ReceiptDomain receiptDomain);
    ReceiptDomain toDomain(ReceiptDAO feeCollectionDAO);

}
