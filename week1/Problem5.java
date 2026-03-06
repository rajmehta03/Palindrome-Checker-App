import java.util.*;

class PageEvent {
    String url;
    String userId;
    String source;

    public PageEvent(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class RealTimeAnalytics {

    // page -> total visits
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // page -> unique visitors
    private HashMap<String, HashSet<String>> uniqueVisitors = new HashMap<>();

    // source -> visit count
    private HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public void processEvent(PageEvent event) {

        // Count page views
        pageViews.put(event.url,
                pageViews.getOrDefault(event.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(event.url, new HashSet<>());
        uniqueVisitors.get(event.url).add(event.userId);

        // Track traffic sources
        trafficSources.put(event.source,
                trafficSources.getOrDefault(event.source, 0) + 1);
    }


    // Get Top 10 pages
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : pageViews.entrySet()) {

            pq.offer(entry);

            if (pq.size() > 10)
                pq.poll();
        }

        List<Map.Entry<String, Integer>> result = new ArrayList<>();

        while (!pq.isEmpty())
            result.add(pq.poll());

        Collections.reverse(result);

        return result;
    }


    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME ANALYTICS DASHBOARD =====\n");

        System.out.println("Top Pages:");

        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;

        for (Map.Entry<String, Integer> entry : topPages) {

            String page = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(page).size();

            System.out.println(rank + ". " + page +
                    " - " + views + " views (" +
                    unique + " unique)");

            rank++;
        }


        System.out.println("\nTraffic Sources:");

        int total = trafficSources.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        for (String source : trafficSources.keySet()) {

            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.println(source + ": " +
                    String.format("%.1f", percent) + "%");
        }
    }


    public static void main(String[] args) throws InterruptedException {

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        // Simulated streaming events
        analytics.processEvent(new PageEvent("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new PageEvent("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_123", "direct"));
        analytics.processEvent(new PageEvent("/sports/championship", "user_789", "google"));
        analytics.processEvent(new PageEvent("/tech/ai-future", "user_333", "google"));
        analytics.processEvent(new PageEvent("/tech/ai-future", "user_444", "direct"));
        analytics.processEvent(new PageEvent("/tech/ai-future", "user_333", "google"));

        // Simulate dashboard refresh every 5 seconds
        while (true) {

            analytics.getDashboard();

            Thread.sleep(5000);
        }
    }
}