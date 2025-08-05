import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class StudentManagementSystem {

    public static class Student {
        private int id;
        private String name;
        private ArrayList<Double> marks;

        public Student(int id, String name, ArrayList<Double> marks) {
            this.id = id;
            this.name = name;
            this.marks = marks;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Double> getMarks() {
            return marks;
        }

        public void setMarks(ArrayList<Double> marks) {
            this.marks = marks;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Marks: " + marks;
        }
    }

    public static class StudentService {
        private ArrayList<Student> students = new ArrayList<>();

        public boolean addStudent(Student newStudent) {
            for (Student s : students) {
                if (s.getId() == newStudent.getId()) {
                    return false;
                }
            }
            students.add(newStudent);
            return true;
        }

        public ArrayList<Student> getAllStudents() {
            return students;
        }
        
        public Optional<Student> getStudentById(int id) {
            for (Student student : students) {
                if (student.getId() == id) {
                    return Optional.of(student);
                }
            }
            return Optional.empty();
        }

        public boolean updateStudent(int id, String newName, ArrayList<Double> newMarks) {
            Optional<Student> studentOptional = getStudentById(id);
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                student.setName(newName);
                student.setMarks(newMarks);
                return true;
            }
            return false;
        }

        public boolean deleteStudent(int id) {
            return students.removeIf(student -> student.getId() == id);
        }
    }

    private static StudentService studentService = new StudentService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            displayMenu();
            try {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewStudents();
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        System.out.println("Exiting the application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
            }
        } while (choice != 5);
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n--- Student Management System ---");
        System.out.println("1. Add a new student");
        System.out.println("2. View all students");
        System.out.println("3. Update a student's details");
        System.out.println("4. Delete a student");
        System.out.println("5. Exit");
    }

    private static void addStudent() {
        try {
            System.out.print("Enter student ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter student name: ");
            String name = scanner.nextLine();

            ArrayList<Double> marksList = getMarksFromUser();
            
            Student newStudent = new Student(id, name, marksList);
            if (studentService.addStudent(newStudent)) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Error: Student with ID " + id + " already exists.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for ID/marks.");
            scanner.nextLine();
        }
    }

    private static void viewStudents() {
        ArrayList<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("\n--- Student List ---");
            students.forEach(System.out::println);
        }
    }

    private static void updateStudent() {
        try {
            System.out.print("Enter student ID to update: ");
            int idToUpdate = scanner.nextInt();
            scanner.nextLine();

            Optional<Student> studentOptional = studentService.getStudentById(idToUpdate);
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();

                System.out.print("Enter new name (current: " + student.getName() + "): ");
                String newName = scanner.nextLine();

                System.out.print("Update marks? (yes/no): ");
                String updateMarksChoice = scanner.nextLine().toLowerCase();
                ArrayList<Double> newMarksList = student.getMarks();
                if (updateMarksChoice.equals("yes")) {
                    newMarksList = getMarksFromUser();
                }

                studentService.updateStudent(idToUpdate, newName, newMarksList);
                System.out.println("Student details updated successfully!");
            } else {
                System.out.println("Student with ID " + idToUpdate + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for ID.");
            scanner.nextLine();
        }
    }

    private static void deleteStudent() {
        try {
            System.out.print("Enter student ID to delete: ");
            int idToDelete = scanner.nextInt();
            scanner.nextLine();

            if (studentService.deleteStudent(idToDelete)) {
                System.out.println("Student with ID " + idToDelete + " deleted successfully!");
            } else {
                System.out.println("Student with ID " + idToDelete + " not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number for ID.");
            scanner.nextLine();
        }
    }
    
    private static ArrayList<Double> getMarksFromUser() {
        ArrayList<Double> marksList = new ArrayList<>();
        String addMore;
        do {
            System.out.print("Enter a mark for a subject: ");
            double mark = scanner.nextDouble();
            marksList.add(mark);
            scanner.nextLine();

            System.out.print("Add another mark? (yes/no): ");
            addMore = scanner.nextLine().toLowerCase();
        } while (addMore.equals("yes"));
        return marksList;
    }
}