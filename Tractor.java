public class Tractor extends FarmMachine {
    public Tractor(int equipmentId, String name, double rentalRatePerDay) {
        super(equipmentId, name, rentalRatePerDay, "Tractor");
    }

    @Override
    public double calculateRentalCharge(long numberOfDays) {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be at least 1.");
        }
        return getRentalRatePerDay() * numberOfDays;
    }
}
