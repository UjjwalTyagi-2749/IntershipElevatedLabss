import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BankApp {
    private static Scanner myScanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("----------------------------------------");
        System.out.println("Welcome to the Bank Management System");
        System.out.println("----------------------------------------");

        System.out.print("Enter your account holder name: ");
        String name = myScanner.nextLine();
        
        System.out.println("\nCreating new account for " + name + "...");
        Account acc = new Account("1001", name);
        System.out.println("Account created successfully!");
        System.out.println("Your account number is: " + acc.getAccountNumber());
        
        System.out.print("\nEnter your initial deposit amount: ");
        double initialDeposit = myScanner.nextDouble();
        myScanner.nextLine(); // Consume the newline
        
        System.out.println("\nDepositing " + initialDeposit + "...");
        acc.deposit(initialDeposit);

        System.out.println("\n--- Final Account Summary for " + acc.getHolderName() + " ---");
        System.out.println("Account Number: " + acc.getAccountNumber());
        System.out.println("Current Balance: " + acc.getBalance());
        System.out.println("Transaction History:");
        for (String t : acc.getTransactionHistory()) {
            System.out.println("  -> " + t);
        }
        System.out.println("--- End of Report ---");
        
        System.out.println("\nApplication shutting down. Goodbye!");
        myScanner.close();
    }

    public static void runExtraFeatures(Account userAccount) {
        System.out.println("Calculating a temporary bonus for the account...");
        double bonus = userAccount.calculateBonus();
        System.out.println("Your calculated bonus is: " + bonus);
        
        System.out.println("Applying a temporary interest calculation...");
        userAccount.processInterest();
        System.out.println("Interest has been processed.");
    }
}

class Account {
    private String accountNumber;
    private String holderName;
    private double balance;
    private List<String> transactions;
    private DateTimeFormatter fmt;
    
    private int securityCode;
    private String branchLocation;
    private boolean isActive;
    private long lastAccessTime;
    private String accountType;

    public Account(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        this.fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Random rand = new Random();
        this.securityCode = rand.nextInt(9000) + 1000;
        this.branchLocation = "Main Branch";
        this.isActive = true;
        this.lastAccessTime = System.currentTimeMillis();
        this.accountType = "Standard Savings";
        
        record("ACCOUNT_OPENED", 0.0, "Account initialized with 0 balance");
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            record("DEPOSIT_FAILED", amount, "Invalid deposit amount entered");
            return false;
        }
        balance = balance + amount;
        record("DEPOSIT_SUCCESS", amount, "Cash deposit was successful");
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            record("WITHDRAW_FAILED", amount, "Invalid withdrawal amount entered");
            return false;
        }
        if (amount > balance) {
            record("WITHDRAW_FAILED", amount, "Withdrawal failed due to insufficient funds");
            return false;
        }
        balance = balance - amount;
        record("WITHDRAW_SUCCESS", amount, "Cash withdrawal was successful");
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    private void record(String type, double amount, String note) {
        String time = LocalDateTime.now().format(fmt);
        String entry = String.format("%s | Action: %s | Amount: %.2f | Status: %s | Current Balance: %.2f", time, type, amount, note, balance);
        transactions.add(entry);
        System.out.println("  >> " + entry);
    }
    
    public double calculateBonus() {
        return balance * 0.01;
    }
    
    public void processInterest() {
        if (balance > 0) {
            double interestRate = 0.005;
            double interestAmount = balance * interestRate;
            deposit(interestAmount);
        }
    }
}
