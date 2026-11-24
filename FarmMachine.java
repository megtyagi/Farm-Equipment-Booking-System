public class FarmMachine extends Equipment {
    private String machineType;

    public FarmMachine(int equipmentId, String name, double rentalRatePerDay, String machineType) {
        super(equipmentId, name, rentalRatePerDay);
        this.machineType = machineType;
    }

    public String getMachineType() {
        return machineType;
    }

    @Override
    public double calculateRentalCharge(long numberOfDays) {
        if (numberOfDays <= 0) {
            throw new IllegalArgumentException("Number of days must be at least 1.");
        }
        return getRentalRatePerDay() * numberOfDays;
    }

    @Override
    public String getEquipmentType() {
        return machineType;
    }
}
