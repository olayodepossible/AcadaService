package com.possible.javaweb.repository;

import com.possible.javaweb.model.Gender;
import com.possible.javaweb.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class StudentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   public List<Student> selectAllStudent(){
        String sql = ""+
                "SELECT "+
                "student_id, "+
                "first_name, "+
                "last_name, "+
                "email, "+
                "gender "+
                "FROM student";
       return jdbcTemplate.query(sql, mapStudentFromDb());
    }

    public int insertStudent(UUID studentId, Student student) {
        String sql = ""+
                "INSERT INTO student (student_id, first_name, last_name, email, gender)  "+
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                studentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().toString().toUpperCase()
        );
    }

    private RowMapper<Student> mapStudentFromDb() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id"); // becos we save id as UUID in Db
            UUID studentId = UUID.fromString(studentIdStr);

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String genderStr = resultSet.getString("gender").toUpperCase();
            return new Student(studentId, firstName, lastName, email, Gender.valueOf(genderStr));
        };
    }


    public boolean isEmailTaken(String email) {

        String sql = "" +
                "SELECT EXISTS( SELECT 1 FROM student WHERE email = ? )";

        return  jdbcTemplate.queryForObject(
                sql,
                new Object [] {email},
                (resultSet, i) -> resultSet.getBoolean(1)
        );

    }
}
