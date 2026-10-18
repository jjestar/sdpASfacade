import java.util.HashMap;
import java.util.Map;

public class RentalRegistry {
    private final Map<Integer, Rental> rentals = new HashMap<>();
    private int nextId = 1;

    public int createRental(String studentId, int bikeId, String bikeType,
                            int hours, int price) {
        Rental rental = new Rental(nextId, studentId, bikeId, bikeType, hours, price);
        rentals.put(nextId, rental);
        return nextId++;
    }

    public Rental getRental(int rentalId) {
        Rental rental = rentals.get(rentalId);
        if (rental == null) {
            throw new IllegalArgumentException("Unknown rental: " + rentalId);
        }
        return rental;
    }

    public void finishRental(int rentalId) {
        getRental(rentalId).markReturned();
    }
}
