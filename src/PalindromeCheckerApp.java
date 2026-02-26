import java.util.Scanner;

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");
        String s1 = sc.nextLine();

        // UC4 Logic: Character Array Based Palindrome Check

        // 1. Convert string to character array
        char[] charArray = s1.toLowerCase().toCharArray();

        // 2. Initialize Two Pointers
        int start = 0;
        int end = charArray.length - 1;
        boolean pal = true;

        // 3. Two-Pointer Technique: Compare start & end characters
        while (start < end) {
            if (charArray[start] != charArray[end]) {
                pal = false; // Mismatch found
                break;       // Exit loop early for efficiency
            }
            start++; // Move forward
            end--;   // Move backward
        }

        // 4. Display the result
        System.out.println("Input Text: " + s1);
        System.out.println("Is it a palindrome? " + pal);

        sc.close();
    }
}