package com.example.SHMS.Repository;

import com.example.SHMS.Model.LoanRequest;
import com.example.SHMS.Model.LoanStatus;
import com.example.SHMS.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    List<LoanRequest> findByStatus(LoanStatus status);
     List<LoanRequest> findByUser(User user);
}
