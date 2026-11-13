package com.example.SHMS.Service;

import com.example.SHMS.Model.Announcements;
import com.example.SHMS.Model.Department;
import com.example.SHMS.Repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcements> getAllAnnouncement(){
        List<Announcements> allAnnouncements = announcementRepository.findAll();
        return allAnnouncements.stream()
                .filter(announcement -> announcement.getPostedBy() == null ||
                        !Boolean.TRUE.equals(announcement.getPostedBy().getIsDeleted())) // CHANGED: Show only ACTIVE users
                .collect(Collectors.toList());
    }

    public Announcements getAnnouncementById(Long id){
        return announcementRepository.findById(id).orElseThrow(()-> new RuntimeException("Announcement not found"));
    }

    public Announcements createAnnouncements(Announcements announcements){
        return announcementRepository.save(announcements);
    }

    public Announcements updateAnnouncement(Long id, Announcements announcementsDetails){
        Announcements announcements = getAnnouncementById(id);

        if (announcementsDetails.getTitle() != null) {
            announcements.setTitle(announcementsDetails.getTitle());
        }
        if (announcementsDetails.getContent() != null) {
            announcements.setContent(announcementsDetails.getContent());
        }
        if (announcementsDetails.getPostedBy() != null) {
            announcements.setPostedBy(announcementsDetails.getPostedBy());
        }
        if (announcementsDetails.getTargetAudience() != null) {
            announcements.setTargetAudience(announcementsDetails.getTargetAudience());
        }
        if (announcementsDetails.getIsActive() != null) {
            announcements.setIsActive(announcementsDetails.getIsActive());
        }
        if (announcementsDetails.getDepartment() != null) {
            announcements.setDepartment(announcementsDetails.getDepartment());
        }

        return announcementRepository.save(announcements);
    }

    // FIXED: Allow HR to delete any announcement
    public String deleteAnnouncements(Long id){
        Announcements announcements = getAnnouncementById(id);
        announcementRepository.delete(announcements);
        return "Announcement deleted successfully";
    }

    public List<Announcements> getAnnouncementByDepartment(Department departmentName){
        return announcementRepository.findByDepartment(departmentName);
    }
}