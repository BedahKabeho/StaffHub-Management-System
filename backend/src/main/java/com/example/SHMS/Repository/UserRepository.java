package com.example.SHMS.Repository;

import com.example.SHMS.Model.Department;
import com.example.SHMS.Model.User;
import com.example.SHMS.Model.UserRole;
import com.example.SHMS.Model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByDepartmentId(Long id);
    List<User> findByManager(User manager);
    List<User> findByStatus(UserStatus status);
    List<User> findByIsDeletedFalse();

    boolean existsByEmail(String email);
    User findByEmailAndIsDeletedFalse(String email);


}
