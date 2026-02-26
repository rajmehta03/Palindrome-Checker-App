import java.util.Scanner;

public class PalindromeCheckerApp {
    public static void main(String[] args) {
        System.out.println("Welcome to the Palindrome Checker Management system");
        System.out.println("Version : 1.0");
        System.out.println("System initialized successfully");

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Text:");
        String s1 = sc.nextLine();

        // 1. Create an empty string for the reverse
        String reversed = "";
        int length = s1.length();

        // 2. Loop backwards to build the reversed string
        for (int i = length - 1; i >= 0; i--) {
            reversed += s1.charAt(i);
        }

        // 3. Compare original and reversed (ignoring case)
        boolean pal = s1.equalsIgnoreCase(reversed);

        // 4. Display the result
        System.out.println("Original: " + s1);
        System.out.println("Reversed: " + reversed);
        System.out.println("Is it a palindrome? " + pal);

        sc.close();
    }
}