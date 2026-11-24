import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking implements Serializable {
    private int bookingId;
    private Farmer farmer;
    private Equipment equipment;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCharge;
    private boolean cancelled;

    public Booking(int bookingId, Farmer farmer, Equipment equipment,
                   LocalDate startDate, LocalDate endDate) {
        if (farmer == null) {
            throw new IllegalArgumentException("Farmer is required.");
        }
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment is required.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Booking dates are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.bookingId = bookingId;
        this.farmer = farmer;
        this.equipment = equipment;
        this.startDate = startDate;
        this.endDate = endDate;
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        this.totalCharge = equipment.calculateRentalCharge(days);
        this.cancelled = false;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        return !otherEnd.isBefore(startDate) && !otherStart.isAfter(endDate);
    }

    @Override
    public String toString() {
        return bookingId + " | Farmer: " + farmer.getName()
                + " | Equipment: " + equipment.getName()
                + " | " + startDate + " to " + endDate
                + " | Charge: Rs." + String.format("%.2f", totalCharge)
                + " | " + (cancelled ? "Cancelled" : "Active");
    }
}
