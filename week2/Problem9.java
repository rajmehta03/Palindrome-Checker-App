import java.util.*;
import java.time.*;

class Transaction {
    int id;
    double amount;
    String merchant;
    String account;
    LocalDateTime time;

    public Transaction(int id, double amount, String merchant, String account, LocalDateTime time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

public class TransactionAnalyzer {

    List<Transaction> transactions;

    public TransactionAnalyzer(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Classic Two-Sum
    public List<int[]> findTwoSum(double target) {

        List<int[]> result = new ArrayList<>();
        Map<Double, Transaction> map = new HashMap<>();

        for (Transaction tx : transactions) {

            double complement = target - tx.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, tx.id});
            }

            map.put(tx.amount, tx);
        }

        return result;
    }

    // Two-Sum with Time Window (minutes)
    public List<int[]> findTwoSumTimeWindow(double target, int minutesWindow) {

        List<int[]> result = new ArrayList<>();
        transactions.sort(Comparator.comparing(tx -> tx.time));

        Map<Double, List<Transaction>> map = new HashMap<>();

        for (Transaction tx : transactions) {

            double complement = target - tx.amount;

            if (map.containsKey(complement)) {
                for (Transaction t : map.get(complement)) {
                    if (Math.abs(Duration.between(t.time, tx.time).toMinutes()) <= minutesWindow) {
                        result.add(new int[]{t.id, tx.id});
                    }
                }
            }

            map.putIfAbsent(tx.amount, new ArrayList<>());
            map.get(tx.amount).add(tx);
        }

        return result;
    }

    // K-Sum (recursive)
    public List<List<Integer>> findKSum(int k, double target) {
        List<List<Integer>> result = new ArrayList<>();
        transactions.sort(Comparator.comparingDouble(tx -> tx.amount));
        kSumHelper(0, k, target, new ArrayList<>(), result);
        return result;
    }

    private void kSumHelper(int start, int k, double target, List<Integer> path, List<List<Integer>> result) {

        if (k == 2) {
            int left = start;
            int right = transactions.size() - 1;
            while (left < right) {
                double sum = transactions.get(left).amount + transactions.get(right).amount;
                if (Math.abs(sum - target) < 1e-6) {
                    List<Integer> newPath = new ArrayList<>(path);
                    newPath.add(transactions.get(left).id);
                    newPath.add(transactions.get(right).id);
                    result.add(newPath);
                    left++;
                    right--;
                } else if (sum < target) left++;
                else right--;
            }
            return;
        }

        for (int i = start; i < transactions.size() - k + 1; i++) {
            path.add(transactions.get(i).id);
            kSumHelper(i + 1, k - 1, target - transactions.get(i).amount, path, result);
            path.remove(path.size() - 1);
        }
    }

    // Detect duplicates: same amount & merchant, different accounts
    public Map<String, Set<String>> detectDuplicates() {

        Map<String, Set<String>> map = new HashMap<>();

        for (Transaction tx : transactions) {

            String key = tx.amount + "-" + tx.merchant;

            map.putIfAbsent(key, new HashSet<>());
            map.get(key).add(tx.account);
        }

        Map<String, Set<String>> duplicates = new HashMap<>();
        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                duplicates.put(key, map.get(key));
            }
        }

        return duplicates;
    }

    public static void main(String[] args) {

        List<Transaction> txs = new ArrayList<>();

        txs.add(new Transaction(1, 500, "Store A", "acc1", LocalDateTime.of(2026,3,6,10,0)));
        txs.add(new Transaction(2, 300, "Store B", "acc2", LocalDateTime.of(2026,3,6,10,15)));
        txs.add(new Transaction(3, 200, "Store C", "acc3", LocalDateTime.of(2026,3,6,10,30)));
        txs.add(new Transaction(4, 500, "Store A", "acc2", LocalDateTime.of(2026,3,6,11,0)));

        TransactionAnalyzer analyzer = new TransactionAnalyzer(txs);

        System.out.println("Two-Sum (target=500):");
        for (int[] pair : analyzer.findTwoSum(500)) {
            System.out.println(Arrays.toString(pair));
        }

        System.out.println("\nTwo-Sum with 60-minute window (target=500):");
        for (int[] pair : analyzer.findTwoSumTimeWindow(500, 60)) {
            System.out.println(Arrays.toString(pair));
        }

        System.out.println("\nK-Sum (k=3, target=1000):");
        for (List<Integer> ksum : analyzer.findKSum(3, 1000)) {
            System.out.println(ksum);
        }

        System.out.println("\nDuplicate Transactions:");
        Map<String, Set<String>> duplicates = analyzer.detectDuplicates();
        for (String key : duplicates.keySet()) {
            System.out.println(key + " -> Accounts: " + duplicates.get(key));
        }
    }
}