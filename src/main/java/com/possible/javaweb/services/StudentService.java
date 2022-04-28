package com.possible.javaweb.services;

import com.possible.javaweb.exception.ApiRequestException;
import com.possible.javaweb.model.Student;
import com.possible.javaweb.repository.StudentRepository;
import com.possible.javaweb.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {
    private StudentRepository studentRepo;
    private EmailValidator emailValidator;

    @Autowired
    public StudentService(StudentRepository studentRepo, EmailValidator emailValidator) {
        this.studentRepo = studentRepo;
        this.emailValidator = emailValidator;
    }

    public List<Student> getAllStudents(){
        return studentRepo.selectAllStudent();
    }

    public void addNewStudent( Student student) {
        addNewStudent(null, student);
    }

    public void addNewStudent(UUID studentId, Student student) {
        UUID newStudentId = Optional.ofNullable(studentId).orElse(UUID.randomUUID());

        if (!emailValidator.test(student.getEmail())){
            throw new ApiRequestException(student.getEmail()+" is not valid");
        }

        if (studentRepo.isEmailTaken(student.getEmail())){
            throw new ApiRequestException(student.getEmail()+" is taken");
        }

        studentRepo.insertStudent(newStudentId, student);
    }
}
