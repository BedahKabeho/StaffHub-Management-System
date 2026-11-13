package com.example.SHMS.Repository;

import com.example.SHMS.Model.Announcements;
import com.example.SHMS.Model.Department;
import com.example.SHMS.Model.TargetAudience;
import com.example.SHMS.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcements, Long> {

    List<Announcements> findByTargetAudience(TargetAudience targetAudience);
   // List<Announcements> findByHowActive(Announcements active);
    List<Announcements> findByDepartment(Department departmentName);
    List<Announcements> findByIsActive(Boolean isActive);
    List<Announcements> findByPostedBy( User user);
}
