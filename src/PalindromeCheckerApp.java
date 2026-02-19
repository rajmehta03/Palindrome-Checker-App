public class PalindromeCheckerApp {
    public static void main(String[] args){
        System.out.println("Jai Shree Ram , Welcome to Palindrome Checker Management App\n" +
                "Version : 1.0"+"\nSystem initialized successfully");
        String str = "Section";int n = str.length();
        for(int i = 0; i<n/2; i++){
            if(str.charAt(i)!= str.charAt(n-1-i)){
                System.out.println("Not a Palindrome");
                return;


            }
        }

    }
}
