package com.example.SHMS.Service;

import com.example.SHMS.Model.Department;
import com.example.SHMS.Model.User;
import com.example.SHMS.Model.UserRole;
import com.example.SHMS.Model.UserStatus;
import com.example.SHMS.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private String encodePassword(String password){
        return String.valueOf(password.hashCode());
    }

    public List<User> getAllUser() {
        return userRepository.findAll(); // Show all users including deleted ones
    }

    public User getUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is deleted - This is CORRECT
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("User has been deleted");
        }

        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User createUser(User user) {
        if(emailExists(user.getEmail())){
            throw new RuntimeException("Email already registered:" + user.getEmail());
        }

        if(user.getPasswordHash() != null){
            user.setPasswordHash(encodePassword(user.getPasswordHash()));
        }

        if (user.getRole() != UserRole.ADMIN) {
            user.setPasswordChangeRequired(true);
        } else {
            user.setPasswordChangeRequired(false);
        }

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        if (userDetails.getFirstName() != null) {
            user.setFirstName(userDetails.getFirstName());
        }
        if (userDetails.getLastName() != null) {
            user.setLastName(userDetails.getLastName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }
        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }
        if (userDetails.getDepartment() != null) {
            user.setDepartment(userDetails.getDepartment());
        }
        if (userDetails.getManager() != null) {
            user.setManager(userDetails.getManager());
        }
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }
        if (userDetails.getStatus() != null) {
            user.setStatus(userDetails.getStatus());
        }

        return userRepository.save(user);
    }

    // FIXED: Allow HR to delete any user
    public String delete(Long id) {
        User user = getUserById(id);
        user.setIsDeleted(true);  // Soft delete - mark as deleted
        user.setStatus(UserStatus.INACTIVE);  // Also set as inactive
        userRepository.save(user);  // Update instead of delete
        return "User deactivated successfully";
    }

    public List<User> getUserByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUserByDepartmentId(Long id) {
        List<User> users = userRepository.findByDepartmentId(id);
        // Filter out deleted users
        return users.stream()
                .filter(user -> user.getIsDeleted() == null || !user.getIsDeleted())
                .collect(Collectors.toList());
    }

    public List<User> getUserByManager(User manager){
        return userRepository.findByManager(manager);
    }

    public List<User> findByStatus(UserStatus status){
        return userRepository.findByStatus(status);
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public User changePassword(Long userId, String newPassword) {
        User user = getUserById(userId);

        if(newPassword == null || newPassword.length() < 6){
            throw new RuntimeException("Password must be at least 6 characters");
        }

        String encodedPassword = encodePassword(newPassword);
        user.setPasswordHash(encodedPassword);

        if (user.getRole() != UserRole.ADMIN) {
            user.setPasswordChangeRequired(false);
        }

        return userRepository.save(user);
    }

    public Boolean isPasswordChangeRequired(Long userId){
        User user = getUserById(userId);
        return user.getPasswordChangeRequired();
    }

    public User authenticateAndGetUser(String email, String password) {
        try {
            User foundUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String encodedPassword = encodePassword(password);
            boolean passwordMatches = foundUser.getPasswordHash().equals(encodedPassword);

            if (passwordMatches) {
                return foundUser;
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    // Add these methods to your UserService class

    public void debugPasswordEncoding(String password) {
        String encoded1 = encodePassword(password);
        String encoded2 = encodePassword(password);
        System.out.println("=== PASSWORD ENCODING DEBUG ===");
        System.out.println("Original: " + password);
        System.out.println("Encoded 1: " + encoded1);
        System.out.println("Encoded 2: " + encoded2);
        System.out.println("Match: " + encoded1.equals(encoded2));
        System.out.println("=== END DEBUG ===");
    }

    public String getEncodedPasswordForDebug(String password) {
        return encodePassword(password);
    }
}