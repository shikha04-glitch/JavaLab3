import java.util.*;


class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}


class Loader implements Runnable {
    private String task;

    public Loader(String task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            System.out.print(task);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted!");
        }
    }
}


interface RecordActions {
    void addStudent();
    void displayAllStudents();
    void searchStudent(int rollNo) throws StudentNotFoundException;
}


class Student {
    private Integer rollNo;    
    private String name;
    private String email;
    private String course;
    private Double marks;      
    private char grade;

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    private void calculateGrade() {
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

    public Integer getRollNo() {
        return rollNo;
    }

    public void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println("-----------------------------------");
    }
}


class StudentManager implements RecordActions {
    private ArrayList<Student> students = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    @Override
    public void addStudent() {
        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer rollNo = sc.nextInt(); 
            sc.nextLine(); 

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty!");

            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            if (email.isEmpty()) throw new IllegalArgumentException("Email cannot be empty!");

            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            if (course.isEmpty()) throw new IllegalArgumentException("Course cannot be empty!");

            System.out.print("Enter Marks: ");
            Double marks = sc.nextDouble(); 
            if (marks < 0 || marks > 100)
                throw new IllegalArgumentException("Marks must be between 0 and 100!");

          
            Thread loader = new Thread(new Loader("Loading"));
            loader.start();
            loader.join();

           
            Student s = new Student(rollNo, name, email, course, marks);
            students.add(s);
            System.out.println("Student added successfully!\n");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input type! Please enter correct data.");
            sc.nextLine(); 
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted during loading.");
        } finally {
            System.out.println("Operation completed.\n");
        }
    }

    @Override
    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
        } else {
            for (Student s : students) {
                s.display();
            }
        }
    }

    @Override
    public void searchStudent(int rollNo) throws StudentNotFoundException {
        boolean found = false;
        for (Student s : students) {
            if (s.getRollNo().equals(rollNo)) {
                s.display();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new StudentNotFoundException("Student with Roll No " + rollNo + " not found!");
        }
    }
}


public class StudentManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        int choice;

        do {
            System.out.println("\n===== Student Management Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addStudent();
                    break;
                case 2:
                    manager.displayAllStudents();
                    break;
                case 3:
                    System.out.print("Enter Roll No to search: ");
                    int roll = sc.nextInt();
                    try {
                        manager.searchStudent(roll);
                    } catch (StudentNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 4);

        sc.close();
    }
}
