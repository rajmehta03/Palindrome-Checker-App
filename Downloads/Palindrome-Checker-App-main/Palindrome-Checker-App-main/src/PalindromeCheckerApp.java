import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.Stack;

public class PalindromeCheckerApp {

    public static boolean stackMethod(String input) {

        String normalized = input.replaceAll("\\s+", "").toLowerCase();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < normalized.length(); i++) {
            stack.push(normalized.charAt(i));
        }

        for (int i = 0; i < normalized.length(); i++) {
            if (normalized.charAt(i) != stack.pop()) {
                return false;
            }
        }

        return true;
    }

    public static boolean dequeMethod(String input) {

        String normalized = input.replaceAll("\\s+", "").toLowerCase();
        Deque<Character> deque = new ArrayDeque<>();

        for (int i = 0; i < normalized.length(); i++) {
            deque.addLast(normalized.charAt(i));
        }

        while (deque.size() > 1) {
            if (!deque.removeFirst().equals(deque.removeLast())) {
                return false;
            }
        }

        return true;
    }

    public static boolean twoPointerMethod(String input) {

        String normalized = input.replaceAll("\\s+", "").toLowerCase();
        int start = 0;
        int end = normalized.length() - 1;

        while (start < end) {
            if (normalized.charAt(start) != normalized.charAt(end)) {
                return false;
            }
            start++;
            end--;
        }

        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String input = scanner.nextLine();

        long startTime1 = System.nanoTime();
        boolean result1 = stackMethod(input);
        long endTime1 = System.nanoTime();

        long startTime2 = System.nanoTime();
        boolean result2 = dequeMethod(input);
        long endTime2 = System.nanoTime();

        long startTime3 = System.nanoTime();
        boolean result3 = twoPointerMethod(input);
        long endTime3 = System.nanoTime();

        System.out.println("\nResults:");
        System.out.println("Stack Method: " + result1 + " | Time: " + (endTime1 - startTime1) + " ns");
        System.out.println("Deque Method: " + result2 + " | Time: " + (endTime2 - startTime2) + " ns");
        System.out.println("Two Pointer Method: " + result3 + " | Time: " + (endTime3 - startTime3) + " ns");

        scanner.close();
    }
}