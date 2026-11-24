import java.time.LocalDate;
import java.util.ArrayList;

public class BookingManager {
    private ArrayList<Equipment> equipmentList = new ArrayList<>();
    private ArrayList<Farmer> farmerList = new ArrayList<>();
    private ArrayList<Booking> bookingList = new ArrayList<>();

    private int nextEquipmentId = 1;
    private int nextFarmerId = 1;
    private int nextBookingId = 1;

    public void addEquipment(Equipment equipment) {
        for (Equipment item : equipmentList) {
            if (item.getEquipmentId() == equipment.getEquipmentId()) {
                throw new IllegalArgumentException("Equipment ID already exists.");
            }
        }
        equipmentList.add(equipment);
        nextEquipmentId = Math.max(nextEquipmentId, equipment.getEquipmentId() + 1);
    }

    public void registerFarmer(Farmer farmer) {
        for (Farmer item : farmerList) {
            if (item.getFarmerId() == farmer.getFarmerId()) {
                throw new IllegalArgumentException("Farmer ID already exists.");
            }
        }
        farmerList.add(farmer);
        nextFarmerId = Math.max(nextFarmerId, farmer.getFarmerId() + 1);
    }

    public Booking bookEquipment(Farmer farmer, Equipment equipment,
                                 LocalDate startDate, LocalDate endDate) {
        if (!farmerList.contains(farmer)) {
            throw new IllegalArgumentException("Farmer is not registered.");
        }
        if (!equipmentList.contains(equipment)) {
            throw new IllegalArgumentException("Equipment is not in the system.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Booking dates are required.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }

        for (Booking booking : bookingList) {
            if (!booking.isCancelled()
                    && booking.getEquipment().getEquipmentId() == equipment.getEquipmentId()
                    && booking.overlaps(startDate, endDate)) {
                throw new IllegalArgumentException(
                        "This equipment is already booked for that date range.");
            }
        }

        Booking booking = new Booking(nextBookingId++, farmer, equipment, startDate, endDate);
        bookingList.add(booking);
        updateAvailability();
        return booking;
    }

    public boolean cancelBooking(int bookingId) {
        for (Booking booking : bookingList) {
            if (booking.getBookingId() == bookingId) {
                if (booking.isCancelled()) {
                    return false;
                }
                booking.cancel();
                updateAvailability();
                return true;
            }
        }
        return false;
    }

    public boolean isDateAvailable(Equipment equipment, LocalDate startDate, LocalDate endDate) {
        if (equipment == null || startDate == null || endDate == null) {
            return false;
        }
        if (endDate.isBefore(startDate) || startDate.isBefore(LocalDate.now())) {
            return false;
        }
        for (Booking booking : bookingList) {
            if (!booking.isCancelled()
                    && booking.getEquipment().getEquipmentId() == equipment.getEquipmentId()
                    && booking.overlaps(startDate, endDate)) {
                return false;
            }
        }
        return true;
    }

    public void updateAvailability() {
        LocalDate today = LocalDate.now();
        for (Equipment equipment : equipmentList) {
            boolean activelyBooked = false;
            for (Booking booking : bookingList) {
                if (!booking.isCancelled()
                        && booking.getEquipment().getEquipmentId() == equipment.getEquipmentId()
                        && !today.isBefore(booking.getStartDate())
                        && !today.isAfter(booking.getEndDate())) {
                    activelyBooked = true;
                    break;
                }
            }
            equipment.setAvailable(!activelyBooked);
        }
    }

    public ArrayList<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public ArrayList<Farmer> getFarmerList() {
        return farmerList;
    }

    public ArrayList<Booking> getBookingList() {
        return bookingList;
    }

    public Equipment findEquipmentById(int id) {
        for (Equipment equipment : equipmentList) {
            if (equipment.getEquipmentId() == id) {
                return equipment;
            }
        }
        return null;
    }

    public Farmer findFarmerById(int id) {
        for (Farmer farmer : farmerList) {
            if (farmer.getFarmerId() == id) {
                return farmer;
            }
        }
        return null;
    }

    public int getNextEquipmentId() {
        return nextEquipmentId;
    }

    public int getNextFarmerId() {
        return nextFarmerId;
    }

    public void setEquipmentList(ArrayList<Equipment> equipmentList) {
        this.equipmentList = equipmentList;
        int maxId = 0;
        for (Equipment equipment : equipmentList) {
            maxId = Math.max(maxId, equipment.getEquipmentId());
        }
        nextEquipmentId = maxId + 1;
    }

    public void setFarmerList(ArrayList<Farmer> farmerList) {
        this.farmerList = farmerList;
        int maxId = 0;
        for (Farmer farmer : farmerList) {
            maxId = Math.max(maxId, farmer.getFarmerId());
        }
        nextFarmerId = maxId + 1;
    }

    public void setBookingList(ArrayList<Booking> bookingList) {
        this.bookingList = bookingList;
        int maxId = 0;
        for (Booking booking : bookingList) {
            maxId = Math.max(maxId, booking.getBookingId());
        }
        nextBookingId = maxId + 1;
        updateAvailability();
    }
}
