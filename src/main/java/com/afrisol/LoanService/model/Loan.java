package com.afrisol.LoanService.model;

import com.afrisol.LoanService.external.Customer;
import com.afrisol.LoanService.external.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Loan {
    private Integer loanId;
    private LocalDate startDate;
    private LocalDate loanEndDate;
    private BigDecimal totalLoanAmount;
    private BigDecimal loanInterestRate;
    private BigDecimal amountReceived;
    private LocalDate targetCompletionDate;
    private LocalDate payOffDate;
    private BigDecimal dailyRate;
    private String productId;
    private String customerId;
    private Status status;
}
