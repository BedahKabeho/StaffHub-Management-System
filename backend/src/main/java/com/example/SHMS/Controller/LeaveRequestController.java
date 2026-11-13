package com.example.SHMS.Controller;

import com.example.SHMS.Model.LeaveRequest;
import com.example.SHMS.Model.LeaveStatus;
import com.example.SHMS.Model.LeaveType;
import com.example.SHMS.Model.User;
import com.example.SHMS.Service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaveRequest")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping
    public List<LeaveRequest> getAllLeaveRequest(){
        return leaveRequestService.findAllLeaveRequest();
    }

    @GetMapping("/{id}")
    public LeaveRequest getRequestById(@PathVariable Long id){
        return leaveRequestService.findLeaveRequestById(id);
    }

    @PostMapping
    public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest leaveRequest){
        return leaveRequestService.createLeaveRequest(leaveRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteLeaveRequest(@PathVariable Long id){
        return leaveRequestService.delete(id);
    }

    @GetMapping("/leaveType/{type}")
    public List<LeaveRequest> getLeaveRequestByType(@PathVariable LeaveType type){
        return leaveRequestService.getRequestByType(type);
    }


    @GetMapping("/status/{status}")
    public List<LeaveRequest> getRequestByStatus(@PathVariable LeaveStatus status){
        return  leaveRequestService.getRequestByStatus(status);
    }
    @PutMapping("/{id}")
    public LeaveRequest updateLeaveRequest(@PathVariable Long id, @RequestBody LeaveRequest leaveRequestDetails){
        return leaveRequestService.updateLeaveRequest(id, leaveRequestDetails);
    }

    @PutMapping("/{id}/approve/manager")
    public LeaveRequest approveByManager(@PathVariable Long id, @RequestBody User manager){
        return leaveRequestService.approveByManager(id, manager);
    }

    @PutMapping("/{id}/approve/hr")
    public ResponseEntity<?> approveByHr(@PathVariable Long id, @RequestBody User hrManager){
        try {
            LeaveRequest result = leaveRequestService.approvedByHr(id, hrManager);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLeave(@PathVariable Long id, @RequestBody User rejectingUser){
        try {
            LeaveRequest result = leaveRequestService.rejectLeave(id, rejectingUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/department/{departmentId}")
    public List<LeaveRequest> getLeaveRequestsByDepartment(@PathVariable Long departmentId) {
        System.out.println("=== CONTROLLER: Getting leave requests by department ===");
        System.out.println("Department ID: " + departmentId);

        List<LeaveRequest> requests = leaveRequestService.findLeaveRequestsByDepartment(departmentId);
        System.out.println("Returning " + requests.size() + " leave requests");

        return requests;
    }


}
