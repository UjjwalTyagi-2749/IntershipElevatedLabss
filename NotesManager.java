import java.io.*;
import java.util.*;

public class NotesManager {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== TEXT NOTES MANAGER =====");
        System.out.println("File: notes.txt");
        File file = new File("notes.txt");
        if (!file.exists()) file.createNewFile();

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Write a new note");
            System.out.println("2. Read all notes");
            System.out.println("3. Clear all notes");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                FileWriter fw = new FileWriter(file, true);
                System.out.println("Enter your note (single line):");
                String note = sc.nextLine();
                if (!note.trim().isEmpty()) {
                    fw.write(note + "\n");
                    System.out.println("Note saved.");
                } else {
                    System.out.println("Empty note ignored.");
                }
                fw.close();
            } 
            else if (choice.equals("2")) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                System.out.println("\n--- Your Notes ---");
                boolean empty = true;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    empty = false;
                }
                if (empty) System.out.println("(No notes yet)");
                br.close();
            }
            else if (choice.equals("3")) {
                FileWriter fw = new FileWriter(file);
                fw.write("");
                fw.close();
                System.out.println("All notes cleared.");
            }
            else if (choice.equals("4")) {
                System.out.println("Exiting Notes Manager.");
                break;
            }
            else {
                System.out.println("Invalid choice, try again.");
            }
        }
        sc.close();
    }
}
