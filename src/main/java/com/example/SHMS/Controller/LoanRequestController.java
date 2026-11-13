package com.example.SHMS.Controller;

import com.example.SHMS.Model.LoanRequest;
import com.example.SHMS.Model.LoanStatus;
import com.example.SHMS.Model.User;
import com.example.SHMS.Service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loanRequest")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LoanRequestController {

    @Autowired
    private LoanRequestService loanRequestService;

    @GetMapping
    public List<LoanRequest> getAllLoanRequest() {
        return loanRequestService.findAllLoan();
    }

    @GetMapping("/{id}")
    public LoanRequest getLoanById(@PathVariable Long id) {
        return loanRequestService.findLoanById(id);
    }

    @PostMapping
    public LoanRequest createLoanRequest(@RequestBody LoanRequest loanRequest) {
        return loanRequestService.createLoanRequest(loanRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteLoanRequest(@PathVariable Long id) {
       return loanRequestService.deleteLoanRequest(id);
    }

    @PutMapping("/{id}")
    public LoanRequest updateLoanRequest(@PathVariable Long id, @RequestBody LoanRequest LoanRequestDetails){
        return loanRequestService.updateLoanRequest(id, LoanRequestDetails);


}


    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long id, @RequestBody User approvingUser){
        try {
            LoanRequest result = loanRequestService.approveByHr(id, approvingUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLoan(@PathVariable Long id, @RequestBody User rejectingUser){
        try {
            LoanRequest result = loanRequestService.rejectLoan(id, rejectingUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public List<LoanRequest> getLoansByStatus(@PathVariable LoanStatus status) {
        return loanRequestService.findByStatus(status);
    }
}
