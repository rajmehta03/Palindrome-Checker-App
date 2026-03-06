import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> queries = new ArrayList<>();
    boolean isEnd = false;
}

public class AutocompleteSystem {

    private TrieNode root = new TrieNode();

    // query -> frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();

    private static final int TOP_K = 10;


    // Insert query into Trie
    public void insert(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            node.queries.add(query);
        }

        node.isEnd = true;

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);
    }


    // Update search frequency
    public void updateFrequency(String query) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);

        insert(query);
    }


    // Get autocomplete suggestions
    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        PriorityQueue<String> minHeap =
                new PriorityQueue<>((a, b) ->
                        frequencyMap.get(a) - frequencyMap.get(b));

        for (String query : node.queries) {

            minHeap.offer(query);

            if (minHeap.size() > TOP_K)
                minHeap.poll();
        }

        List<String> result = new ArrayList<>();

        while (!minHeap.isEmpty())
            result.add(minHeap.poll());

        Collections.reverse(result);

        return result;
    }


    public void printSuggestions(String prefix) {

        List<String> suggestions = search(prefix);

        System.out.println("\nSuggestions for \"" + prefix + "\":");

        int rank = 1;

        for (String s : suggestions) {

            System.out.println(rank + ". " + s +
                    " (" + frequencyMap.get(s) + " searches)");

            rank++;
        }
    }


    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java tutorial");
        system.insert("java 21 features");
        system.insert("java interview questions");
        system.insert("java vs python");

        system.printSuggestions("jav");

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");

        system.printSuggestions("jav");
    }
}