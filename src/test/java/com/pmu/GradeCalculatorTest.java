package com.pmu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GradeCalculator
 *
 * Testing Strategy: Equivalence Partitioning + Boundary Value Analysis (Black-box testing)
 *
 * Equivalence Classes:
 * 1. Valid Score Range: 0-100
 *    - A: [90-100]
 *    - B: [80-89]
 *    - C: [70-79]
 *    - D: [60-69]
 *    - F: [0-59]
 * 2. Invalid Score Range: < 0 or > 100
 *
 * Boundary Values:
 * - Score < 0 (e.g., -1, -100)
 * - Score = 0 (boundary for F)
 * - Score = 59 (boundary between F and D)
 * - Score = 60 (boundary for D)
 * - Score = 69 (boundary between D and C)
 * - Score = 70 (boundary for C)
 * - Score = 79 (boundary between C and B)
 * - Score = 80 (boundary for B)
 * - Score = 89 (boundary between B and A)
 * - Score = 90 (boundary for A)
 * - Score = 100 (boundary for A/valid)
 * - Score > 100 (e.g., 101, 150)
 */
@DisplayName("GradeCalculator Tests - Equivalence Partitioning & Boundary Value Analysis")
class GradeCalculatorTest {

    private GradeCalculator gradeCalculator;

    @BeforeEach
    void setUp() {
        gradeCalculator = new GradeCalculator();
    }

    // ============ EQUIVALENCE PARTITIONING TESTS ============

    @DisplayName("Equivalence Class: Grade A [90-100]")
    @ParameterizedTest
    @ValueSource(ints = {90, 91, 95, 99, 100})
    void testEquivalenceClassA(int score) {
        assertEquals("A", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return grade A");
    }

    @DisplayName("Equivalence Class: Grade B [80-89]")
    @ParameterizedTest
    @ValueSource(ints = {80, 81, 85, 89})
    void testEquivalenceClassB(int score) {
        assertEquals("B", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return grade B");
    }

    @DisplayName("Equivalence Class: Grade C [70-79]")
    @ParameterizedTest
    @ValueSource(ints = {70, 71, 75, 79})
    void testEquivalenceClassC(int score) {
        assertEquals("C", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return grade C");
    }

    @DisplayName("Equivalence Class: Grade D [60-69]")
    @ParameterizedTest
    @ValueSource(ints = {60, 61, 65, 69})
    void testEquivalenceClassD(int score) {
        assertEquals("D", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return grade D");
    }

    @DisplayName("Equivalence Class: Grade F [0-59]")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 30, 50, 59})
    void testEquivalenceClassF(int score) {
        assertEquals("F", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return grade F");
    }

    @DisplayName("Equivalence Class: Invalid Score (< 0)")
    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void testEquivalenceClassInvalidNegative(int score) {
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return Invalid");
    }

    @DisplayName("Equivalence Class: Invalid Score (> 100)")
    @ParameterizedTest
    @ValueSource(ints = {101, 110, 150, 200})
    void testEquivalenceClassInvalidPositive(int score) {
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(score),
                "Score " + score + " should return Invalid");
    }

    // ============ BOUNDARY VALUE ANALYSIS TESTS ============

    @Test
    @DisplayName("BVA: Lower Boundary of Valid Range - Score = 0 (F)")
    void testBVALowerBoundaryValid() {
        assertEquals("F", gradeCalculator.calculateLetterGrade(0));
    }

    @Test
    @DisplayName("BVA: Just Below Valid Range - Score = -1 (Invalid)")
    void testBVAJustBelowValid() {
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(-1));
    }

    @Test
    @DisplayName("BVA: Boundary between F and D - Score = 59 (F)")
    void testBVABoundaryFD_Lower() {
        assertEquals("F", gradeCalculator.calculateLetterGrade(59));
    }

    @Test
    @DisplayName("BVA: Boundary between F and D - Score = 60 (D)")
    void testBVABoundaryFD_Upper() {
        assertEquals("D", gradeCalculator.calculateLetterGrade(60));
    }

    @Test
    @DisplayName("BVA: Boundary between D and C - Score = 69 (D)")
    void testBVABoundaryDC_Lower() {
        assertEquals("D", gradeCalculator.calculateLetterGrade(69));
    }

    @Test
    @DisplayName("BVA: Boundary between D and C - Score = 70 (C)")
    void testBVABoundaryDC_Upper() {
        assertEquals("C", gradeCalculator.calculateLetterGrade(70));
    }

    @Test
    @DisplayName("BVA: Boundary between C and B - Score = 79 (C)")
    void testBVABoundaryCB_Lower() {
        assertEquals("C", gradeCalculator.calculateLetterGrade(79));
    }

    @Test
    @DisplayName("BVA: Boundary between C and B - Score = 80 (B)")
    void testBVABoundaryCB_Upper() {
        assertEquals("B", gradeCalculator.calculateLetterGrade(80));
    }

    @Test
    @DisplayName("BVA: Boundary between B and A - Score = 89 (B)")
    void testBVABoundaryBA_Lower() {
        assertEquals("B", gradeCalculator.calculateLetterGrade(89));
    }

    @Test
    @DisplayName("BVA: Boundary between B and A - Score = 90 (A)")
    void testBVABoundaryBA_Upper() {
        assertEquals("A", gradeCalculator.calculateLetterGrade(90));
    }

    @Test
    @DisplayName("BVA: Upper Boundary of Valid Range - Score = 100 (A)")
    void testBVAUpperBoundaryValid() {
        assertEquals("A", gradeCalculator.calculateLetterGrade(100));
    }

    @Test
    @DisplayName("BVA: Just Above Valid Range - Score = 101 (Invalid)")
    void testBVAJustAboveValid() {
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(101));
    }

    // ============ COMPREHENSIVE TESTS ============

    @Test
    @DisplayName("Comprehensive: Test all grade boundaries and invalid ranges")
    void testComprehensiveAllRanges() {
        // Invalid negative
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(-50));

        // F range
        assertEquals("F", gradeCalculator.calculateLetterGrade(0));
        assertEquals("F", gradeCalculator.calculateLetterGrade(30));
        assertEquals("F", gradeCalculator.calculateLetterGrade(59));

        // D range
        assertEquals("D", gradeCalculator.calculateLetterGrade(60));
        assertEquals("D", gradeCalculator.calculateLetterGrade(65));
        assertEquals("D", gradeCalculator.calculateLetterGrade(69));

        // C range
        assertEquals("C", gradeCalculator.calculateLetterGrade(70));
        assertEquals("C", gradeCalculator.calculateLetterGrade(75));
        assertEquals("C", gradeCalculator.calculateLetterGrade(79));

        // B range
        assertEquals("B", gradeCalculator.calculateLetterGrade(80));
        assertEquals("B", gradeCalculator.calculateLetterGrade(85));
        assertEquals("B", gradeCalculator.calculateLetterGrade(89));

        // A range
        assertEquals("A", gradeCalculator.calculateLetterGrade(90));
        assertEquals("A", gradeCalculator.calculateLetterGrade(95));
        assertEquals("A", gradeCalculator.calculateLetterGrade(100));

        // Invalid positive
        assertEquals("Invalid", gradeCalculator.calculateLetterGrade(150));
    }
}