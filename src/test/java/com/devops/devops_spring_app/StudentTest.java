package com.devops.devops_spring_app.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StudentTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        Student student = new Student();
        student.setId(1);
        student.setName("John Doe");
        student.setAddress("123 Main St");

        // Act & Assert
        assertEquals(1, student.getId());
        assertEquals("John Doe", student.getName());
        assertEquals("123 Main St", student.getAddress());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Student student = new Student();
        student.setId(1);
        student.setName("Jane Doe");
        student.setAddress("456 Elm St");

        assertEquals(1, student.getId());
        assertEquals("Jane Doe", student.getName());
        assertEquals("456 Elm St", student.getAddress());
    }
    @Test
    public void testSetNullName() {
        Student student = new Student();
        student.setName(null);
        assertNull(student.getName());
    }

    @Test
    public void testSetNullAddress() {
        Student student = new Student();
        student.setAddress(null);
        assertNull(student.getAddress());
    }
}
