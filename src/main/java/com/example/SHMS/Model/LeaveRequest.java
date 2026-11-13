package com.example.SHMS.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Long leaveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "Leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "manager_approval", nullable = false)
    private Boolean managerApproval = false;

    @Column(name = "hr_approval", nullable = false)
    private Boolean hrApproval = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_manager_id")
    private User approvedByManager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_hr_id")
    private User approvedByHr;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Validate required fields before saving
        if (startDate == null || endDate == null) {
            throw new IllegalStateException("Start date and end date are required");
        }

        // Auto-calculate total days if not set
        if (totalDays == null) {
            calculateTotalDays();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Set approval date when status changes to approved
        if (status == LeaveStatus.APPROVED && approvalDate == null) {
            approvalDate = LocalDateTime.now();
        }
    }

    public void calculateTotalDays() {
        if (startDate != null && endDate != null) {
            if (!endDate.isBefore(startDate)) {
                this.totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
            } else {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
        }
    }

    // Constructors
    public LeaveRequest() {}

    public LeaveRequest(User user, LeaveType leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        this.user = user;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        calculateTotalDays();
    }

    // Getters and setters for all fields
    public Long getId() {
        return leaveId;
    }

    public void setId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        if (endDate != null) {
            calculateTotalDays();
        }
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        if (startDate != null) {
            calculateTotalDays();
        }
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
        if (status == LeaveStatus.APPROVED && approvalDate == null) {
            approvalDate = LocalDateTime.now();
        }
    }

    public Boolean getManagerApproval() {
        return managerApproval;
    }

    public void setManagerApproval(Boolean managerApproval) {
        this.managerApproval = managerApproval;
    }

    public Boolean getHrApproval() {
        return hrApproval;
    }

    public void setHrApproval(Boolean hrApproval) {
        this.hrApproval = hrApproval;
    }

    public User getApprovedByManager() {
        return approvedByManager;
    }

    public void setApprovedByManager(User approvedByManager) {
        this.approvedByManager = approvedByManager;
    }

    public User getApprovedByHr() {
        return approvedByHr;
    }

    public void setApprovedByHr(User approvedByHr) {
        this.approvedByHr = approvedByHr;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}