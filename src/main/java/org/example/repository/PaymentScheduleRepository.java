package org.example.repository;

import org.example.table.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    List<PaymentSchedule> findByLoanApplicationIdOrderByPaymentNumberAsc(Long applicationId);
}
