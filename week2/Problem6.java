import java.util.concurrent.ConcurrentHashMap;

class TokenBucket {

    private int maxTokens;
    private double refillRate; // tokens per second
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    // Refill tokens based on elapsed time
    private void refill() {

        long now = System.currentTimeMillis();
        double seconds = (now - lastRefillTime) / 1000.0;

        double tokensToAdd = seconds * refillRate;

        tokens = Math.min(maxTokens, tokens + tokensToAdd);

        lastRefillTime = now;
    }

    // Attempt to consume token
    public synchronized boolean allowRequest() {

        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }

        return false;
    }

    public synchronized int getRemainingTokens() {
        refill();
        return (int) tokens;
    }

    public int getMaxTokens() {
        return maxTokens;
    }
}

public class RateLimiter {

    // clientId -> token bucket
    private ConcurrentHashMap<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();

    private int limit = 1000;
    private double refillRate = 1000.0 / 3600.0; // tokens per second


    public boolean checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(limit, refillRate));

        TokenBucket bucket = clientBuckets.get(clientId);

        boolean allowed = bucket.allowRequest();

        if (allowed) {

            System.out.println(
                    "Allowed (" +
                            bucket.getRemainingTokens() +
                            " requests remaining)"
            );

        } else {

            System.out.println(
                    "Denied (0 requests remaining)"
            );
        }

        return allowed;
    }


    public void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) {
            System.out.println("Client not found");
            return;
        }

        int used = bucket.getMaxTokens() - bucket.getRemainingTokens();

        System.out.println(
                "{used: " + used +
                        ", limit: " + bucket.getMaxTokens() +
                        ", remaining: " + bucket.getRemainingTokens() + "}"
        );
    }


    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        // simulate requests
        for (int i = 0; i < 5; i++) {
            limiter.checkRateLimit(client);
        }

        limiter.getRateLimitStatus(client);
    }
}