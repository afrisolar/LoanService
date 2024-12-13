package com.afrisol.LoanService.repository;

import com.afrisol.LoanService.model.Loan;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanRepository extends ReactiveCrudRepository<Loan, Integer> {
}
