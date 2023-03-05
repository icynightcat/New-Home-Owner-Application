import java.util.Objects;

public class Neighbourhood {
    private int neighbourhoodID; //unique identifier
    private String neighbourhood;
    private String ward;

    public Neighbourhood(int neighbourhoodID, String neighbourhood, String ward){
        this.neighbourhoodID = neighbourhoodID;
        this.neighbourhood = neighbourhood;
        this.ward = ward;
    }

    @Override
    public String toString(){
        if(!ward.equals("")){
            return neighbourhood + " (" + ward + ")";
        }
        return neighbourhood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neighbourhood that = (Neighbourhood) o;
        return neighbourhoodID == that.neighbourhoodID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighbourhoodID);
    }

    public int getNeighbourhoodID() {
        return neighbourhoodID;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getWard() {
        return ward;
    }
}
