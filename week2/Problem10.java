import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// Simulated Video Data
class VideoData {
    String videoId;
    String content;

    public VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

// Multi-level cache system
public class MultiLevelCache {

    private static final int L1_CAPACITY = 10000;
    private static final int L2_CAPACITY = 100000;

    // L1 Cache: in-memory LRU
    private LinkedHashMap<String, VideoData> l1Cache;

    // L2 Cache: SSD-backed simulation (videoId -> FilePath)
    private LinkedHashMap<String, VideoData> l2Cache;

    // Access count for promotions
    private HashMap<String, AtomicInteger> accessCountMap;

    // Simulated L3 database
    private HashMap<String, VideoData> database;

    // Stats
    private int l1Hits = 0, l2Hits = 0, l3Hits = 0;
    private int l1Requests = 0, l2Requests = 0, l3Requests = 0;
    private double l1Time = 0.0, l2Time = 0.0, l3Time = 0.0;

    private static final int PROMOTION_THRESHOLD = 3;

    public MultiLevelCache() {

        l1Cache = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        l2Cache = new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_CAPACITY;
            }
        };

        accessCountMap = new HashMap<>();
        database = new HashMap<>();

        // Populate database with 1 million videos for simulation
        for (int i = 1; i <= 1000000; i++) {
            database.put("video_" + i, new VideoData("video_" + i, "Content of video " + i));
        }
    }

    // Get video from multi-level cache
    public VideoData getVideo(String videoId) {

        // L1 Cache
        l1Requests++;
        if (l1Cache.containsKey(videoId)) {
            l1Hits++;
            l1Time += 0.5;
            return l1Cache.get(videoId);
        }

        // L2 Cache
        l2Requests++;
        if (l2Cache.containsKey(videoId)) {
            l2Hits++;
            l2Time += 5.0;

            // Increment access count
            accessCountMap.putIfAbsent(videoId, new AtomicInteger(0));
            int count = accessCountMap.get(videoId).incrementAndGet();

            // Promote to L1 if threshold reached
            if (count >= PROMOTION_THRESHOLD) {
                l1Cache.put(videoId, l2Cache.get(videoId));
            }

            return l2Cache.get(videoId);
        }

        // L3 Database
        l3Requests++;
        if (database.containsKey(videoId)) {
            l3Hits++;
            l3Time += 150.0;

            // Add to L2 for future accesses
            l2Cache.put(videoId, database.get(videoId));
            accessCountMap.put(videoId, new AtomicInteger(1));

            return database.get(videoId);
        }

        return null; // video not found
    }

    // Invalidate video in cache
    public void invalidateVideo(String videoId) {
        l1Cache.remove(videoId);
        l2Cache.remove(videoId);
        accessCountMap.remove(videoId);
    }

    // Print cache statistics
    public void getStatistics() {

        double l1HitRate = l1Requests == 0 ? 0 : (l1Hits * 100.0 / l1Requests);
        double l2HitRate = l2Requests == 0 ? 0 : (l2Hits * 100.0 / l2Requests);
        double l3HitRate = l3Requests == 0 ? 0 : (l3Hits * 100.0 / l3Requests);

        double l1Avg = l1Requests == 0 ? 0 : l1Time / l1Requests;
        double l2Avg = l2Requests == 0 ? 0 : l2Time / l2Requests;
        double l3Avg = l3Requests == 0 ? 0 : l3Time / l3Requests;

        double overallHitRate = (l1Hits + l2Hits + l3Hits) * 100.0 / (l1Requests + l2Requests + l3Requests);
        double overallAvgTime = (l1Time + l2Time + l3Time) / (l1Requests + l2Requests + l3Requests);

        System.out.println("\n=== Cache Statistics ===");
        System.out.printf("L1: Hit Rate %.2f%%, Avg Time %.2fms\n", l1HitRate, l1Avg);
        System.out.printf("L2: Hit Rate %.2f%%, Avg Time %.2fms\n", l2HitRate, l2Avg);
        System.out.printf("L3: Hit Rate %.2f%%, Avg Time %.2fms\n", l3HitRate, l3Avg);
        System.out.printf("Overall: Hit Rate %.2f%%, Avg Time %.2fms\n", overallHitRate, overallAvgTime);
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // Access some videos
        cache.getVideo("video_123"); // L1 MISS, L2 MISS, L3 HIT → added to L2
        cache.getVideo("video_123"); // L1 MISS, L2 HIT → promote if access count ≥ threshold
        cache.getVideo("video_123"); // L1 HIT after promotion

        cache.getVideo("video_999"); // L3 HIT → added to L2

        // Access another video to simulate traffic
        cache.getVideo("video_456"); // L3 HIT
        cache.getVideo("video_789"); // L3 HIT
        cache.getVideo("video_123"); // L1 HIT

        cache.getStatistics();
    }
}