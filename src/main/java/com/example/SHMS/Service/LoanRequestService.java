package com.example.SHMS.Service;

import com.example.SHMS.Model.LoanRequest;
import com.example.SHMS.Model.LoanStatus;
import com.example.SHMS.Model.User;
import com.example.SHMS.Model.UserRole;
import com.example.SHMS.Repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanRequestService {

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    private static final BigDecimal MAX_LOAN_AMOUNT = new BigDecimal("5000000");
    private static final int MIN_REPAYMENT_PERIOD = 3;
    private static final int MAX_REPAYMENT_PERIOD = 24;

    public List<LoanRequest> findAllLoan(){
        List<LoanRequest> allRequests = loanRequestRepository.findAll();
        return allRequests.stream()
                .filter(request -> request.getUser() != null &&
                        !Boolean.TRUE.equals(request.getUser().getIsDeleted())) // CHANGED: Show only ACTIVE users
                .collect(Collectors.toList());
    }
    public LoanRequest findLoanById(Long id){
        return loanRequestRepository.findById(id).orElseThrow(()-> new RuntimeException("Loan not found"));
    }

    public LoanRequest updateLoanRequest(Long id, LoanRequest loanRequestDetails){
        LoanRequest loanRequest = findLoanById(id);

        if (loanRequestDetails.getLoanAmount() != null) {
            validateLoanAmount(loanRequestDetails.getLoanAmount());
            loanRequest.setLoanAmount(loanRequestDetails.getLoanAmount());
        }
        if (loanRequestDetails.getRepaymentPeriod() != null) {
            validateRepaymentPeriod(loanRequestDetails.getRepaymentPeriod());
            loanRequest.setRepaymentPeriod(loanRequestDetails.getRepaymentPeriod());
        }
        if (loanRequestDetails.getPurpose() != null && !loanRequestDetails.getPurpose().trim().isEmpty()) {
            validatePurpose(loanRequestDetails.getPurpose());
            loanRequest.setPurpose(loanRequestDetails.getPurpose());
        }
        if (loanRequestDetails.getStatus() != null) {
            loanRequest.setStatus(loanRequestDetails.getStatus());
        }

        return loanRequestRepository.save(loanRequest);
    }

    public LoanRequest createLoanRequest(LoanRequest loanRequest){
        return loanRequestRepository.save(loanRequest);
    }

    public List<LoanRequest> findByStatus(LoanStatus status){
        return loanRequestRepository.findByStatus(status);
    }

    // FIXED: Allow HR to delete any loan request regardless of status
    public String deleteLoanRequest(Long id){
        LoanRequest loanRequest = findLoanById(id);
        loanRequestRepository.delete(loanRequest);
        return "Loan request deleted successfully";
    }

    public List<LoanRequest> getLoanByUSer(User user){
        return loanRequestRepository.findByUser(user);
    }

    public LoanRequest approveByHr(Long id, User approvingUser){
        LoanRequest loan = findLoanById(id);

        if (approvingUser.getRole() != UserRole.HR_MANAGER) {
            throw new IllegalStateException("Only HR managers can approve loans");
        }

        if (loan.getStatus() != LoanStatus.PENDING){
            throw new IllegalStateException("Only pending loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedByHr(approvingUser);
        loan.setApprovalDate(LocalDateTime.now());

        return loanRequestRepository.save(loan);
    }

    public LoanRequest rejectLoan(Long id, User rejectingUser){
        LoanRequest loan = findLoanById(id);

        if(rejectingUser.getRole() != UserRole.HR_MANAGER){
            throw new IllegalStateException("Only HR managers can reject loans");
        }

        loan.setStatus(LoanStatus.REJECTED);
        return loanRequestRepository.save(loan);
    }

    private void validateLoanAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Loan amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if(amount.compareTo(MAX_LOAN_AMOUNT) > 0){
            throw new IllegalArgumentException("Loan amount cannot exceed " + MAX_LOAN_AMOUNT);
        }
    }

    private void validateRepaymentPeriod(Integer repaymentPeriod){
        if (repaymentPeriod == null) {
            throw new IllegalArgumentException("Repayment period is required");
        }
        if (repaymentPeriod < MIN_REPAYMENT_PERIOD || repaymentPeriod > MAX_REPAYMENT_PERIOD) {
            throw new IllegalArgumentException("Repayment period must be between " +
                    MIN_REPAYMENT_PERIOD + " and " + MAX_REPAYMENT_PERIOD + " months");
        }
    }

    private void validatePurpose(String purpose) {
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalArgumentException("Loan purpose is required");
        }
    }
}