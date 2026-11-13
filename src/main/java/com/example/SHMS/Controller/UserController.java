package com.example.SHMS.Controller;

import com.example.SHMS.Model.Department;
import com.example.SHMS.Model.User;
import com.example.SHMS.Model.UserRole;
import com.example.SHMS.Model.UserStatus;
import com.example.SHMS.Service.DepartmentService;
import com.example.SHMS.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    //Get all users
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public  User getUserById(@PathVariable Long id){
        return userService.getUserById(id);

    }

    //Create new user
    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    //update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails){
        return userService.updateUser(id, userDetails);
    }

    //delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return userService.delete(id);
    }

    //Get user by email
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    //Get users by department
    @GetMapping("/department/{departmentId}")
    public List<User> getUserByDepartment(@PathVariable Long departmentId){
        return userService.getUserByDepartmentId(departmentId);
    }

    //Get user by role
    @GetMapping("/role/{role}")
    public List<User> getUserByRole(@PathVariable UserRole role){
        return userService.getUserByRole(role);
    }

    //Get user by manager
    @GetMapping("/manager/{managerId}")
    public List<User> getUsersByManager(@PathVariable Long managerId){
        //First get the manager user then find their team members
        User manager = userService.getUserById(managerId);
        return userService.getUserByManager(manager);

    }

    //Get users by status
    @GetMapping("/status/{status}")
    public List<User> getUserByStatus(@PathVariable UserStatus status){
        return userService.findByStatus(status);
    }

    //Check if email exist
    @GetMapping("/check-email/{email}")
    public boolean checkEmailExists(@PathVariable String email){
        return userService.emailExists(email);
    }

    @PostMapping("/{id}/change_password")
    public User changePassword(@PathVariable Long id, @RequestBody String newPassword){
        return userService.changePassword(id, newPassword);
    }

    @GetMapping("/test-users")
    public List<User> testUsers() {
        return userService.getAllUser();
    }

    // Add this endpoint to UserController
    // Add this endpoint to UserController
    @PutMapping("/{id}/assign-department/{departmentId}")
    public User assignDepartment(@PathVariable Long id, @PathVariable Long departmentId) {
        User user = userService.getUserById(id);
        // Remove the local declaration - use the autowired field instead
        Department department = departmentService.getDepartmentById(departmentId);

        user.setDepartment(department);
        return userService.updateUser(id, user);
    }

    @PostMapping("/debug-password")
    public Map<String, String> debugPassword(@RequestBody String password) {
        userService.debugPasswordEncoding(password);

        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("encoded", userService.getEncodedPasswordForDebug(password));
        return response;
    }

    @PostMapping("/test-login")
    public String testLogin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        User user = userService.authenticateAndGetUser(email, password);
        if (user != null) {
            return "SUCCESS - User: " + user.getEmail() + ", Role: " + user.getRole();
        } else {
            return "FAILED - Invalid credentials";
        }
    }

    @GetMapping("/test-department-assignment/{userId}")
    public ResponseEntity<?> testDepartmentAssignment(@PathVariable Long userId) {
        try {
            User user = getUserById(userId);
            Map<String, Object> response = new HashMap<>();

            response.put("user", user.getFirstName() + " " + user.getLastName());
            response.put("hasDepartment", user.getDepartment() != null);

            if (user.getDepartment() != null) {
                response.put("departmentId", user.getDepartment().getId());
                response.put("departmentName", user.getDepartment().getDepartmentName());
            } else {
                response.put("departmentId", null);
                response.put("departmentName", null);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}/password_change_required")
    public Boolean isPasswordChangeRequired(@PathVariable Long id){
        return userService.isPasswordChangeRequired(id);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            User user = userService.authenticateAndGetUser(request.getEmail(), request.getPassword());

            if (user != null){
                // Return both user data and password change requirement
                Map<String, Object> response = new HashMap<>();
                response.put("user", user);
                response.put("passwordChangeRequired", user.getPasswordChangeRequired());
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid login credentials");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Login failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Make LoginRequest a static class so Jackson can instantiate it
    public static class LoginRequest {
        private String email;
        private String password;

        // Add default no-argument constructor
        public LoginRequest() {
        }

        // Add constructor with parameters
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}