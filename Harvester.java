public class Harvester extends FarmMachine {
    public Harvester(int equipmentId, String name, double rentalRatePerDay) {
        super(equipmentId, name, rentalRatePerDay, "Harvester");
    }

    @Override
    public double calculateRentalCharge(long numberOfDays) {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be at least 1.");
        }
        double charge = getRentalRatePerDay() * numberOfDays;
        if (numberOfDays >= 7) {
            charge = charge * 0.90;
        }
        return charge;
    }
}
