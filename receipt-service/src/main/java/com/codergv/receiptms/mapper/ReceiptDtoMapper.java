package com.codergv.receiptms.mapper;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptDtoMapper {

    @Mapping(source="studentDTO.studentId",target="studentId")
    @Mapping(source="studentDTO.school.name",target="school")
    ReceiptDTO receiptDTO(StudentDTO studentDTO, FeeCollectionDTO feeCollectionDTO);
}
