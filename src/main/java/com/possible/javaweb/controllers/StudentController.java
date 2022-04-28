package com.possible.javaweb.controllers;

import com.possible.javaweb.exception.ApiRequestException;
import com.possible.javaweb.model.Gender;
import com.possible.javaweb.model.Student;
import com.possible.javaweb.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public List<Student> allStudents(){
//        throw new ApiRequestException("Oops cannot get all student at the moment");
        return studentService.getAllStudents();
    }

    @PostMapping
    public void addNewStudent(@RequestBody @Valid Student student){
        studentService.addNewStudent(student);
    }
}
