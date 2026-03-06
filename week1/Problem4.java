import java.util.*;

class PlagiarismDetector {

    // n-gram size
    private int N = 5;

    // ngram -> set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    // document -> total ngrams
    private HashMap<String, Integer> docGramCount = new HashMap<>();


    // Generate n-grams from text
    private List<String> generateNGrams(String text) {

        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }


    // Add document to index
    public void addDocument(String docId, String text) {

        List<String> grams = generateNGrams(text);

        docGramCount.put(docId, grams.size());

        for (String gram : grams) {

            index.putIfAbsent(gram, new HashSet<>());
            index.get(gram).add(docId);
        }

        System.out.println("Indexed document: " + docId +
                " (" + grams.size() + " n-grams)");
    }


    // Analyze a new document
    public void analyzeDocument(String docId, String text) {

        List<String> grams = generateNGrams(text);

        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {

            if (index.containsKey(gram)) {

                for (String otherDoc : index.get(gram)) {

                    if (!otherDoc.equals(docId)) {

                        matchCount.put(
                                otherDoc,
                                matchCount.getOrDefault(otherDoc, 0) + 1
                        );
                    }
                }
            }
        }

        System.out.println("\nAnalyzing document: " + docId);
        System.out.println("Extracted " + grams.size() + " n-grams\n");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            int total = grams.size();

            double similarity = (matches * 100.0) / total;

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.println("Similarity: " +
                    String.format("%.2f", similarity) + "%");

            if (similarity > 60) {
                System.out.println("⚠ PLAGIARISM DETECTED\n");
            } else if (similarity > 15) {
                System.out.println("Suspicious similarity\n");
            } else {
                System.out.println("Low similarity\n");
            }
        }
    }


    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 =
                "Artificial intelligence is transforming the world of technology. "
                        + "Machine learning and deep learning are subsets of artificial intelligence.";

        String essay2 =
                "Artificial intelligence is transforming technology and many industries. "
                        + "Machine learning is an important part of artificial intelligence.";

        String essay3 =
                "The history of computers started with mechanical machines. "
                        + "Modern computers use advanced processors and memory systems.";


        detector.addDocument("essay_001", essay1);
        detector.addDocument("essay_002", essay2);
        detector.addDocument("essay_003", essay3);


        String newEssay =
                "Artificial intelligence is transforming the world of technology. "
                        + "Machine learning is a subset of artificial intelligence.";

        detector.analyzeDocument("essay_004", newEssay);
    }
}