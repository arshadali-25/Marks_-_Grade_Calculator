
import java.io.*;
import java.util.*;

public class MarksGradeCalculator {

    static final int SUBJECTS = 5;
    static final int MAX_MARK = 100;
    static final String DATA_FILE = "marks.txt";

    static String studentName = "";
    static int[] marks = new int[SUBJECTS];
    static boolean hasData = false;

    // Read an integer from the user
    static int readInt(Scanner sc) {
        String line = sc.nextLine().trim();

        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    // Validate marks between 0 and 100
    static int readValidMark(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            int value = readInt(sc);

            if (value >= 0 && value <= MAX_MARK)
                return value;

            System.out.println(
                "Invalid marks. Enter a value between 0 and 100."
            );
        }
    }

    // Enter student name and marks
    static void enterMarks(Scanner sc) {
        System.out.print("Student name: ");
        String name = sc.nextLine().trim();

        int[] temp = new int[SUBJECTS];

        for (int i = 0; i < SUBJECTS; i++) {
            temp[i] = readValidMark(
                sc, "Subject " + (i + 1) + ": "
            );
        }

        studentName = name.isEmpty() ? "Student" : name;
        marks = temp;
        hasData = true;

        displayResult();
    }

    // Calculate total marks
    static int calculateTotal(int[] m) {
        int total = 0;

        for (int i = 0; i < m.length; i++) {
            total += m[i];
        }

        return total;
    }

    // Calculate percentage
    static double calculatePercentage(int total) {
        return total * 100.0 / (SUBJECTS * MAX_MARK);
    }

    // Calculate grade
    static String getGrade(double p) {
        if (p >= 90) return "A+";
        if (p >= 75) return "A";
        if (p >= 60) return "B";
        if (p >= 40) return "C (Pass)";

        return "F (Fail)";
    }

    // Display result
    static void displayResult() {
        int total = calculateTotal(marks);
        double percentage = calculatePercentage(total);

        System.out.printf(
            "%s | Total: %d/%d | %.1f%% | Grade %s%n",
            studentName,
            total,
            SUBJECTS * MAX_MARK,
            percentage,
            getGrade(percentage)
        );
    }

    // Find highest and lowest marks
    static void findHighestLowest(int[] m) {
        int hi = 0, lo = 0;

        for (int i = 1; i < m.length; i++) {
            if (m[i] > m[hi])
                hi = i;

            if (m[i] < m[lo])
                lo = i;
        }

        System.out.println(
            "Highest: Subject " + (hi + 1) +
            " (" + m[hi] + ") | Lowest: Subject " +
            (lo + 1) + " (" + m[lo] + ")"
        );
    }

    // Sort marks in descending order
    static int[] sortDescending(int[] m) {
        int[] s = Arrays.copyOf(m, m.length);

        for (int i = 0; i < s.length - 1; i++) {
            int max = i;

            for (int j = i + 1; j < s.length; j++) {
                if (s[j] > s[max])
                    max = j;
            }

            int t = s[i];
            s[i] = s[max];
            s[max] = t;
        }

        return s;
    }

    // Print sorted marks
    static void printSorted(int[] m) {
        int[] s = sortDescending(m);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length; i++) {
            if (i > 0)
                sb.append(", ");

            sb.append(s[i]);
        }

        System.out.println(sb);
    }

    // Search for a particular mark
    static int searchMark(int[] m, int key) {
        for (int i = 0; i < m.length; i++) {
            if (m[i] == key)
                return i + 1;
        }

        return -1;
    }

    // Load saved data from file
    static void loadFromFile() {
        try (
            BufferedReader br =
                new BufferedReader(new FileReader(DATA_FILE))
        ) {
            String name = br.readLine();
            String line = br.readLine();

            if (name == null || line == null)
                return;

            String[] parts = line.split(",");

            if (parts.length != SUBJECTS)
                return;

            int[] temp = new int[SUBJECTS];

            for (int i = 0; i < SUBJECTS; i++) {
                int v = Integer.parseInt(parts[i].trim());

                if (v < 0 || v > MAX_MARK)
                    return;

                temp[i] = v;
            }

            studentName = name;
            marks = temp;
            hasData = true;

            System.out.println(
                "Saved data loaded for " + studentName + "."
            );

        } catch (IOException | NumberFormatException e) {
            // No saved data available
        }
    }

    // Save data to file
    static void saveToFile() {
        if (!hasData)
            return;

        try (
            PrintWriter pw =
                new PrintWriter(new FileWriter(DATA_FILE))
        ) {
            pw.println(studentName);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < SUBJECTS; i++) {
                if (i > 0)
                    sb.append(",");

                sb.append(marks[i]);
            }

            pw.println(sb);

            System.out.println(
                "Data saved to " + DATA_FILE + "."
            );

        } catch (IOException e) {
            System.out.println(
                "Could not save data: " + e.getMessage()
            );
        }
    }

    // Display menu
    static void displayMenu() {
        System.out.println(
            "\n===== MARKS & GRADE CALCULATOR ====="
        );

        System.out.println("1. Enter marks");
        System.out.println("2. Show result");
        System.out.println("3. Highest / Lowest");
        System.out.println("4. Sort marks (high to low)");
        System.out.println("5. Search a mark");
        System.out.println("6. Save and Exit");

        System.out.print("Enter your choice: ");
    }

    // Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        loadFromFile();

        int choice = 0;

        while (choice != 6) {
            displayMenu();

            choice = readInt(sc);

            if (choice < 1 || choice > 6) {
                System.out.println(
                    "Invalid choice. Enter 1 to 6."
                );
                continue;
            }

            if (choice == 1) {
                enterMarks(sc);

            } else if (choice == 6) {
                saveToFile();

            } else if (!hasData) {
                System.out.println(
                    "No marks entered yet. Choose option 1 first."
                );

            } else if (choice == 2) {
                displayResult();

            } else if (choice == 3) {
                findHighestLowest(marks);

            } else if (choice == 4) {
                printSorted(marks);

            } else {
                int key = readValidMark(
                    sc, "Enter mark to search: "
                );

                int subject = searchMark(marks, key);

                if (subject == -1) {
                    System.out.println(
                        "Mark " + key + " not found."
                    );
                } else {
                    System.out.println(
                        "Mark " + key +
                        " found in Subject " + subject
                    );
                }
            }
        }

        sc.close();
    }
}
