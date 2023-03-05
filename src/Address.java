import java.util.Objects;

public class Address {
    private String suite;
    private int houseNumber;
    private String streetName;

    public Address(String suite, int houseNumber, String streetName){
        this.suite = suite;
        this.houseNumber = houseNumber;
        this.streetName = streetName;
    }

    @Override
    public String toString(){
        if(houseNumber != 0){
            return suite + " " + houseNumber + " " + streetName;
        }
        return suite + streetName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return houseNumber == address.houseNumber && Objects.equals(suite, address.suite) && streetName.equals(address.streetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, houseNumber, streetName);
    }

    public String getSuite() {
        return suite;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }
}
