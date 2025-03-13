package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();

        List<Long> studentIds = allStudentCourses.stream()
                .map(sc -> sc.getStudent().getId())
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Student> studentMap = studentRepository.findAllById(studentIds)
                .stream()
                .collect(Collectors.toMap(Student::getId, student -> student));

        for (StudentCourse sc : allStudentCourses) {
            Long studentId = sc.getStudent().getId();
            sc.setStudent(studentMap.get(studentId));
        }

        return allStudentCourses;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        return studentRepository.findAll().stream()
                .max(Comparator.comparing(Student::getGpa));
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        String result = "";
        for (Student student : students) {
            result += student.getName() + ", ";
        }
        return result.substring(0, result.length() - 2);
    }
}

