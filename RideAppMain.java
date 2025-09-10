package sample1;

	import java.util.*;

	// ================= USER (Base Class) =================
	class User {
	    protected int id;
	    protected String name;
	    protected String phone;

	    public User(int id, String name, String phone) {
	        this.id = id;
	        this.name = name;
	        this.phone = phone;
	    }

	    public int getId() { return id; }
	    public String getName() { return name; }
	    public String getPhone() { return phone; }

	    public void setPhone(String phone) { this.phone = phone; }

	    public void display() {
	        System.out.println("User: " + name + " | Phone: " + phone);
	    }
	}

	// ================= RIDER =================
	class Rider extends User {
	    private String preferredPayment;
	    private double rating;
	    private List<String> rideHistory;

	    public Rider(int id, String name, String phone, String preferredPayment) {
	        super(id, name, phone);
	        this.preferredPayment = preferredPayment;
	        this.rating = 5.0; // default
	        this.rideHistory = new ArrayList<>();
	    }

	    public String getPreferredPayment() { return preferredPayment; }
	    public double getRating() { return rating; }
	    public List<String> getRideHistory() { return rideHistory; }

	    public void addRideHistory(String ride) { rideHistory.add(ride); }

	    public void updateRating(double newRating) {
	        this.rating = (this.rating + newRating) / 2;
	    }

	    @Override
	    public void display() {
	        System.out.println("Rider: " + name + " | Payment: " + preferredPayment + " | Rating: " + rating);
	    }
	}

	// ================= DRIVER =================
	class Driver extends User {
	    private String vehicleNo;
	    private String vehicleType;
	    private boolean available;
	    private double rating;
	    private double earnings;

	    public Driver(int id, String name, String phone, String vehicleNo, String vehicleType) {
	        super(id, name, phone);
	        this.vehicleNo = vehicleNo;
	        this.vehicleType = vehicleType;
	        this.available = true;
	        this.rating = 5.0;
	        this.earnings = 0;
	    }

	    public boolean isAvailable() { return available; }
	    public double getRating() { return rating; }
	    public double getEarnings() { return earnings; }

	    public void setAvailable(boolean available) { this.available = available; }

	    public void completeRide(double fare, double newRating) {
	        this.earnings += fare;
	        this.rating = (this.rating + newRating) / 2;
	        this.available = true;
	    }

	    @Override
	    public void display() {
	        System.out.println("Driver: " + name + " | Vehicle: " + vehicleNo + " | Type: " + vehicleType + " | Rating: " + rating + " | Earnings: " + earnings);
	    }
	}

	// ================= RIDE SERVICE =================
	class RideService {
	    private List<Rider> riders;
	    private List<Driver> drivers;
	    private List<String> tripHistory;

	    public RideService() {
	        riders = new ArrayList<>();
	        drivers = new ArrayList<>();
	        tripHistory = new ArrayList<>();
	    }

	    public void registerRider(Rider rider) {
	        riders.add(rider);
	        System.out.println("Rider " + rider.getName() + " registered.");
	    }

	    public void registerDriver(Driver driver) {
	        drivers.add(driver);
	        System.out.println("Driver " + driver.getName() + " registered.");
	    }

	    public Driver findAvailableDriver() {
	        for (Driver d : drivers) {
	            if (d.isAvailable()) {
	                d.setAvailable(false);
	                return d;
	            }
	        }
	        return null;
	    }

	    // Overloaded fare calculation
	    public double fare(double distanceKm) {
	        return distanceKm * 10; // simple fare
	    }

	    public double fare(double distanceKm, int timeMinutes, double surcharge) {
	        return (distanceKm * 10) + (timeMinutes * 2) + surcharge;
	    }

	    public void requestRide(Rider rider, double distance, int time) {
	        Driver driver = findAvailableDriver();
	        if (driver == null) {
	            System.out.println("No drivers available right now!");
	            return;
	        }
	        double rideFare = fare(distance, time, 20); // using overloaded fare
	        String trip = "Rider " + rider.getName() + " with Driver " + driver.getName() + " | Fare: " + rideFare;
	        tripHistory.add(trip);
	        rider.addRideHistory(trip);
	        driver.completeRide(rideFare, 4.5); // update driver earnings + rating
	        System.out.println("✅ Ride Completed: " + trip);
	    }

	    public void showTripHistory() {
	        System.out.println("=== Trip History ===");
	        for (String trip : tripHistory) {
	            System.out.println(trip);
	        }
	    }

	    public void showDriverLeaderboard() {
	        System.out.println("=== Driver Leaderboard ===");
	        drivers.stream()
	                .sorted((d1, d2) -> Double.compare(d2.getEarnings(), d1.getEarnings()))
	                .forEach(Driver::display);
	    }

	    public void showRiderHistory(Rider rider) {
	        System.out.println("=== Ride History for " + rider.getName() + " ===");
	        for (String ride : rider.getRideHistory()) {
	            System.out.println(ride);
	        }
	    }
	}

	// ================= MAIN APP =================
	public class RideAppMain {
	    public static void main(String[] args) {
	        RideService rs = new RideService();

	        // Register Riders
	        Rider r1 = new Rider(1, "Karthik", "9999999999", "UPI");
	        Rider r2 = new Rider(2, "Anitha", "8888888888", "Cash");
	        rs.registerRider(r1);
	        rs.registerRider(r2);

	        // Register Drivers
	        Driver d1 = new Driver(101, "Ravi", "7777777777", "TN01AB1234", "Sedan");
	        Driver d2 = new Driver(102, "Mani", "6666666666", "TN02CD5678", "SUV");
	        rs.registerDriver(d1);
	        rs.registerDriver(d2);

	        // Request Rides
	        rs.requestRide(r1, 12.5, 25); // distance + time
	        rs.requestRide(r2, 5, 10);

	        // Show Trip History
	        rs.showTripHistory();

	        // Show Driver Leaderboard
	        rs.showDriverLeaderboard();

	        // Show Rider Specific History
	        rs.showRiderHistory(r1);
	    }
	}


