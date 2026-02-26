import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue; // Import Queue interface

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");
        String s1 = sc.nextLine();

        // Normalize input for comparison (lowercase)
        String input = s1.toLowerCase();

        // --- UC6: Queue + Stack Based Palindrome Check ---

        // 1. Initialize Data Structures
        Stack<Character> stack = new Stack<>();
        Queue<Character> queue = new LinkedList<>(); // Queue is an interface; LinkedList is a common implementation

        // 2. Enqueue & Push Operations
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            stack.push(c);    // LIFO
            queue.add(c);     // FIFO (Enqueue)
        }

        // 3. Logical Comparison
        boolean pal = true;
        while (!stack.isEmpty()) {
            // Pop (Last character) vs Dequeue (First character)
            char fromStack = stack.pop();
            char fromQueue = queue.remove(); // Dequeue operation

            if (fromStack != fromQueue) {
                pal = false;
                break;
            }
        }

        // 4. Display the result
        System.out.println("\n--- UC6 Queue + Stack Analysis ---");
        System.out.println("Input Text: " + s1);
        System.out.println("Is it a palindrome? " + pal);

        sc.close();
    }
}