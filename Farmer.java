import java.io.Serializable;

public class Farmer implements Serializable {
    private int farmerId;
    private String name;
    private String phoneNumber;

    public Farmer(int farmerId, String name, String phoneNumber) {
        this.farmerId = farmerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return farmerId + " | " + name + " | " + phoneNumber;
    }
}
