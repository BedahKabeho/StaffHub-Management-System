package com.example.SHMS.Repository;

import com.example.SHMS.Model.LeaveRequest;
import com.example.SHMS.Model.LeaveStatus;
import com.example.SHMS.Model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {


    List<LeaveRequest> findByStatus(LeaveStatus status);
    List<LeaveRequest> findByLeaveType(LeaveType type);
}
