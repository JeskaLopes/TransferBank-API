package com.transferbank.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transferbank.api.model.Transfer;
import com.transferbank.api.repository.TransferRepository;
import com.transferbank.api.service.TransferService;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @InjectMocks
    private TransferController transferController;

    @MockBean
    private TransferRepository transferRepository;

    @MockBean
    private TransferService transferService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
    }

    @Test
    @DisplayName("Retornar todas as Transferências")
    public void testGetAllTransfers() throws Exception {
        Transfer transfer1 = new Transfer(1L, "123", "456", new BigDecimal("100.00"), LocalDate.now());
        Transfer transfer2 = new Transfer(2L, "789", "012", new BigDecimal("200.00"), LocalDate.now());
        List<Transfer> transfers = Arrays.asList(transfer1, transfer2);

        when(transferRepository.findAllByOrderByTransferDateDesc()).thenReturn(transfers);

        mockMvc.perform(get("/api/transfers"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(transfers)));
    }

    @Test
    @DisplayName("Criar uma transferência")
    public void testCreateTransfer() throws Exception {
        Transfer transfer = new Transfer(1L, "123", "456", new BigDecimal("100.00"), LocalDate.now());

        when(transferService.createTransfer(any(Transfer.class))).thenReturn(transfer);

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transfer)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(transfer)));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}