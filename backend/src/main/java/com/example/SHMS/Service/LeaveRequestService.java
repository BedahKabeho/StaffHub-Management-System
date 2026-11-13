package com.example.SHMS.Service;

import com.example.SHMS.Model.*;
import com.example.SHMS.Repository.LeaveRequestRepository;
import com.example.SHMS.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public List<LeaveRequest> findAllLeaveRequest(){
        List<LeaveRequest> allRequests = leaveRequestRepository.findAll();
        // FIXED: Properly check for active users
        return allRequests.stream()
                .filter(request -> request.getUser() != null &&
                        (request.getUser().getIsDeleted() == null ||
                                !request.getUser().getIsDeleted()))
                .collect(Collectors.toList());
    }

    public LeaveRequest findLeaveRequestById(Long id){
        return leaveRequestRepository.findById(id).orElseThrow(()-> new RuntimeException("Leave request not found"));
    }

    // FIXED: Allow HR to delete any leave request regardless of status
    public String delete(Long id){
        LeaveRequest leaveRequest = findLeaveRequestById(id);
        leaveRequestRepository.delete(leaveRequest);
        return "Leave request deleted successfully";
    }

    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails){
        LeaveRequest leaveRequest = findLeaveRequestById(id);

        if (leaveRequestDetails.getLeaveType() != null) {
            leaveRequest.setLeaveType(leaveRequestDetails.getLeaveType());
        }
        if (leaveRequestDetails.getStartDate() != null) {
            leaveRequest.setStartDate(leaveRequestDetails.getStartDate());
        }
        if (leaveRequestDetails.getEndDate() != null) {
            leaveRequest.setEndDate(leaveRequestDetails.getEndDate());
        }
        if (leaveRequestDetails.getTotalDays() != null) {
            leaveRequest.setTotalDays(leaveRequestDetails.getTotalDays());
        }
        if (leaveRequestDetails.getReason() != null) {
            leaveRequest.setReason(leaveRequestDetails.getReason());
        }
        if (leaveRequestDetails.getStatus() != null) {
            leaveRequest.setStatus(leaveRequestDetails.getStatus());
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getRequestByStatus(LeaveStatus status){
        return leaveRequestRepository.findByStatus(status);
    }

    public List<LeaveRequest> getRequestByType(LeaveType type){
        return leaveRequestRepository.findByLeaveType(type);
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest){
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest approveByManager(Long leaveId, User manager){
        LeaveRequest leaveRequest = findLeaveRequestById(leaveId);

        if (manager.getRole() != UserRole.DEPT_MANAGER && manager.getRole() != UserRole.HR_MANAGER){
            throw new IllegalStateException("Only managers can approve leave request");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING){
            throw new IllegalStateException("Only pending requests can be approved");
        }

        leaveRequest.setManagerApproval(true);
        leaveRequest.setApprovedByManager(manager);
        leaveRequest.setStatus(LeaveStatus.APPROVED);

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest approvedByHr(Long leaveId, User hrManager){
        LeaveRequest leaveRequest = findLeaveRequestById(leaveId);

        if (hrManager.getRole() != UserRole.HR_MANAGER){
            throw new IllegalStateException("Only HR managers can give final approval");
        }

        if (leaveRequest.getStatus() != LeaveStatus.PENDING){
            throw new IllegalStateException("Only pending requests can be approved");
        }

        leaveRequest.setHrApproval(true);
        leaveRequest.setApprovedByHr(hrManager);
        leaveRequest.setStatus(LeaveStatus.APPROVED);

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest rejectLeave(Long leaveId, User rejectingUser){
        LeaveRequest leaveRequest = findLeaveRequestById(leaveId);

        if(rejectingUser.getRole() != UserRole.DEPT_MANAGER && rejectingUser.getRole() != UserRole.HR_MANAGER){
            throw new IllegalStateException("Only managers or HR can reject leave request");
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        return leaveRequestRepository.save(leaveRequest);
    }
    // Add this method to your LeaveRequestService class
    public List<LeaveRequest> findLeaveRequestsByDepartment(Long departmentId) {
        System.out.println("=== FINDING LEAVE REQUESTS BY DEPARTMENT ===");
        System.out.println("Department ID: " + departmentId);

        List<LeaveRequest> allRequests = leaveRequestRepository.findAll();
        System.out.println("Total leave requests in system: " + allRequests.size());

        List<LeaveRequest> departmentRequests = allRequests.stream()
                .filter(request -> {
                    if (request.getUser() == null) {
                        System.out.println("Leave request has no user assigned");
                        return false;
                    }

                    if (request.getUser().getDepartment() == null) {
                        System.out.println("User " + request.getUser().getFirstName() + " has no department");
                        return false;
                    }

                    boolean userNotDeleted = request.getUser().getIsDeleted() == null || !request.getUser().getIsDeleted();
                    boolean departmentMatches = request.getUser().getDepartment().getId().equals(departmentId);

                    if (departmentMatches) {
                        System.out.println("Found leave request from user: " + request.getUser().getFirstName() +
                                " in department: " + request.getUser().getDepartment().getDepartmentName());
                    }

                    return userNotDeleted && departmentMatches;
                })
                .collect(Collectors.toList());

        System.out.println("Leave requests found in department: " + departmentRequests.size());
        System.out.println("=== END FINDING LEAVE REQUESTS ===");

        return departmentRequests;
    }

}