package com.devops.devops_spring_app.controller;

import com.devops.devops_spring_app.model.Student;
import com.devops.devops_spring_app.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testListAll() throws Exception {
        // Arrange
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("John Doe");
        student1.setAddress("123 Main St");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("Jane Doe");
        student2.setAddress("456 Elm St");

        when(studentService.listAll()).thenReturn(Arrays.asList(student1, student2));

        // Act & Assert
        mockMvc.perform(get("/student/getAll"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("John Doe"))
               .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    public void testAdd() throws Exception {
        // Arrange
        Student student = new Student();
        student.setName("John Doe");
        student.setAddress("123 Main St");

        // Act & Assert
        mockMvc.perform(post("/student/add")
                        .contentType("application/json")
                        .content("{\"name\":\"John Doe\", \"address\":\"123 Main St\"}"))
               .andExpect(status().isOk())
               .andExpect(content().string("New Student Added"));

        verify(studentService, times(1)).save(any(Student.class));
    }

    @Test
    public void testGet() throws Exception {
        // Arrange
        Student student = new Student();
        student.setId(1);
        student.setName("John Doe");
        student.setAddress("123 Main St");

        when(studentService.get(1)).thenReturn(student);

        // Act & Assert
        mockMvc.perform(get("/student/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.address").value("123 Main St"));
    }
}
