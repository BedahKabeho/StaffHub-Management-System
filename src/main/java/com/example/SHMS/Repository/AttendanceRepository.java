package com.example.SHMS.Repository;

import com.example.SHMS.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // CORRECT METHOD NAMES:
    Attendance findByUserIdAndAttendanceDate(Long userId, LocalDate date);

    boolean existsByUserIdAndAttendanceDate(Long userId, LocalDate date);

    List<Attendance> findByUserIdAndAttendanceDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByAttendanceDate(LocalDate date);

    List<Attendance> findByUserDepartmentIdAndAttendanceDateBetween(Long departmentId, LocalDate startDate, LocalDate endDate);
}