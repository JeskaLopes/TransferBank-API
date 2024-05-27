package com.transferbank.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.transferbank.api.model.Transfer;
import com.transferbank.api.repository.TransferRepository;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Transfer createTransfer(Transfer transfer) {
        BigDecimal transferTax = calculateTransferTax(transfer.getTransferDate(), transfer.getAmount());

        if (transferTax.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Taxa de transferência não aplicável");
        }

        BigDecimal totalAmount = transfer.getAmount().add(transferTax);
        totalAmount = totalAmount.setScale(2, RoundingMode.HALF_UP);
        transfer.setAmount(totalAmount);

        transfer.setSchedulingDate(LocalDate.now());

        return transferRepository.save(transfer);
    }

    private BigDecimal calculateTransferTax(LocalDate transferDate, BigDecimal transferAmount) {
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), transferDate);

        if (daysDifference == 0) {
            BigDecimal taxPercentage = BigDecimal.valueOf(0.025);
            return BigDecimal.valueOf(3.00).add(transferAmount.multiply(taxPercentage));
        } else if (daysDifference >= 1 && daysDifference <= 10) {
            return BigDecimal.valueOf(12.00);
        } else if (daysDifference >= 11 && daysDifference <= 20) {
            BigDecimal taxPercentage = BigDecimal.valueOf(0.086);
            return transferAmount.multiply(taxPercentage);
        } else if (daysDifference >= 21 && daysDifference <= 30) {
            BigDecimal taxPercentage = BigDecimal.valueOf(0.069);
            return transferAmount.multiply(taxPercentage);
        } else if (daysDifference >= 31 && daysDifference <= 40) {
            BigDecimal taxPercentage = BigDecimal.valueOf(0.047);
            return transferAmount.multiply(taxPercentage);
        } else if (daysDifference >= 41 && daysDifference <= 50) {
            BigDecimal taxPercentage = BigDecimal.valueOf(0.017);
            return transferAmount.multiply(taxPercentage);
        } else {
            return BigDecimal.valueOf(3.00);
        }
    }
}