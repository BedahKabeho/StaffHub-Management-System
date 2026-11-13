package com.example.SHMS.Service;

import com.example.SHMS.Model.Department;
import com.example.SHMS.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id){
        return departmentRepository.findById(id).orElseThrow(()-> new RuntimeException("department  not found"));
    }

    public Department createDepartment(Department department){
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Long id, Department departmentDetails){
        Department department = getDepartmentById(id);

        // Only update if provided (not null)
        if (departmentDetails.getDepartmentName() != null) {
            department.setDepartmentName(departmentDetails.getDepartmentName());
        }

        if (departmentDetails.getDepartmentCode() != null) {
            department.setDepartmentCode(departmentDetails.getDepartmentCode());
        }

        if (departmentDetails.getManager() != null) {
            department.setManager(departmentDetails.getManager());
        }

        return departmentRepository.save(department);
    }

    public String delete(Long id){
        Department department = getDepartmentById(id);
        departmentRepository.delete(department);
        return "department with id " + id + " deleted successfully";
    }

    public Department getDepartmentByName(String departmentName){
        return departmentRepository.findByDepartmentName(departmentName).orElseThrow(()-> new RuntimeException("department not found with name " + departmentName));
    }
}
