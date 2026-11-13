package com.example.SHMS.Controller;

import com.example.SHMS.Model.Attendance;
import com.example.SHMS.Model.User;
import com.example.SHMS.Service.AttendanceService;
import com.example.SHMS.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    // Improved network check that allows localhost for testing
    private boolean isOnCompanyNetwork(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();

        // Allow company network AND localhost (for development)
        boolean isAllowed = clientIp.startsWith("192.168.100.") ||
                clientIp.startsWith("127.0.0.1") ||
                clientIp.equals("0:0:0:0:0:0:0:1");

        System.out.println("Network Check - IP: " + clientIp + ", Allowed: " + isAllowed);
        return isAllowed;
    }

    @PostMapping("/clock-in")
    public String clockIn(@RequestParam Long userId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return "Access denied: You must be connected to the company network to clock in";
        }

        String ipAddress = request.getRemoteAddr();
        return attendanceService.clockIn(userId, ipAddress);
    }

    @PostMapping("/clock-out")
    public String clockOut(@RequestParam Long userId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return "Access denied: You must be connected to the company network to clock out";
        }

        String ipAddress = request.getRemoteAddr();
        return attendanceService.clockOut(userId, ipAddress);
    }

    @GetMapping("/today")
    public Object getTodayAttendance(@RequestParam Long userId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return "Access denied: Network restriction";
        }
        return attendanceService.getTodayAttendance(userId);
    }

    @GetMapping("/can-clock-in")
    public boolean canClockIn(@RequestParam Long userId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return false;
        }
        return attendanceService.canClockIn(userId);
    }

    @GetMapping("/can-clock-out")
    public boolean canClockOut(@RequestParam Long userId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return false;
        }
        return attendanceService.canClockOut(userId);
    }

    // These methods don't need network check (viewing history is allowed anywhere)
    @GetMapping("/history")
    public List<Attendance> getAttendanceHistory(@RequestParam Long userId,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate) {
        return attendanceService.getUserAttendanceHistory(userId, startDate, endDate);
    }

    @GetMapping("/all")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllUserAttendance();
    }

    @GetMapping("/department/today")
    public Object getTodayDepartmentAttendance(@RequestParam Long managerId, HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return "Access denied: Network restriction";
        }

        try {
            User manager = userService.getUserById(managerId);
            if (manager.getDepartment() == null) {
                return "Manager is not assigned to any department";
            }
            Long departmentId = manager.getDepartment().getId();
            return attendanceService.getTodayDepartmentAttendance(departmentId);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/department/history")
    public Object getDepartmentAttendanceHistory(@RequestParam Long managerId,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate,
                                                 HttpServletRequest request) {
        if (!isOnCompanyNetwork(request)) {
            return "Access denied: Network restriction";
        }

        try {
            User manager = userService.getUserById(managerId);
            if (manager.getDepartment() == null) {
                return "Manager is not assigned to any department";
            }
            Long departmentId = manager.getDepartment().getId();
            return attendanceService.getDepartmentAttendance(departmentId, startDate, endDate);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}