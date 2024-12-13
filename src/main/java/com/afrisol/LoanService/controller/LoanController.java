package com.afrisol.LoanService.controller;

import com.afrisol.LoanService.dto.LoanRequestDTO;
import com.afrisol.LoanService.dto.LoanResponseDTO;
import com.afrisol.LoanService.exception.LoanNotFoundException;
import com.afrisol.LoanService.service.LoanService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/loans")
@Slf4j
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public Flux<LoanResponseDTO> getAllLoans() {
        String requestID = UUID.randomUUID().toString();
        log.info("Retrieving all loans: {}", requestID);
        return loanService.getAllLoans(requestID);
    }

    @GetMapping("/{loanId}")
    public Mono<ResponseEntity<LoanResponseDTO>> getLoan(@PathVariable Integer loanId) {
        String requestID = UUID.randomUUID().toString();
        log.info("Retrieving loan with ID: {} and request ID: {}", loanId, requestID);
        return loanService.getLoan(loanId, requestID)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<LoanResponseDTO>> addLoan(@RequestBody @Valid LoanRequestDTO loanRequestDTO) {
        String requestID = UUID.randomUUID().toString();
        log.info("Adding loan for request ID: {}", requestID);
        return loanService.addLoan(loanRequestDTO, requestID)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{loanId}")
    public Mono<ResponseEntity<LoanResponseDTO>> updateLoan(
            @PathVariable Integer loanId,
            @RequestBody @Valid LoanRequestDTO loanRequestDTO) {
        String requestID = UUID.randomUUID().toString();
        log.info("Updating loan with ID: {} and request ID: {}", loanId, requestID);
        return loanService.updateLoan(loanRequestDTO, loanId, requestID)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{loanId}")
    public Mono<ResponseEntity<Object>> deleteLoan(@PathVariable Integer loanId) {
        String requestID = UUID.randomUUID().toString();
        log.info("Deleting loan with ID: {} and request ID: {}", loanId, requestID);
        return loanService.deleteLoan(loanId, requestID)
                .then(Mono.just(ResponseEntity.noContent().<Object>build()));
    }
}