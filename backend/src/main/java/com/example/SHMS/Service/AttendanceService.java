package com.example.SHMS.Service;

import com.example.SHMS.Model.Attendance;
import com.example.SHMS.Model.User;
import com.example.SHMS.Repository.AttendanceRepository;
import com.example.SHMS.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Attendance> getAllUserAttendance() {
        return attendanceRepository.findAll();
    }

    // Clock in method
    public String clockIn(Long userId, String ipAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return "User not found";
        }

        User user = userOptional.get();

        boolean alreadyClockedIn = attendanceRepository.existsByUserIdAndAttendanceDate(userId, LocalDate.now());
        if (alreadyClockedIn) {
            return "You have already clocked in today";
        }

        Attendance attendance = new Attendance(user, LocalDateTime.now(), ipAddress);
        attendanceRepository.save(attendance);
        return "Clocked in successfully at " + LocalDateTime.now().toLocalTime();
    }

    // Clock Out method
    public String clockOut(Long userId, String ipAddress) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return "User not found";
        }


        Attendance todayRecord = attendanceRepository.findByUserIdAndAttendanceDate(userId, LocalDate.now());
        if(todayRecord == null){
            return "Please clock in first";
        }

        if (todayRecord.getClockOutTIme() != null){
            return "You have already clocked out today";
        }

        todayRecord.setClockOutTIme(LocalDateTime.now());
        attendanceRepository.save(todayRecord);
        return "Clocked out successfully at " + LocalDateTime.now().toLocalTime();
    }

    // Get attendance history of a user
    public List<Attendance> getUserAttendanceHistory(Long userId, LocalDate startDate, LocalDate endDate){

        return attendanceRepository.findByUserIdAndAttendanceDateBetween(userId, startDate, endDate);
    }

    // Get today's attendance
    public Attendance getTodayAttendance(Long userId){

        return attendanceRepository.findByUserIdAndAttendanceDate(userId, LocalDate.now());
    }

    // Check if user can clock out
    public boolean canClockOut(Long userId){

        Attendance todayRecord = attendanceRepository.findByUserIdAndAttendanceDate(userId, LocalDate.now());
        return todayRecord != null && todayRecord.getClockOutTIme() == null;
    }

    // Check if user can clock in
    public Boolean canClockIn(Long userId){
        return !attendanceRepository.existsByUserIdAndAttendanceDate(userId, LocalDate.now());
    }

    public List<Attendance> getAttendanceByDate(LocalDate date){
        return attendanceRepository.findByAttendanceDate(date);
    }

    public List<Attendance> getTodayDepartmentAttendance(Long departmentId){
        return attendanceRepository.findByUserDepartmentIdAndAttendanceDateBetween(departmentId, LocalDate.now(), LocalDate.now());
    }

    public List<Attendance> getDepartmentAttendance(Long departmentId, LocalDate startDate, LocalDate endDate){
        return attendanceRepository.findByUserDepartmentIdAndAttendanceDateBetween(departmentId, startDate, endDate);
    }
}