import java.util.*;

abstract class Person {
    protected String name;
    protected String email;
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public abstract void displayInfo();
}

class Student extends Person {
    private int rollNo;
    private String course;
    private double marks;
    private String grade;

    public Student(int rollNo, String name, String email, String course, double marks, String grade) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        this.grade = grade;
    }

    @Override
    public void displayInfo() {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Grade: " + grade);
        System.out.println();
    }

    public void displayInfo(String researchArea) {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Research Area: " + researchArea);
        System.out.println();
    }

    public int getRollNo() {
        return rollNo;
    }
    public double getMarks() {
        return marks;
    }
    public String getGrade() {
        return grade;
    }
    public void setMarks(double marks) {
        this.marks = marks;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Finalize method called before object is garbage collected.");
    }
}

interface RecordActions {
    void addStudent(Student student);
    void deleteStudent(int rollNo);
    void updateStudent(int rollNo, double newMarks, String newGrade);
    Student searchStudent(int rollNo);
    void viewAllStudents();
}

class StudentManager implements RecordActions {
    private Map<Integer, Student> studentMap = new HashMap<>();

    @Override
    public void addStudent(Student student) {
        if (studentMap.containsKey(student.getRollNo())) {
            System.out.println("Duplicate roll number! Cannot add student.\n");
        } else {
            studentMap.put(student.getRollNo(), student);
            System.out.println("Student added successfully.\n");
        }
    }

    @Override
    public void deleteStudent(int rollNo) {
        if (studentMap.remove(rollNo) != null) {
            System.out.println("Student with Roll No " + rollNo + " deleted successfully.\n");
        } else {
            System.out.println("No student found with Roll No " + rollNo + ".\n");
        }
    }

    @Override
    public void updateStudent(int rollNo, double newMarks, String newGrade) {
        Student student = studentMap.get(rollNo);
        if (student != null) {
            student.setMarks(newMarks);
            student.setGrade(newGrade);
            System.out.println("Student record updated successfully.\n");
        } else {
            System.out.println("Student not found.\n");
        }
    }

    @Override
    public Student searchStudent(int rollNo) {
        return studentMap.get(rollNo);
    }

    @Override
    public void viewAllStudents() {
        if (studentMap.isEmpty()) {
            System.out.println("No student records found.\n");
        } else {
            for (Student s : studentMap.values()) {
                s.displayInfo();
            }
        }
    }

    public void viewAllStudents(boolean showCount) {
        viewAllStudents();
        if (showCount) {
            System.out.println("Total Students: " + studentMap.size() + "\n");
        }
    }
}

public class StudentManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        while (true) {
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. View Count");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter Roll No: ");
                    int roll = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = sc.nextDouble(); sc.nextLine();
                    System.out.print("Enter Grade: ");
                    String grade = sc.nextLine();
                    manager.addStudent(new Student(roll, name, email, course, marks, grade));
                    break;

                case 2:
                    manager.viewAllStudents();
                    break;

                case 3:
                    System.out.print("Enter Roll No to Search: ");
                    int r = sc.nextInt();
                    Student found = manager.searchStudent(r);
                    if (found != null) {
                        found.displayInfo();
                        System.out.print("Enter Research Area (optional): ");
                        sc.nextLine();
                        String res = sc.nextLine();
                        if (!res.isEmpty()) found.displayInfo(res);
                    } else {
                        System.out.println("Student not found.\n");
                    }
                    break;

                case 4:
                    System.out.print("Enter Roll No to Update: ");
                    int ur = sc.nextInt();
                    System.out.print("Enter New Marks: ");
                    double newMarks = sc.nextDouble(); sc.nextLine();
                    System.out.print("Enter New Grade: ");
                    String newGrade = sc.nextLine();
                    manager.updateStudent(ur, newMarks, newGrade);
                    break;

                case 5:
                    System.out.print("Enter Roll No to Delete: ");
                    int dr = sc.nextInt();
                    manager.deleteStudent(dr);
                    break;

                case 6:
                    manager.viewAllStudents(true);
                    break;

                case 7:
                    System.out.println("Exiting...");
                    sc.close();
                    System.gc();
                    return;

                default:
                    System.out.println("Invalid choice.\n");
            }
        }
    }
}
