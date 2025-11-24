package com.pmu;


public class Course {
    private String courseName;
    private int creditHours;
    private String letterGrade;

    public Course(String courseName, int creditHours, String letterGrade) {

        if (courseName == null || courseName.isEmpty()) {
            this.courseName = "Unknown";
        } else {
            this.courseName = courseName;
        }

        if (creditHours < 1 || creditHours > 6) {
            this.creditHours = 3; // default credit hours
        } else {
            this.creditHours = creditHours;
        }

        if (isValidGrade(letterGrade)) {
            this.letterGrade = letterGrade;
        } else {
            this.letterGrade = "F"; // default grade
        }
    }

    private boolean isValidGrade(String grade) {
        if (grade == null) return false;
        return grade.equals("A") || grade.equals("B") ||
                grade.equals("C") || grade.equals("D") || grade.equals("F");
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        switch (letterGrade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

}
