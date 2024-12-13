package com.afrisol.LoanService.service;

import com.afrisol.LoanService.dto.LoanRequestDTO;
import com.afrisol.LoanService.dto.LoanResponseDTO;
import com.afrisol.LoanService.model.Loan;
import com.afrisol.LoanService.model.Status;
import com.afrisol.LoanService.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Loan sampleLoan;
    private LoanRequestDTO sampleRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample Loan object
        sampleLoan = Loan.builder()
                .loanId(1)
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

        // Sample LoanRequestDTO object
        sampleRequestDTO = LoanRequestDTO.builder()
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
    void addLoan() {
        when(loanRepository.save(any(Loan.class))).thenReturn(Mono.just(sampleLoan));

        Mono<LoanResponseDTO> result = loanService.addLoan(sampleRequestDTO, "req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getProductId().equals("PROD123"))
                .verifyComplete();

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void updateLoan() {
        when(loanRepository.findById(eq(1))).thenReturn(Mono.just(sampleLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(Mono.just(sampleLoan));

        Mono<LoanResponseDTO> result = loanService.updateLoan(sampleRequestDTO, 1, "req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getDailyRate().equals(sampleLoan.getDailyRate()))
                .verifyComplete();

        verify(loanRepository, times(1)).findById(1);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void deleteLoan() {
        when(loanRepository.findById(eq(1))).thenReturn(Mono.just(sampleLoan));
        when(loanRepository.delete(any(Loan.class))).thenReturn(Mono.empty());

        Mono<Void> result = loanService.deleteLoan(1, "req-123");

        StepVerifier.create(result)
                .verifyComplete();

        verify(loanRepository, times(1)).findById(1);
        verify(loanRepository, times(1)).delete(any(Loan.class));
    }

    @Test
    void getAllLoans() {
        when(loanRepository.findAll()).thenReturn(Flux.just(sampleLoan));

        Flux<LoanResponseDTO> result = loanService.getAllLoans("req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getProductId().equals("PROD123"))
                .verifyComplete();

        verify(loanRepository, times(1)).findAll();
    }

    @Test
    void getLoan() {
        when(loanRepository.findById(eq(1))).thenReturn(Mono.just(sampleLoan));

        Mono<LoanResponseDTO> result = loanService.getLoan(1, "req-123");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getLoanEndDate().equals(sampleLoan.getLoanEndDate()))
                .verifyComplete();

        verify(loanRepository, times(1)).findById(1);
    }
}
