package com.transferbank.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transferbank.api.model.Transfer;
import com.transferbank.api.repository.TransferRepository;
import com.transferbank.api.service.TransferService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "http://localhost:8081")
public class TransferController {
	@Autowired
    private TransferRepository transferRepository;
    
	@Autowired
    private TransferService transferService;
    
    @GetMapping
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAllByOrderByTransferDateDesc();
    }
    
    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer savedTransfer = transferService.createTransfer(transfer);
        return ResponseEntity.ok(savedTransfer);
    }
}