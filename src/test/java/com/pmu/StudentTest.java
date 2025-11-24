package com.pmu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Student
 * Tests all methods: constructor, getId, getName, enrollCourse, getCourses, calculateGPA
 */
@DisplayName("Student Tests")
class StudentTest {

    private Student student;
    private Course courseA;
    private Course courseB;
    private Course courseF;

    @BeforeEach
    void setUp() {
        // Create a valid student
        student = new Student("S001", "mohammed");

        // Create test courses with different grades
        courseA = new Course("Math", 3, "A");      // Grade point: 4.0
        courseB = new Course("English", 4, "B");   // Grade point: 3.0
        courseF = new Course("Science", 2, "F");   // Grade point: 0.0
    }

    // ============ Constructor Tests ============

    @Test
    @DisplayName("Constructor: Valid input should create student with correct values")
    void testConstructorWithValidInput() {
        Student testStudent = new Student("S002", "ahmad");
        assertEquals("S002", testStudent.getId());
        assertEquals("ahmad", testStudent.getName());
    }

    @Test
    @DisplayName("Constructor: Null student ID should default to '0000'")
    void testConstructorNullId() {
        Student testStudent = new Student(null, "ahmad");
        assertEquals("0000", testStudent.getId());
    }

    @Test
    @DisplayName("Constructor: Empty student ID should default to '0000'")
    void testConstructorEmptyId() {
        Student testStudent = new Student("", "ahmad");
        assertEquals("0000", testStudent.getId());
    }

    @Test
    @DisplayName("Constructor: Null student name should default to 'Unknown'")
    void testConstructorNullName() {
        Student testStudent = new Student("S001", null);
        assertEquals("Unknown", testStudent.getName());
    }

    @Test
    @DisplayName("Constructor: Empty student name should default to 'Unknown'")
    void testConstructorEmptyName() {
        Student testStudent = new Student("S001", "");
        assertEquals("Unknown", testStudent.getName());
    }

    @Test
    @DisplayName("Constructor: Courses list should be initialized empty")
    void testConstructorCoursesEmpty() {
        assertTrue(student.getCourses().isEmpty());
        assertEquals(0, student.getCourses().size());
    }

    // ============ getId() Tests ============

    @Test
    @DisplayName("getId: Should return the student ID correctly")
    void testGetId() {
        assertEquals("S001", student.getId());
    }

    @Test
    @DisplayName("getId: Should return default '0000' for null input")
    void testGetIdDefault() {
        Student testStudent = new Student(null, "hadi");
        assertEquals("0000", testStudent.getId());
    }

    // ============ getName() Tests ============

    @Test
    @DisplayName("getName: Should return the student name correctly")
    void testGetName() {
        assertEquals("mohammed", student.getName());
    }

    @Test
    @DisplayName("getName: Should return 'Unknown' for null input")
    void testGetNameDefault() {
        Student testStudent = new Student("S001", null);
        assertEquals("Unknown", testStudent.getName());
    }

    // ============ enrollCourse() Tests ============

    @Test
    @DisplayName("enrollCourse: Should add a valid course to the list")
    void testEnrollCourseValid() {
        student.enrollCourse(courseA);
        assertEquals(1, student.getCourses().size());
        assertEquals(courseA, student.getCourses().get(0));
    }

    @Test
    @DisplayName("enrollCourse: Should add multiple courses to the list")
    void testEnrollMultipleCourses() {
        student.enrollCourse(courseA);
        student.enrollCourse(courseB);
        student.enrollCourse(courseF);
        assertEquals(3, student.getCourses().size());
    }

    @Test
    @DisplayName("enrollCourse: Should not add null course")
    void testEnrollCourseNull() {
        student.enrollCourse(null);
        assertEquals(0, student.getCourses().size());
    }

    @Test
    @DisplayName("enrollCourse: Should add duplicate courses (same course multiple times)")
    void testEnrollDuplicateCourse() {
        student.enrollCourse(courseA);
        student.enrollCourse(courseA);
        assertEquals(2, student.getCourses().size());
    }

    // ============ getCourses() Tests ============

    @Test
    @DisplayName("getCourses: Should return empty list when no courses enrolled")
    void testGetCoursesEmpty() {
        assertTrue(student.getCourses().isEmpty());
    }

