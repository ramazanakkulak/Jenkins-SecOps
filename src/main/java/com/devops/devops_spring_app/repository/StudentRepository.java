package com.devops.devops_spring_app.repository;

import com.devops.devops_spring_app.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository is an interface that provides access to data in a database
 */
public interface StudentRepository extends JpaRepository<Student,Integer> {


}
