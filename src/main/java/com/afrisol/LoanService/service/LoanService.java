package com.afrisol.LoanService.service;

import com.afrisol.LoanService.dto.LoanRequestDTO;
import com.afrisol.LoanService.dto.LoanResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanService {
    Mono<LoanResponseDTO> addLoan(LoanRequestDTO loanRequestDTO, String requestID);

    Mono<LoanResponseDTO> updateLoan(LoanRequestDTO loanRequestDTO, Integer loanId, String requestID);

    Mono<Void> deleteLoan(Integer loanId, String requestID);

    Flux<LoanResponseDTO> getAllLoans(String requestID);

    Mono<LoanResponseDTO> getLoan(Integer loanId, String requestID);
}
