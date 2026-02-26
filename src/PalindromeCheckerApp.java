import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque; // Efficient Deque implementation

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");
        String s1 = sc.nextLine();

        // Normalize for comparison
        String input = s1.toLowerCase();

        // --- UC7: Deque-Based Optimized Palindrome Checker ---

        // 1. Initialize Deque
        // ArrayDeque is faster than LinkedList for stack/queue operations
        Deque<Character> deque = new ArrayDeque<>();

        // 2. Insert characters into Deque (Front to Rear)
        for (int i = 0; i < input.length(); i++) {
            deque.addLast(input.charAt(i));
        }

        // 3. Compare until empty or only 1 character remains
        boolean pal = true;
        while (deque.size() > 1) {
            // Remove from both ends
            char first = deque.removeFirst();
            char last = deque.removeLast();

            if (first != last) {
                pal = false;
                break;
            }
        }

        // 4. Display the result
        System.out.println("\n--- UC7 Deque Analysis ---");
        System.out.println("Input Text: " + s1);
        System.out.println("Is it a palindrome? " + pal);

        sc.close();
    }
}