package com.example.SHMS.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")

public class User {


    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "Employee_id", unique = true, nullable = false, length = 20)
    private String employeeId;

    @Column(name = "First_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "Last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false,length = 100)
    private  String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"manager", "users"})
    private Department department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    //@JsonIgnore
    private User manager;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "Phone_number", length = 10)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    // Creates: address TEXT  -- can store much larger text
    private String address;

    @Column(name = "password_change_required")
    private Boolean passwordChangeRequired = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  UserStatus status = UserStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    //PrePersist and PreUpdate methods for automatic timestamps
    @PrePersist
    protected  void onCreate(){
       createdAt = LocalDateTime.now();
       updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();  // This is what's missing!
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    //Constructor
    public User(){


    }

    public User(String lastName, UserRole role, String phoneNumber, UserStatus status, LocalDateTime updatedAt, String passwordHash, User manager, Long id, LocalDate hireDate, String firstName, String employeeId, String email, Department department, LocalDateTime createdAt, String address) {
        this.lastName = lastName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.updatedAt = updatedAt;
        this.passwordHash = passwordHash;
        this.manager = manager;
        this.id = id;
        this.hireDate = hireDate;
        this.firstName = firstName;
        this.employeeId = employeeId;
        this.email = email;
        this.department = department;
        this.createdAt = createdAt;
        this.address = address;
    }

    public Boolean getPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }
}
