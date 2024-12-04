package com.afrisol.LoanService.controller;

import com.afrisol.LoanService.dto.LoanRequestDTO;
import com.afrisol.LoanService.dto.LoanResponseDTO;
import com.afrisol.LoanService.service.LoanService;
import com.afrisol.LoanService.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private WebTestClient webTestClient;

    private LoanResponseDTO sampleLoanResponse;
    private LoanRequestDTO sampleLoanRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize WebTestClient
        webTestClient = WebTestClient.bindToController(loanController).build();

        // Sample LoanResponseDTO
        sampleLoanResponse = LoanResponseDTO.builder()
                .startDate(LocalDate.now())
                .loanEndDate(LocalDate.now().plusDays(30))
                .totalLoanAmount(BigDecimal.valueOf(10000))
                .loanInterestRate(BigDecimal.valueOf(5.5))
                .amountReceived(BigDecimal.valueOf(2000))
                .targetCompletionDate(LocalDate.now().plusMonths(1))
                .payOffDate(LocalDate.now().plusMonths(1))
                .dailyRate(BigDecimal.valueOf(10))
                .productId("PROD123")
                .customerId("CUST123")
                .status(Status.ACTIVE)
                .build();

        // Sample LoanRequestDTO
        sampleLoanRequest = LoanRequestDTO.builder()
                .startDate(LocalDate.now())
                .loanEndDate(LocalDate.now().plusDays(30))
                .totalLoanAmount(BigDecimal.valueOf(10000))
                .loanInterestRate(BigDecimal.valueOf(5.5))
                .amountReceived(BigDecimal.valueOf(2000))
                .targetCompletionDate(LocalDate.now().plusMonths(1))
                .payOffDate(LocalDate.now().plusMonths(1))
                .dailyRate(BigDecimal.valueOf(10))
                .productId("PROD123")
                .customerId("CUST123")
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    void getAllLoans() {
        when(loanService.getAllLoans(anyString())).thenReturn(Flux.just(sampleLoanResponse));

        webTestClient.get()
                .uri("/api/v1/loans")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(LoanResponseDTO.class)
                .hasSize(1)
                .consumeWith(response -> {
                    LoanResponseDTO loan = response.getResponseBody().get(0);
                    assertEquals("PROD123", loan.getProductId());
                });

        verify(loanService, times(1)).getAllLoans(anyString());
    }

    @Test
    void getLoan() {
        when(loanService.getLoan(eq(1), anyString())).thenReturn(Mono.just(sampleLoanResponse));

        webTestClient.get()
                .uri("/api/v1/loans/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanResponseDTO.class)
                .consumeWith(response -> {
                    LoanResponseDTO loan = response.getResponseBody();
                    assertEquals("PROD123", loan.getProductId());
                });

        verify(loanService, times(1)).getLoan(eq(1), anyString());
    }

    @Test
    void addLoan() {
        when(loanService.addLoan(any(LoanRequestDTO.class), anyString())).thenReturn(Mono.just(sampleLoanResponse));

        webTestClient.post()
                .uri("/api/v1/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleLoanRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanResponseDTO.class)
                .consumeWith(response -> {
                    LoanResponseDTO loan = response.getResponseBody();
                    assertEquals("PROD123", loan.getProductId());
                });

        verify(loanService, times(1)).addLoan(any(LoanRequestDTO.class), anyString());
    }

    @Test
    void updateLoan() {
        when(loanService.updateLoan(any(LoanRequestDTO.class), eq(1), anyString())).thenReturn(Mono.just(sampleLoanResponse));

        webTestClient.put()
                .uri("/api/v1/loans/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleLoanRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoanResponseDTO.class)
                .consumeWith(response -> {
                    LoanResponseDTO loan = response.getResponseBody();
                    assertEquals("PROD123", loan.getProductId());
                });

        verify(loanService, times(1)).updateLoan(any(LoanRequestDTO.class), eq(1), anyString());
    }

    @Test
    void deleteLoan() {
        when(loanService.deleteLoan(eq(1), anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/v1/loans/1")
                .exchange()
                .expectStatus().isNoContent();

        verify(loanService, times(1)).deleteLoan(eq(1), anyString());
    }
}
