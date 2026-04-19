package org.example.repository;

import org.example.table.LoanApplication;
import org.example.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    boolean existsByPersonalCodeAndStatusIn(String personalCode, List<Status> activeStatuses);
}
