public class Rental {
    private final int id;
    private final String studentId;
    private final int bikeId;
    private final String bikeType;
    private final int hours;
    private final int price;
    private boolean active = true;

    public Rental(int id, String studentId, int bikeId,
                  String bikeType, int hours, int price) {
        if (id <= 0 || bikeId <= 0 || hours <= 0 || price <= 0) {
            throw new IllegalArgumentException("Rental values must be positive.");
        }
        if (studentId == null || studentId.trim().isEmpty()
                || bikeType == null || bikeType.trim().isEmpty()) {
            throw new IllegalArgumentException("Student and bike type are required.");
        }
        this.id = id;
        this.studentId = studentId;
        this.bikeId = bikeId;
        this.bikeType = bikeType;
        this.hours = hours;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public int getHours() {
        return hours;
    }

    public int getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    void markReturned() {
        if (!active) {
            throw new IllegalStateException("This rental is already returned.");
        }
        active = false;
    }
}
