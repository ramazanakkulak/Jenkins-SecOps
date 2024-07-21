package com.devops.devops_spring_app.service;

import com.devops.devops_spring_app.model.Student;
import com.devops.devops_spring_app.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() {
        // Arrange
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("John Doe");
        student1.setAddress("123 Main St");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Jane Doe");
        student2.setAddress("456 Elm St");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        // Act
        List<Student> students = studentService.listAll();

        // Assert
        assertEquals(2, students.size());
        assertEquals("John Doe", students.get(0).getName());
        assertEquals("Jane Doe", students.get(1).getName());
    }

    @Test
    public void testSave() {
        // Arrange
        Student student = new Student();
        student.setId(1);
        student.setName("John Doe");
        student.setAddress("123 Main St");

        // Act
        studentService.save(student);

        // Assert
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testGet() {
        // Arrange
        Student student = new Student();
        student.setId(1);
        student.setName("John Doe");
        student.setAddress("123 Main St");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        // Act
        Student foundStudent = studentService.get(1);

        // Assert
        assertNotNull(foundStudent);
        assertEquals("John Doe", foundStudent.getName());
    }

    @Test
    public void testDelete() {
        // Act
        studentService.delete(1);

        // Assert
        verify(studentRepository, times(1)).deleteById(1);
    }
}
