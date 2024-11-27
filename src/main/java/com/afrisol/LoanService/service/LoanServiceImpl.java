package com.afrisol.LoanService.service;


import com.afrisol.LoanService.dto.LoanRequestDTO;
import com.afrisol.LoanService.dto.LoanResponseDTO;
import com.afrisol.LoanService.exception.LoanNotFoundException;
import com.afrisol.LoanService.model.Loan;
import com.afrisol.LoanService.repository.LoanRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Mono<LoanResponseDTO> addLoan(LoanRequestDTO loanRequestDTO, String requestID) {
        if (loanRequestDTO == null) {
            return Mono.error(new IllegalArgumentException("LoanRequestDTO cannot be null"));
        }
        return loanRepository.save(mapToLoanEntity(loanRequestDTO))
                .doOnNext(savedLoan ->
                        log.info("Successfully added loan with ID: {} for request ID: {}", savedLoan.getLoanId(), requestID)
                )
                .map(this::mapToLoanResponseDTO);
    }

    @Override
    public Mono<LoanResponseDTO> updateLoan(@Valid LoanRequestDTO loanRequestDTO, Integer loanId, String requestID) {
        log.info("Updating loan with ID: {} Request ID: {}", loanId, requestID);

        return loanRepository.findById(loanId)
                .switchIfEmpty(Mono.error(new LoanNotFoundException("Loan not found with ID: " + loanId)))
                .flatMap(existingLoan -> {
                    // Update loan fields
                    existingLoan.setStartDate(loanRequestDTO.getStartDate());
                    existingLoan.setLoanEndDate(loanRequestDTO.getLoanEndDate());
                    existingLoan.setTotalLoanAmount(loanRequestDTO.getTotalLoanAmount());
                    existingLoan.setLoanInterestRate(loanRequestDTO.getLoanInterestRate());
                    existingLoan.setAmountReceived(loanRequestDTO.getAmountReceived());
                    existingLoan.setTargetCompletionDate(loanRequestDTO.getTargetCompletionDate());
                    existingLoan.setPayOffDate(loanRequestDTO.getPayOffDate());
                    existingLoan.setDailyRate(loanRequestDTO.getDailyRate());
                    existingLoan.setProductId(loanRequestDTO.getProductId());
                    existingLoan.setCustomerId(loanRequestDTO.getCustomerId());
                    existingLoan.setStatus(loanRequestDTO.getStatus());
                    return loanRepository.save(existingLoan);
                })
                .doOnNext(updatedLoan ->
                        log.info("Successfully updated loan with ID: {} for request ID: {}", updatedLoan.getLoanId(), requestID)
                )
                .map(this::mapToLoanResponseDTO);
    }

    @Override
    public Mono<Void> deleteLoan(Integer loanId, String requestID) {
        log.info("Deleting loan with ID: {} Request ID: {}", loanId, requestID);
        if (loanId == null) {
            return Mono.error(new IllegalArgumentException("Invalid loan ID"));
        }
        return loanRepository.findById(loanId)
                .switchIfEmpty(Mono.error(new LoanNotFoundException("Loan not found with ID: " + loanId)))
                .flatMap(loanRepository::delete)
                .doOnSuccess(unused -> log.info("Successfully deleted loan with ID: {} for request ID: {}", loanId, requestID));
    }

    @Override
    public Flux<LoanResponseDTO> getAllLoans(String requestID) {
        log.info("Retrieving all loans for request ID: {}", requestID);
        return loanRepository.findAll().map(this::mapToLoanResponseDTO);
    }

    @Override
    public Mono<LoanResponseDTO> getLoan(Integer loanId, String requestID) {
        if (loanId == null) {
            return Mono.error(new IllegalArgumentException("Loan ID cannot be null"));
        }
        log.info("Searching for loan with ID: {}", loanId);
        return loanRepository.findById(loanId)
                .switchIfEmpty(Mono.error(new LoanNotFoundException("Loan not found with ID: " + loanId)))
                .map(this::mapToLoanResponseDTO)
                .doOnNext(loan ->
                        log.info("Successfully retrieved loan with ID: {} for request ID: {}", loanId, requestID)
                );
    }

    private Loan mapToLoanEntity(LoanRequestDTO dto) {
        return Loan.builder()
                .startDate(dto.getStartDate())
                .loanEndDate(dto.getLoanEndDate())
                .totalLoanAmount(dto.getTotalLoanAmount())
                .loanInterestRate(dto.getLoanInterestRate())
                .amountReceived(dto.getAmountReceived())
                .targetCompletionDate(dto.getTargetCompletionDate())
                .payOffDate(dto.getPayOffDate())
                .dailyRate(dto.getDailyRate())
                .productId(dto.getProductId())
                .customerId(dto.getCustomerId())
                .status(dto.getStatus())
                .build();
    }

    private LoanResponseDTO mapToLoanResponseDTO(Loan loan) {
        return LoanResponseDTO.builder()
                .loanId(loan.getLoanId())
                .startDate(loan.getStartDate())
                .loanEndDate(loan.getLoanEndDate())
                .totalLoanAmount(loan.getTotalLoanAmount())
                .loanInterestRate(loan.getLoanInterestRate())
                .amountReceived(loan.getAmountReceived())
                .targetCompletionDate(loan.getTargetCompletionDate())
                .payOffDate(loan.getPayOffDate())
                .dailyRate(loan.getDailyRate())
                .productId(loan.getProductId())
                .customerId(loan.getCustomerId())
                .status(loan.getStatus())
                .build();
    }
}
