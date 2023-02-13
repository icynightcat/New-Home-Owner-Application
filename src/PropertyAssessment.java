import java.util.Objects;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    private int accountNumber; //unique identifier
    private Address address;
    private char garage;
    private Neighbourhood neighbourhood;
    private int assessedValue;
    private Location location;
    private AssessmentClass assessmentClass;

    public PropertyAssessment(int accountNumber, String suite, int houseNumber, String streetName, char garage, int neighbourhoodID, String neighbourhood, String ward, int assessedValue, double latitude, double longitude, double class1Percent, double class2Percent, double class3Percent, String class1, String class2, String class3){
        this.accountNumber = accountNumber;
        this.address = new Address(suite, houseNumber, streetName);
        this.garage = garage;
        this.neighbourhood = new Neighbourhood(neighbourhoodID, neighbourhood, ward);
        this.assessedValue = assessedValue;
        this.location = new Location(latitude, longitude);
        this.assessmentClass = new AssessmentClass(class1Percent, class2Percent, class3Percent, class1, class2, class3);
    }

    //note: do not format this super specifically like the example prints, just do "item: value"
    @Override
    public String toString(){
        return String.format("""
                Account number = %s
                Address = %s
                Assessed value = $%,d
                Assessment class = %s
                Neighbourhood = %s
                Location = %s""", accountNumber, address, assessedValue, assessmentClass, neighbourhood, location);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return accountNumber == that.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Address getAddress() {
        return address;
    }

    public char getGarage() {
        return garage;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public int getAssessedValue() {
        return assessedValue;
    }

    public Location getLocation() {
        return location;
    }

    public AssessmentClass getAssessmentClass() {
        return assessmentClass;
    }

    @Override
    public int compareTo(PropertyAssessment o) {
        if (o == null){
            throw new NullPointerException("PropertyAssessment cannot be null");
        }
        return Integer.compare(assessedValue, o.assessedValue);
    }
}
