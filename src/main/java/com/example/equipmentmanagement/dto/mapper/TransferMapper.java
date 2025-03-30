package com.example.equipmentmanagement.dto.mapper;

import com.example.equipmentmanagement.dto.TransferDto;
import com.example.equipmentmanagement.dto.TransferSaveDto;
import com.example.equipmentmanagement.model.Equipment;
import com.example.equipmentmanagement.model.Transfer;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.model.enumeration.TransferStatus;

public class TransferMapper {

    private TransferMapper() {
    }

    public static TransferDto toDto(Transfer transfer) {
        TransferDto dto = new TransferDto();
        dto.setId(transfer.getId());
        dto.setRequestDate(transfer.getRequestDate());
        dto.setDecisionDate(transfer.getDecisionDate());
        dto.setStatus(transfer.getStatus().toString());
        dto.setCreatedBy(transfer.getCreatedBy());
        dto.setCreatedDate(transfer.getCreatedDate());
        dto.setLastModifiedBy(transfer.getLastModifiedBy());
        dto.setLastModifiedDate(transfer.getLastModifiedDate());

        if (transfer.getEquipment() != null) {
            dto.setEquipment(EquipmentMapper.toDto(transfer.getEquipment()));
        }

        if (transfer.getTransferor() != null) {
            dto.setTransferor(UserMapper.toDto(transfer.getTransferor()));
        }

        if (transfer.getObtainer() != null) {
            dto.setObtainer(UserMapper.toDto(transfer.getObtainer()));
        }

        return dto;
    }

    public static Transfer toEntity(TransferSaveDto dto, Equipment equipment, User transferor, User obtainer) {
        Transfer transfer = new Transfer();
        transfer.setRequestDate(dto.getRequestDate());
        transfer.setDecisionDate(dto.getDecisionDate());
        transfer.setStatus(TransferStatus.valueOf(dto.getStatus()));
        transfer.setEquipment(equipment);
        transfer.setTransferor(transferor);
        transfer.setObtainer(obtainer);

        return transfer;
    }
}
