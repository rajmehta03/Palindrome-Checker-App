import java.util.Scanner;
import java.util.LinkedList;

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");

        // Define the input string
        String input = sc.nextLine().toLowerCase();

        // Create a LinkedList to store characters
        LinkedList<Character> list = new LinkedList<>();

        // Add each character to the linked list
        for (char c : input.toCharArray()) {
            list.add(c);
        }

        // Flag to track palindrome state
        boolean isPalindrome = true;

        // Compare until only one or zero elements remain
        while (list.size() > 1) {
            // Remove the first and last elements for comparison
            char first = list.removeFirst();
            char last = list.removeLast();

            if (first != last) {
                isPalindrome = false;
                break;
            }
        }

        // Display the result
        System.out.println("\n--- UC8 Built-in LinkedList Results ---");
        System.out.println("Input Text: " + input);
        System.out.println("Is it a palindrome? " + isPalindrome);

        sc.close();
    }
}