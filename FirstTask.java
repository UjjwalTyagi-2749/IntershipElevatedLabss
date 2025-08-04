import java.util.Scanner;

public class FirstTask {
    
    // Method for addition
    public static double add(double a, double b) {
        return a + b;
    }
    
    // Method for subtraction
    public static double subtract(double a, double b) {
        return a - b;
    }
    
    // Method for multiplication
    public static double multiply(double a, double b) {
        return a * b;
    }
    
    // Method for division
    public static double divide(double a, double b) {
        if (b == 0) {
            System.out.println("Error: Division by zero is not allowed!");
            return Double.NaN; // Not a Number
        }
        return a / b;
    }
    
    // Method to display the menu
    public static void displayMenu() {
        System.out.println("\n===== CALCULATOR MENU =====");
        System.out.println("1. Addition (+)");
        System.out.println("2. Subtraction (-)");
        System.out.println("3. Multiplication (*)");
        System.out.println("4. Division (/)");
        System.out.println("5. Exit");
        System.out.print("Choose an operation (1-5): ");
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculating = true;
        
        System.out.println("Welcome to the Java Calculator!");
        
        // Main loop for multiple calculations
        while (continueCalculating) {
            displayMenu();
            
            int choice = scanner.nextInt();
            
            // Exit condition
            if (choice == 5) {
                System.out.println("Thank you for using the calculator. Goodbye!");
                continueCalculating = false;
                continue;
            }
            
            // Validate choice
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice! Please select 1-5.");
                continue;
            }
            
            // Get numbers from user
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            
            double result = 0;
            boolean validOperation = true;
            
            // Switch statement to handle different operations
            switch (choice) {
                case 1:
                    result = add(num1, num2);
                    System.out.printf("%.2f + %.2f = %.2f\n", num1, num2, result);
                    break;
                case 2:
                    result = subtract(num1, num2);
                    System.out.printf("%.2f - %.2f = %.2f\n", num1, num2, result);
                    break;
                case 3:
                    result = multiply(num1, num2);
                    System.out.printf("%.2f * %.2f = %.2f\n", num1, num2, result);
                    break;
                case 4:
                    result = divide(num1, num2);
                    if (!Double.isNaN(result)) {
                        System.out.printf("%.2f / %.2f = %.2f\n", num1, num2, result);
                    }
                    break;
                default:
                    System.out.println("Invalid operation!");
                    validOperation = false;
            }
            
            // Ask if user wants to continue
            if (validOperation) {
                System.out.print("\nDo you want to perform another calculation? (y/n): ");
                char continueChoice = scanner.next().toLowerCase().charAt(0);
                
                if (continueChoice != 'y') {
                    System.out.println("Thank you for using the calculator. Goodbye!");
                    continueCalculating = false;
                }
            }
        }
        
        scanner.close(); // Good practice to close the scanner
    }
}