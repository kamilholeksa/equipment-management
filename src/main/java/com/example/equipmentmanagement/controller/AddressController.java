package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.address.AddressDto;
import com.example.equipmentmanagement.dto.address.AddressSaveDto;
import com.example.equipmentmanagement.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping()
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return ResponseEntity.ok(this.addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.addressService.getAddress(id));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressSaveDto dto) {
        AddressDto result = this.addressService.createAddress(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable("id") Long id, @Valid @RequestBody AddressSaveDto dto) {
        AddressDto result = this.addressService.updateAddress(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") Long id) {
        this.addressService.deleteAddress(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
