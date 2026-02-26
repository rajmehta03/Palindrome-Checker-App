import java.util.Scanner;
import java.util.Stack; // Import Stack for UC5 logic

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");
        String s1 = sc.nextLine();

        // --- UC5: Stack Based Palindrome Check ---

        // 1. Initialize a Stack of Characters
        // Using the Java Stack class: https://docs.oracle.com
        Stack<Character> stack = new Stack<>();

        // 2. Push Operation: Insert all characters into the stack
        // This places the last character of the string at the top of the stack.
        for (int i = 0; i < s1.length(); i++) {
            stack.push(s1.charAt(i));
        }

        // 3. Pop Operation: Build reversed string by removing from stack
        // Due to LIFO (Last In First Out), popping creates a reversed version of s1.
        String reversed = "";
        while (!stack.isEmpty()) {
            reversed += stack.pop();
        }

        // 4. Validation: Compare original and reversed
        boolean pal = s1.equalsIgnoreCase(reversed);

        // 5. Display the result
        System.out.println("Input Text: " + s1);
        System.out.println("Reversed via Stack: " + reversed);
        System.out.println("Is it a palindrome? " + pal);

        sc.close();
    }
}