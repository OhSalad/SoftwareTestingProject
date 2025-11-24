package com.pmu;

/**
 * Driver Program for GradeCalculator Testing
 * 
 * This driver tests the calculateLetterGrade method using:
 * 1. Equivalence Partitioning (Black-box testing)
 * 2. Boundary Value Analysis (Black-box testing)
 * 
 * Test Strategy:
 * - Divides the input range (0-100) into equivalence classes
 * - Tests boundary values to ensure correct transitions between grades
 * - Tests invalid inputs (< 0 and > 100)
 */
public class GradeCalculatorDriver {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        printSeparator('=', 80);
        System.out.println("Grade Calculator - Black Box Testing Driver");
        System.out.println("Testing: calculateLetterGrade(int score)");
        printSeparator('=', 80);
        System.out.println();

        GradeCalculator calculator = new GradeCalculator();

        // Run all test categories
        testEquivalencePartitioning(calculator);
        System.out.println();
        testBoundaryValueAnalysis(calculator);
        System.out.println();

        // Print summary
        printTestSummary();
    }

    private static void printSeparator(char c, int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(c);
        }
        System.out.println();
    }

    /**
     * Test using Equivalence Partitioning
     * Divides input space into equivalence classes and tests one value from each
     */
    private static void testEquivalencePartitioning(GradeCalculator calculator) {
        printSeparator('=', 80);
        System.out.println("1. EQUIVALENCE PARTITIONING TESTING");
        printSeparator('=', 80);
        System.out.println();

        // Valid Equivalence Classes
        System.out.println("Valid Equivalence Classes:");
        printSeparator('-', 80);

        // Class 1: Grade A [90-100]
        System.out.println("EC1: Grade A [90-100]");
        testScores(calculator, new int[]{90, 95, 100}, "A", "Grade A");

        // Class 2: Grade B [80-89]
        System.out.println("\nEC2: Grade B [80-89]");
        testScores(calculator, new int[]{80, 85, 89}, "B", "Grade B");

        // Class 3: Grade C [70-79]
        System.out.println("\nEC3: Grade C [70-79]");
        testScores(calculator, new int[]{70, 75, 79}, "C", "Grade C");

        // Class 4: Grade D [60-69]
        System.out.println("\nEC4: Grade D [60-69]");
        testScores(calculator, new int[]{60, 65, 69}, "D", "Grade D");

        // Class 5: Grade F [0-59]
        System.out.println("\nEC5: Grade F [0-59]");
        testScores(calculator, new int[]{0, 30, 59}, "F", "Grade F");

        // Invalid Equivalence Classes
        System.out.println("\n");
        printSeparator('-', 80);
        System.out.println("Invalid Equivalence Classes:");
        printSeparator('-', 80);

        // Class 6: Score < 0
        System.out.println("EC6: Invalid Score (< 0)");
        testScores(calculator, new int[]{-1, -50, -100}, "Invalid", "Negative score");

        // Class 7: Score > 100
        System.out.println("\nEC7: Invalid Score (> 100)");
        testScores(calculator, new int[]{101, 150, 200}, "Invalid", "Score > 100");
    }

    /**
     * Test using Boundary Value Analysis
     * Tests critical values at the boundaries between equivalence classes
     */
    private static void testBoundaryValueAnalysis(GradeCalculator calculator) {
        printSeparator('=', 80);
        System.out.println("2. BOUNDARY VALUE ANALYSIS TESTING");
        printSeparator('=', 80);
        System.out.println();

        System.out.println("Boundary Values and Transitions:");
        printSeparator('-', 80);

        // Boundary: Start of valid range (0 = F)
        System.out.println("BVA1: Lower Boundary of Valid Range");
        testScore(calculator, 0, "F", "Score = 0 (Valid/F)");

        // Boundary: Just below valid range (-1 = Invalid)
        System.out.println("\nBVA2: Just Below Valid Range");
        testScore(calculator, -1, "Invalid", "Score = -1 (Invalid)");

        // Boundary: Between F and D (59/60)
        System.out.println("\nBVA3: Boundary between F and D");
        testScore(calculator, 59, "F", "Score = 59 (F)");
        testScore(calculator, 60, "D", "Score = 60 (D)");

        // Boundary: Between D and C (69/70)
        System.out.println("\nBVA4: Boundary between D and C");
        testScore(calculator, 69, "D", "Score = 69 (D)");
        testScore(calculator, 70, "C", "Score = 70 (C)");

        // Boundary: Between C and B (79/80)
        System.out.println("\nBVA5: Boundary between C and B");
        testScore(calculator, 79, "C", "Score = 79 (C)");
        testScore(calculator, 80, "B", "Score = 80 (B)");

        // Boundary: Between B and A (89/90)
        System.out.println("\nBVA6: Boundary between B and A");
        testScore(calculator, 89, "B", "Score = 89 (B)");
        testScore(calculator, 90, "A", "Score = 90 (A)");

        // Boundary: End of valid range (100 = A)
        System.out.println("\nBVA7: Upper Boundary of Valid Range");
        testScore(calculator, 100, "A", "Score = 100 (Valid/A)");

        // Boundary: Just above valid range (101 = Invalid)
        System.out.println("\nBVA8: Just Above Valid Range");
        testScore(calculator, 101, "Invalid", "Score = 101 (Invalid)");
    }

    /**
     * Test a single score and verify the result
     */
    private static void testScore(GradeCalculator calculator, int score, String expectedGrade, String description) {
        totalTests++;
        String result = calculator.calculateLetterGrade(score);
        boolean passed = result.equals(expectedGrade);

        if (passed) {
            passedTests++;
            System.out.printf("  PASS: %s | Expected: %s | Got: %s%n", description, expectedGrade, result);
        } else {
            failedTests++;
            System.out.printf("  FAIL: %s | Expected: %s | Got: %s%n", description, expectedGrade, result);
        }
    }

    /**
     * Test multiple scores and verify they all return the expected grade
     */
    private static void testScores(GradeCalculator calculator, int[] scores, String expectedGrade, String description) {
        for (int score : scores) {
            testScore(calculator, score, expectedGrade, description + " - Score: " + score);
        }
    }

    /**
     * Print test summary statistics
     */
    private static void printTestSummary() {
        printSeparator('=', 80);
        System.out.println("TEST SUMMARY");
        printSeparator('=', 80);
        System.out.printf("Total Tests Run:  %d%n", totalTests);
        System.out.printf("Tests Passed:     %d (%.1f%%)%n", passedTests, (passedTests * 100.0 / totalTests));
        System.out.printf("Tests Failed:     %d (%.1f%%)%n", failedTests, (failedTests * 100.0 / totalTests));
        printSeparator('=', 80);

        if (failedTests == 0) {
            System.out.println("All tests passed! The calculateLetterGrade method is working correctly.");
        } else {
            System.out.println("Some tests failed. Please review the implementation.");
        }
        printSeparator('=', 80);
    }
}

