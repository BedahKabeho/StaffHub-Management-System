package com.example.SHMS.Controller;


import com.example.SHMS.Model.Department;
import com.example.SHMS.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //Get all user
    @GetMapping
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment();
    }

    //Get department By id
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id){
        return departmentService.getDepartmentById(id);
    }

    //Create new Department
    @PostMapping
    public Department createDepartment(@RequestBody Department department){
        return departmentService.createDepartment(department);
    }

    //Update department
    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails ){
        return departmentService.updateDepartment(id, departmentDetails);
    }

    //Delete department
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return departmentService.delete(id);
    }

    //Get department By name
    @GetMapping("/name/{departmentName}")
    public Department getByDepartmentName(@PathVariable String departmentName){
        return departmentService.getDepartmentByName(departmentName);
    }
}
