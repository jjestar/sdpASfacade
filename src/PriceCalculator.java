public class PriceCalculator {
    private static final int CITY_PRICE_PER_HOUR = 500;
    private static final int MOUNTAIN_PRICE_PER_HOUR = 800;
    private static final int MAX_HOURS = 24;

    public int calculatePrice(String bikeType, int hours) {
        if (hours < 1 || hours > MAX_HOURS) {
            throw new IllegalArgumentException("Rental time must be from 1 to 24 hours.");
        }

        if ("city".equals(bikeType)) {
            return CITY_PRICE_PER_HOUR * hours;
        }
        if ("mountain".equals(bikeType)) {
            return MOUNTAIN_PRICE_PER_HOUR * hours;
        }
        throw new IllegalArgumentException("Choose city or mountain.");
    }
}
