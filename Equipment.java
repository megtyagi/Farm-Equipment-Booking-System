import java.io.Serializable;

public abstract class Equipment implements Serializable {
    private int equipmentId;
    private String name;
    private double rentalRatePerDay;
    private boolean available;

    public Equipment(int equipmentId, String name, double rentalRatePerDay) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.rentalRatePerDay = rentalRatePerDay;
        this.available = true;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public double getRentalRatePerDay() {
        return rentalRatePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateRentalCharge(long numberOfDays);

    public abstract String getEquipmentType();

    @Override
    public String toString() {
        return equipmentId + " | " + getEquipmentType() + " | " + name
                + " | Rs." + String.format("%.2f", rentalRatePerDay) + " per day"
                + " | " + (available ? "Available" : "Unavailable");
    }
}
