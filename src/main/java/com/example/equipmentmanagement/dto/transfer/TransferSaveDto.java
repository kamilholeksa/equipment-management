package com.example.equipmentmanagement.dto.transfer;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransferSaveDto {
    private LocalDate requestDate;
    private LocalDate decisionDate;
    private String status;
    private Long equipmentId;
    private Long transferorId;
    @NotNull(message = "Użytkownik docelowy jest wymagany")
    private Long obtainerId;
}
