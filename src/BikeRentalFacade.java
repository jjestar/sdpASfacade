public class BikeRentalFacade {
    private final BikeStorage bikes = new BikeStorage();
    private final PriceCalculator prices = new PriceCalculator();
    private final PaymentService payments = new PaymentService();
    private final RentalRegistry rentals = new RentalRegistry();

    public int rentBike(String studentId, String bikeType, int hours) {
        payments.getBalance(studentId);
        int price = prices.calculatePrice(bikeType, hours);
        int bikeId = bikes.reserveBike(bikeType);
        boolean paid = false;

        try {
            payments.charge(studentId, price);
            paid = true;
            return rentals.createRental(studentId, bikeId, bikeType, hours, price);
        } catch (RuntimeException exception) {
            // A failed rental must not leave the bike reserved or money charged.
            if (paid) {
                payments.refund(studentId, price);
            }
            bikes.releaseBike(bikeId);
            throw exception;
        }
    }

    public void returnBike(int rentalId) {
        Rental rental = rentals.getRental(rentalId);
        if (!rental.isActive()) {
            throw new IllegalStateException("This rental is already returned.");
        }
        bikes.releaseBike(rental.getBikeId());
        rentals.finishRental(rentalId);
    }

    public String getRentalInfo(int rentalId) {
        Rental rental = rentals.getRental(rentalId);
        String status = rental.isActive() ? "ACTIVE" : "RETURNED";
        return "Rental #" + rental.getId()
                + " | Student: " + rental.getStudentId()
                + " | Bike: " + rental.getBikeType()
                + " | Hours: " + rental.getHours()
                + " | Price: " + rental.getPrice() + " KZT"
                + " | Status: " + status;
    }

    public int getBalance(String studentId) {
        return payments.getBalance(studentId);
    }
}
