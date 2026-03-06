import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class FlashSaleInventory {

    private ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> waitingList = new ConcurrentHashMap<>();

    public FlashSaleInventory() {
        inventory.put("IPHONE15_256GB", new AtomicInteger(100));
        waitingList.put("IPHONE15_256GB", new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        return inventory.get(productId).get();
    }

    public String purchaseItem(String productId, int userId) {

        AtomicInteger stock = inventory.get(productId);

        while (true) {
            int currentStock = stock.get();

            if (currentStock <= 0) {
                waitingList.get(productId).add(userId);
                return "Added to waiting list, position #" + waitingList.get(productId).size();
            }

            if (stock.compareAndSet(currentStock, currentStock - 1)) {
                return "Success, " + (currentStock - 1) + " units remaining";
            }
        }
    }

}