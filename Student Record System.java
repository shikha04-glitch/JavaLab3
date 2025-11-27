
import java.util.ArrayList;
import java.util.Scanner;


class Person {
    String name;

    Person() {}

    Person(String name) {
        this.name = name;
    }
}


class Student extends Person {
    int rollNo;
    String course;
    double marks;
    char grade;

   
    Student() {}

    
    Student(int rollNo, String name, String course, double marks) {
        super(name);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

   
    void inputDetails(Scanner sc) {
        System.out.print("Enter Roll No: ");
        rollNo = sc.nextInt();
        sc.nextLine(); 

        System.out.print("Enter Name: ");
        name = sc.nextLine();

        System.out.print("Enter Course: ");
        course = sc.nextLine();

        do {
            System.out.print("Enter Marks (0-100): ");
            marks = sc.nextDouble();
            if (marks < 0 || marks > 100) {
                System.out.println("Invalid marks! Please enter between 0 and 100.");
            }
        } while (marks < 0 || marks > 100);

        calculateGrade();
    }

    
    void displayDetails() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("--------------------------------");
    }

    
    void calculateGrade() {
        if (marks >= 90)
            grade = 'A';
        else if (marks >= 75)
            grade = 'B';
        else if (marks >= 60)
            grade = 'C';
        else if (marks >= 40)
            grade = 'D';
        else
            grade = 'F';
    }
}


public class StudentRecordSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();
        int choice;

        do {
            System.out.println("\n===== Student Record Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Student s = new Student();
                    s.inputDetails(sc);
                    students.add(s);
                    System.out.println("Student record added successfully!");
                    break;

                case 2:
                    if (students.isEmpty()) {
                        System.out.println("No student records found!");
                    } else {
                        System.out.println("\n===== All Student Records =====");
                        for (Student st : students) {
                            st.displayDetails();
                        }
                    }
                    break;

                case 3:
                    System.out.println("Exiting the application. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 3);

        sc.close();
    }
}
