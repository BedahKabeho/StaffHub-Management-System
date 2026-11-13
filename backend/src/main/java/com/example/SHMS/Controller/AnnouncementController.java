package com.example.SHMS.Controller;

import com.example.SHMS.Model.Announcements;
import com.example.SHMS.Model.Department;
import com.example.SHMS.Repository.AnnouncementRepository;
import com.example.SHMS.Service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
@CrossOrigin(origins = "http://127.0.0.1:5500")

public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public List<Announcements> getAllAnnouncement(){
        return announcementService.getAllAnnouncement();
    }

    @GetMapping("/{id}")
    public Announcements getAnnouncementById(@PathVariable Long id){
        return announcementService.getAnnouncementById(id);

    }

    @PostMapping
    public Announcements createAnnouncement(@RequestBody Announcements announcements){
        return announcementService.createAnnouncements(announcements);

    }

    @PutMapping("/{id}")
    public Announcements updateAnnouncement(@PathVariable Long id, @RequestBody Announcements announcementsDetails){
        return announcementService.updateAnnouncement(id, announcementsDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteAnnouncement(@PathVariable Long id){
        return announcementService.deleteAnnouncements(id);
    }

    @GetMapping("/department/{department}")
    public List<Announcements> getAnnouncementByDepartment(@PathVariable Department departmentName){
        return announcementService.getAnnouncementByDepartment(departmentName);
    }
}
