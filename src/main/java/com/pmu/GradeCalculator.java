package com.pmu;


public class GradeCalculator {

    /**
     * Converts numeric score (0–100) into a letter grade (A–F).
     * If score is invalid, returns "Invalid".
     */
    public  String calculateLetterGrade(int score) {
        if (score < 0 || score > 100) {
            return "Invalid";
        }

        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }



}
