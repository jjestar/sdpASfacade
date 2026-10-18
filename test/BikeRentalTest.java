public class BikeRentalTest {
    public static void main(String[] args) {
        testCityRental();
        testMountainPrice();
        testInsufficientFunds();
        testUnavailableBikes();
        testReturnAndRepeatedReturn();
        testInvalidRequests();
        testUnknownRentalIds();
        System.out.println("All 7 tests passed.");
    }

    private static void testCityRental() {
        BikeRentalFacade shop = new BikeRentalFacade();
        int rentalId = shop.rentBike("s1", "city", 2);

        check(shop.getBalance("s1") == 4000, "City rental should cost 1000 KZT");
        String expected = "Rental #" + rentalId
                + " | Student: s1 | Bike: city | Hours: 2 | Price: 1000 KZT | Status: ACTIVE";
        check(shop.getRentalInfo(rentalId).equals(expected), "Rental information is incorrect");
    }

    private static void testMountainPrice() {
        BikeRentalFacade shop = new BikeRentalFacade();
        int rentalId = shop.rentBike("s1", "mountain", 3);

        check(shop.getBalance("s1") == 2600, "Mountain rental should cost 2400 KZT");
        check(shop.getRentalInfo(rentalId).contains("Price: 2400 KZT"), "Wrong mountain price");
    }

    private static void testInsufficientFunds() {
        BikeRentalFacade shop = new BikeRentalFacade();
        expectError(IllegalStateException.class,
                () -> shop.rentBike("s2", "city", 1));

        check(shop.getBalance("s2") == 300, "Failed payment must not change the balance");
        int firstId = shop.rentBike("s1", "city", 1);
        int secondId = shop.rentBike("s1", "city", 1);
        check(firstId != secondId, "Each rental must have a different ID");
        check(shop.getBalance("s1") == 4000, "Both city bikes must remain available after failure");
    }

    private static void testUnavailableBikes() {
        BikeRentalFacade shop = new BikeRentalFacade();
        shop.rentBike("s1", "city", 1);
        shop.rentBike("s1", "city", 1);
        expectError(IllegalStateException.class,
                () -> shop.rentBike("s1", "city", 1));

        check(shop.getBalance("s1") == 4000, "An unavailable bike must not cause a charge");
        shop.rentBike("s1", "mountain", 1);
        check(shop.getBalance("s1") == 3200, "A different bike type should still be available");
    }

    private static void testReturnAndRepeatedReturn() {
        BikeRentalFacade shop = new BikeRentalFacade();
        int oldRental = shop.rentBike("s1", "mountain", 1);
        shop.returnBike(oldRental);

        check(shop.getBalance("s1") == 4200, "Returning a bike must not refund the prepaid price");
        check(shop.getRentalInfo(oldRental).endsWith("Status: RETURNED"), "Return status is incorrect");

        int newRental = shop.rentBike("s1", "mountain", 1);
        expectError(IllegalStateException.class, () -> shop.returnBike(oldRental));
        expectError(IllegalStateException.class,
                () -> shop.rentBike("s1", "mountain", 1));
        check(shop.getBalance("s1") == 3400, "Repeated return must not change the balance");
        check(shop.getRentalInfo(newRental).endsWith("Status: ACTIVE"), "The new rental must stay active");

        shop.returnBike(newRental);
        shop.rentBike("s1", "mountain", 1);
        check(shop.getBalance("s1") == 2600, "The current renter must still be able to return the bike");
    }

    private static void testInvalidRequests() {
        BikeRentalFacade shop = new BikeRentalFacade();
        expectError(IllegalArgumentException.class, () -> shop.rentBike("missing", "city", 1));
        expectError(IllegalArgumentException.class, () -> shop.rentBike(null, "city", 1));
        expectError(IllegalArgumentException.class, () -> shop.rentBike("s1", "CITY", 1));
        expectError(IllegalArgumentException.class, () -> shop.rentBike("s1", null, 1));
        expectError(IllegalArgumentException.class, () -> shop.rentBike("s1", "city", 0));
        expectError(IllegalArgumentException.class, () -> shop.rentBike("s1", "city", 25));
        expectError(IllegalArgumentException.class, () -> shop.getBalance("missing"));

        // 24 hours is valid, but this account cannot afford it.
        expectError(IllegalStateException.class, () -> shop.rentBike("s1", "city", 24));
        check(shop.getBalance("s1") == 5000, "Rejected requests must not change the balance");
        shop.rentBike("s1", "city", 1);
        shop.rentBike("s1", "city", 1);
        check(shop.getBalance("s1") == 4000, "Rejected requests must not reserve bikes");
    }

    private static void testUnknownRentalIds() {
        BikeRentalFacade shop = new BikeRentalFacade();
        expectError(IllegalArgumentException.class, () -> shop.getRentalInfo(999));
        expectError(IllegalArgumentException.class, () -> shop.returnBike(999));
        check(shop.getBalance("s1") == 5000, "An unknown rental must not change the balance");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectError(Class<? extends RuntimeException> expected, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException error) {
            if (expected.isInstance(error)) {
                return;
            }
            throw new AssertionError("Expected " + expected.getSimpleName()
                    + ", but got " + error.getClass().getSimpleName(), error);
        }
        throw new AssertionError("Expected " + expected.getSimpleName() + ", but no error occurred");
    }
}
