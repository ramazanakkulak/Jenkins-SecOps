package com.devops.devops_spring_app.service;
import org.springframework.stereotype.Service;
import com.devops.devops_spring_app.model.Student;
import com.devops.devops_spring_app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> listAll(){
        return studentRepository.findAll();
    }

    public void save(Student student){
        studentRepository.save(student);
    }

    public Student get(Integer id){
        return studentRepository.findById(id).get();
    }

    public void delete(Integer id){
        studentRepository.deleteById(id);
    }
}
