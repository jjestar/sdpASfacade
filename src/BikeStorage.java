import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BikeStorage {
    private final Map<Integer, String> bikes = new LinkedHashMap<>();
    private final Set<Integer> rentedBikes = new HashSet<>();

    public BikeStorage() {
        bikes.put(101, "city");
        bikes.put(102, "city");
        bikes.put(201, "mountain");
    }

    public int reserveBike(String bikeType) {
        for (Map.Entry<Integer, String> bike : bikes.entrySet()) {
            int bikeId = bike.getKey();
            if (bike.getValue().equals(bikeType) && !rentedBikes.contains(bikeId)) {
                rentedBikes.add(bikeId);
                return bikeId;
            }
        }
        throw new IllegalStateException("No available " + bikeType + " bikes.");
    }

    public void releaseBike(int bikeId) {
        if (!bikes.containsKey(bikeId)) {
            throw new IllegalArgumentException("Unknown bike: " + bikeId);
        }
        if (!rentedBikes.remove(bikeId)) {
            throw new IllegalStateException("This bike is already available.");
        }
    }
}
