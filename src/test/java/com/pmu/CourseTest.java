package com.pmu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Course
 * Tests all methods: constructor, getCourseName, getCreditHours, getLetterGrade, getGradePoint
 */
@DisplayName("Course Tests")
class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        // Initialize a valid course for each test
        course = new Course("Java Programming", 3, "A");
    }

    // ============ Constructor Tests ============

    @Test
    @DisplayName("Constructor: Valid input should create course with correct values")
    void testConstructorWithValidInput() {
        Course testCourse = new Course("Data Structures", 4, "B");
        assertEquals("Data Structures", testCourse.getCourseName());
        assertEquals(4, testCourse.getCreditHours());
        assertEquals("B", testCourse.getLetterGrade());
    }

    @Test
    @DisplayName("Constructor: Null course name should default to 'Unknown'")
    void testConstructorNullCourseName() {
        Course testCourse = new Course(null, 3, "A");
        assertEquals("Unknown", testCourse.getCourseName());
    }

    @Test
    @DisplayName("Constructor: Empty course name should default to 'Unknown'")
    void testConstructorEmptyCourseName() {
        Course testCourse = new Course("", 3, "A");
        assertEquals("Unknown", testCourse.getCourseName());
    }

    @Test
    @DisplayName("Constructor: Credit hours less than 1 should default to 3")
    void testConstructorCreditHoursTooLow() {
        Course testCourse = new Course("Java", 0, "A");
        assertEquals(3, testCourse.getCreditHours());
    }

    @Test
    @DisplayName("Constructor: Credit hours greater than 6 should default to 3")
    void testConstructorCreditHoursTooHigh() {
        Course testCourse = new Course("Java", 7, "A");
        assertEquals(3, testCourse.getCreditHours());
    }

    @Test
    @DisplayName("Constructor: Valid credit hours (1-6) should be set correctly")
    void testConstructorValidCreditHours() {
        for (int hours = 1; hours <= 6; hours++) {
            Course testCourse = new Course("Java", hours, "A");
            assertEquals(hours, testCourse.getCreditHours());
        }
    }

    @Test
    @DisplayName("Constructor: Invalid letter grade should default to 'F'")
    void testConstructorInvalidLetterGrade() {
        Course testCourse = new Course("Java", 3, "Z");
        assertEquals("F", testCourse.getLetterGrade());
    }

    @Test
    @DisplayName("Constructor: Null letter grade should default to 'F'")
    void testConstructorNullLetterGrade() {
        Course testCourse = new Course("Java", 3, null);
        assertEquals("F", testCourse.getLetterGrade());
    }

    @Test
    @DisplayName("Constructor: All valid grades (A, B, C, D, F) should be set correctly")
    void testConstructorAllValidGrades() {
        String[] validGrades = {"A", "B", "C", "D", "F"};
        for (String grade : validGrades) {
            Course testCourse = new Course("Java", 3, grade);
            assertEquals(grade, testCourse.getLetterGrade());
        }
    }

    // ============ getCourseName() Tests ============

    @Test
    @DisplayName("getCourseName: Should return the course name correctly")
    void testGetCourseName() {
        assertEquals("Java Programming", course.getCourseName());
    }

    @Test
    @DisplayName("getCourseName: Should return 'Unknown' for null input")
    void testGetCourseNameUnknown() {
        Course testCourse = new Course(null, 3, "A");
        assertEquals("Unknown", testCourse.getCourseName());
    }

    // ============ getCreditHours() Tests ============

    @Test
    @DisplayName("getCreditHours: Should return the credit hours correctly")
    void testGetCreditHours() {
        assertEquals(3, course.getCreditHours());
    }

    @Test
    @DisplayName("getCreditHours: Should return default 3 for invalid input")
    void testGetCreditHoursDefault() {
        Course testCourse = new Course("Java", -1, "A");
        assertEquals(3, testCourse.getCreditHours());
    }

    // ============ getLetterGrade() Tests ============

    @Test
    @DisplayName("getLetterGrade: Should return the letter grade correctly")
    void testGetLetterGrade() {
        assertEquals("A", course.getLetterGrade());
    }

    @Test
    @DisplayName("getLetterGrade: Should return 'F' for invalid input")
    void testGetLetterGradeInvalid() {
        Course testCourse = new Course("Java", 3, "InvalidGrade");
        assertEquals("F", testCourse.getLetterGrade());
    }

    // ============ getGradePoint() Tests ============

    @Test
    @DisplayName("getGradePoint: Grade A should return 4.0")
    void testGetGradePointA() {
        Course testCourse = new Course("Java", 3, "A");
        assertEquals(4.0, testCourse.getGradePoint());
    }

    @Test
    @DisplayName("getGradePoint: Grade B should return 3.0")
    void testGetGradePointB() {
        Course testCourse = new Course("Java", 3, "B");
        assertEquals(3.0, testCourse.getGradePoint());
    }

    @Test
    @DisplayName("getGradePoint: Grade C should return 2.0")
    void testGetGradePointC() {
        Course testCourse = new Course("Java", 3, "C");
        assertEquals(2.0, testCourse.getGradePoint());
    }

    @Test
    @DisplayName("getGradePoint: Grade D should return 1.0")
    void testGetGradePointD() {
        Course testCourse = new Course("Java", 3, "D");
        assertEquals(1.0, testCourse.getGradePoint());
    }

    @Test
    @DisplayName("getGradePoint: Grade F should return 0.0")
    void testGetGradePointF() {
        Course testCourse = new Course("Java", 3, "F");
        assertEquals(0.0, testCourse.getGradePoint());
    }

    @Test
    @DisplayName("getGradePoint: Invalid grade should return 0.0")
    void testGetGradePointInvalid() {
        Course testCourse = new Course("Java", 3, "Invalid");
        assertEquals(0.0, testCourse.getGradePoint());
    }
}