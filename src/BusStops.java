import java.util.Objects;

public class BusStops {
    private int stopNumber;
    private String stopName;
    private double latitude;

    private double longitude;

    public int getStopNumber() {
        return stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStops busStops = (BusStops) o;
        return getStopNumber() == busStops.getStopNumber() && Double.compare(busStops.getLatitude(), getLatitude()) == 0 && Double.compare(busStops.getLongitude(), getLongitude()) == 0 && Objects.equals(getStopName(), busStops.getStopName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStopNumber(), getStopName(), getLatitude(), getLongitude());
    }

    public BusStops(int stopNumber, String stopName, double latitude, double longitude){
        this.stopNumber = stopNumber;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public String toString(){

        return "Stop Number: " + stopNumber + " Stop Name: "
                + stopName + " Latitude: " + latitude + " Longitude: "
                + longitude;
    }


}