    @Test
    @DisplayName("getCourses: Should return all enrolled courses")
    void testGetCoursesMultiple() {
        student.enrollCourse(courseA);
        student.enrollCourse(courseB);

        List<Course> courses = student.getCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains(courseA));
        assertTrue(courses.contains(courseB));
    }

    @Test
    @DisplayName("getCourses: Should return courses in enrollment order")
    void testGetCoursesOrder() {
        student.enrollCourse(courseA);
        student.enrollCourse(courseB);

        List<Course> courses = student.getCourses();
        assertEquals(courseA, courses.get(0));
        assertEquals(courseB, courses.get(1));
    }

    // ============ calculateGPA() Tests ============

    @Test
    @DisplayName("calculateGPA: Should return 0.0 when no courses enrolled")
    void testCalculateGPANoCourses() {
        assertEquals(0.0, student.calculateGPA());
    }

    @Test
    @DisplayName("calculateGPA: Should calculate GPA correctly for single course with grade A")
    void testCalculateGPASingleCourseA() {
        student.enrollCourse(courseA);
        // GPA = (4.0 * 3) / 3 = 4.0
        assertEquals(4.0, student.calculateGPA());
    }

    @Test
    @DisplayName("calculateGPA: Should calculate GPA correctly for single course with grade B")
    void testCalculateGPASingleCourseB() {
        student.enrollCourse(courseB);
        // GPA = (3.0 * 4) / 4 = 3.0
        assertEquals(3.0, student.calculateGPA());
    }

    @Test
    @DisplayName("calculateGPA: Should calculate GPA correctly for multiple courses with different grades")
    void testCalculateGPAMultipleCourses() {
        // courseA: 4.0 * 3 credits = 12.0
        // courseB: 3.0 * 4 credits = 12.0
        // Total: 24.0 / 7 credits = 3.43 (approx)
        student.enrollCourse(courseA);
        student.enrollCourse(courseB);

        double expectedGPA = (4.0 * 3 + 3.0 * 4) / 7.0;
        assertEquals(expectedGPA, student.calculateGPA(), 0.0001);
    }

    @Test
    @DisplayName("calculateGPA: Should calculate GPA with F grade correctly")
    void testCalculateGPAWithFGrade() {
        // courseF: 0.0 * 2 credits = 0.0
        // courseA: 4.0 * 3 credits = 12.0
        // Total: 12.0 / 5 credits = 2.4
        student.enrollCourse(courseF);
        student.enrollCourse(courseA);

        double expectedGPA = (0.0 * 2 + 4.0 * 3) / 5.0;
        assertEquals(expectedGPA, student.calculateGPA(), 0.0001);
    }

    @Test
    @DisplayName("calculateGPA: Should cap GPA at 4.0 maximum")
    void testCalculateGPACapped() {
        // Even with all A grades, GPA should not exceed 4.0
        Course courseA2 = new Course("Physics", 3, "A");
        student.enrollCourse(courseA);
        student.enrollCourse(courseA2);

        double gpa = student.calculateGPA();
        assertTrue(gpa <= 4.0);
        assertEquals(4.0, gpa);
    }

    @Test
    @DisplayName("calculateGPA: Should handle course with invalid grade (defaults to F)")
    void testCalculateGPAInvalidGrade() {
        Course invalidCourse = new Course("History", 3, "InvalidGrade");
        student.enrollCourse(invalidCourse);

        // Invalid grade defaults to F (0.0 points)
        assertEquals(0.0, student.calculateGPA());
    }

    @Test
    @DisplayName("calculateGPA: Should calculate correctly with varying credit hours")
    void testCalculateGPAVaryingCredits() {
        Course course1 = new Course("Course1", 1, "A");  // 4.0 * 1 = 4.0
        Course course2 = new Course("Course2", 5, "F");  // 0.0 * 5 = 0.0

        student.enrollCourse(course1);
        student.enrollCourse(course2);

        // GPA = 4.0 / 6 = 0.667
        double expectedGPA = 4.0 / 6.0;
        assertEquals(expectedGPA, student.calculateGPA(), 0.0001);
    }

    @Test
    @DisplayName("calculateGPA: Should handle all F grades")
    void testCalculateGPAAllF() {
        Course f1 = new Course("Course1", 3, "F");
        Course f2 = new Course("Course2", 4, "F");

        student.enrollCourse(f1);
        student.enrollCourse(f2);

        assertEquals(0.0, student.calculateGPA());
    }
}