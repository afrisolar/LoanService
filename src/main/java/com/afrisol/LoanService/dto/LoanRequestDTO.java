package com.afrisol.LoanService.dto;

import com.afrisol.LoanService.model.Status;
import jakarta.validation.constraints.*;
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
public class LoanRequestDTO {

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be in the past or present")
    private LocalDate startDate;

    @NotNull(message = "Loan end date cannot be null")
    @FutureOrPresent(message = "Loan end date must be in the future or present")
    private LocalDate loanEndDate;

    @NotNull(message = "Total loan amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total loan amount must be greater than 0")
    private BigDecimal totalLoanAmount;

    @NotNull(message = "Loan interest rate cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Loan interest rate must be greater than 0")
    @DecimalMax(value = "100.0", message = "Loan interest rate cannot exceed 100")
    private BigDecimal loanInterestRate;

    @NotNull(message = "Amount received cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Amount received cannot be negative")
    private BigDecimal amountReceived;

    @NotNull(message = "Target completion date cannot be null")
    @FutureOrPresent(message = "Target completion date must be in the future or present")
    private LocalDate targetCompletionDate;

    @Future(message = "Payoff date must be in the future")
    private LocalDate payOffDate;

    @NotNull(message = "Daily rate cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Daily rate cannot be negative")
    private BigDecimal dailyRate;

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Customer ID cannot be blank")
    private String customerId;

    @NotNull(message = "Status cannot be null")
    private Status status;
}
