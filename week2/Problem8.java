import java.util.*;
import java.time.*;

enum SpotStatus {
    EMPTY,
    OCCUPIED,
    DELETED
}

class ParkingSpot {
    String licensePlate;
    SpotStatus status;
    LocalDateTime entryTime;

    public ParkingSpot() {
        this.status = SpotStatus.EMPTY;
    }
}

public class ParkingLot {

    private int capacity = 500;
    private ParkingSpot[] spots;
    private int totalProbes = 0;
    private int parkedVehicles = 0;

    public ParkingLot() {
        spots = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            spots[i] = new ParkingSpot();
        }
    }

    // Simple hash function: licensePlate -> preferred spot
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String licensePlate) {

        int preferred = hash(licensePlate);
        int probeCount = 0;

        while (probeCount < capacity) {

            int spotIndex = (preferred + probeCount) % capacity;

            if (spots[spotIndex].status == SpotStatus.EMPTY ||
                    spots[spotIndex].status == SpotStatus.DELETED) {

                spots[spotIndex].licensePlate = licensePlate;
                spots[spotIndex].status = SpotStatus.OCCUPIED;
                spots[spotIndex].entryTime = LocalDateTime.now();

                parkedVehicles++;
                totalProbes += probeCount;

                System.out.println("Assigned spot #" + spotIndex +
                        " (" + probeCount + " probes)");

                return;
            }

            probeCount++;
        }

        System.out.println("Parking lot full! Cannot park vehicle.");
    }

    // Exit vehicle and calculate fee
    public void exitVehicle(String licensePlate) {

        int preferred = hash(licensePlate);
        int probeCount = 0;

        while (probeCount < capacity) {

            int spotIndex = (preferred + probeCount) % capacity;

            if (spots[spotIndex].status == SpotStatus.OCCUPIED &&
                    spots[spotIndex].licensePlate.equals(licensePlate)) {

                LocalDateTime entry = spots[spotIndex].entryTime;
                LocalDateTime exit = LocalDateTime.now();

                Duration duration = Duration.between(entry, exit);
                long minutes = duration.toMinutes();
                double hours = minutes / 60.0;

                double fee = calculateFee(hours);

                spots[spotIndex].status = SpotStatus.DELETED;
                spots[spotIndex].licensePlate = null;
                spots[spotIndex].entryTime = null;

                parkedVehicles--;
                System.out.println("Spot #" + spotIndex + " freed, Duration: " +
                        (int)hours + "h " + (minutes % 60) + "m, Fee: $" +
                        String.format("%.2f", fee));

                return;
            }

            probeCount++;
        }

        System.out.println("Vehicle not found in parking lot!");
    }

    // Simple fee calculation: $5 per hour
    private double calculateFee(double hours) {
        return Math.ceil(hours) * 5.0;
    }

    // Get parking statistics
    public void getStatistics() {

        double occupancy = (parkedVehicles * 100.0) / capacity;
        double avgProbes = parkedVehicles == 0 ? 0.0 : (totalProbes * 1.0) / parkedVehicles;

        System.out.println("\n=== Parking Lot Statistics ===");
        System.out.println("Occupancy: " + String.format("%.2f", occupancy) + "%");
        System.out.println("Average Probes: " + String.format("%.2f", avgProbes));
        System.out.println("Capacity: " + capacity);
        System.out.println("Currently Parked Vehicles: " + parkedVehicles);
    }

    public static void main(String[] args) throws InterruptedException {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        Thread.sleep(1000);
        lot.parkVehicle("ABC-1235");
        Thread.sleep(1000);
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking duration

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}
