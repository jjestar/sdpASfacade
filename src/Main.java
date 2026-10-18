public class Main {
    public static void main(String[] args) {
        BikeRentalFacade rentalService = new BikeRentalFacade();

        System.out.println("Starting balance: " + rentalService.getBalance("s1") + " KZT");
        int rentalId = rentalService.rentBike("s1", "city", 2);
        System.out.println(rentalService.getRentalInfo(rentalId));
        System.out.println("Balance after rental: " + rentalService.getBalance("s1") + " KZT");

        System.out.println("\nTrying to rent with too little money:");
        try {
            rentalService.rentBike("s2", "city", 1);
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
        }

        int secondRental = rentalService.rentBike("s1", "city", 1);
        System.out.println("Another rental after the failed payment:");
        System.out.println(rentalService.getRentalInfo(secondRental));

        System.out.println("\nTrying to rent when both city bikes are taken:");
        try {
            rentalService.rentBike("s1", "city", 1);
        } catch (IllegalStateException exception) {
            System.out.println(exception.getMessage());
        }

        rentalService.returnBike(rentalId);
        System.out.println("\nAfter returning the first bike:");
        System.out.println(rentalService.getRentalInfo(rentalId));
        System.out.println("Final balance: " + rentalService.getBalance("s1") + " KZT");
    }
}
