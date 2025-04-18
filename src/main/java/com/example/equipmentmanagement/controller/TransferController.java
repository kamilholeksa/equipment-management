package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.transfer.TransferDto;
import com.example.equipmentmanagement.dto.transfer.TransferSaveDto;
import com.example.equipmentmanagement.dto.transfer.AcceptTransferRequest;
import com.example.equipmentmanagement.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping()
    public ResponseEntity<Page<TransferDto>> getAllTransfers(Pageable pageable) {
        return ResponseEntity.ok(this.transferService.getAllTransfers(pageable));
    }

    @GetMapping("/to-accept")
    public ResponseEntity<Page<TransferDto>> getTransfersToAccept(Pageable pageable) {
        return ResponseEntity.ok(this.transferService.getTransfersToAccept(pageable));
    }

    @GetMapping("/my-transfers")
    public ResponseEntity<Page<TransferDto>> getMyTransfers(Pageable pageable) {
        return ResponseEntity.ok(this.transferService.getMyTransfers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDto> getTransfer(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.transferService.getTransfer(id));
    }

    @PostMapping
    public ResponseEntity<TransferDto> createTransfer(@Valid @RequestBody TransferSaveDto dto) {
        TransferDto result = this.transferService.createTransfer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferDto> updateTransfer(@PathVariable("id") Long id, @Valid @RequestBody TransferSaveDto dto) {
        TransferDto result = this.transferService.updateTransfer(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable("id") Long id) {
        this.transferService.deleteTransfer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ResponseMessage> acceptTransfer(@PathVariable("id") Long id, @Valid @RequestBody AcceptTransferRequest request) {
        this.transferService.acceptTransfer(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Transfer accepted successfully"));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ResponseMessage> rejectTransfer(@PathVariable("id") Long id) {
        this.transferService.rejectTransfer(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Transfer rejected successfully"));
    }
}
