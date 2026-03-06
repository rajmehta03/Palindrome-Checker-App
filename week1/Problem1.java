import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class UsernameChecker {

    private ConcurrentHashMap<String, Integer> users = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();

    private String mostAttempted = "";
    private int maxAttempts = 0;

    public boolean checkAvailability(String username) {

        int count = attempts.getOrDefault(username, 0) + 1;
        attempts.put(username, count);

        if (count > maxAttempts) {
            maxAttempts = count;
            mostAttempted = username;
        }

        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String candidate = username + i;
            if (!users.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        String alt = username.replace("_", ".");
        if (!users.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }

    public String getMostAttempted() {
        return mostAttempted;
    }

    public void registerUser(String username, int userId) {
        users.put(username, userId);
    }
}
