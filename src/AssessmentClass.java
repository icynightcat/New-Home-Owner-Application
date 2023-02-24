import java.util.Objects;

public class AssessmentClass {
    private double class1Percent;
    private double class2Percent;
    private double class3Percent;
    private String class1;
    private String class2;
    private String class3;

    public AssessmentClass(double class1Percent, double class2Percent, double class3Percent, String class1, String class2, String class3){
        this.class1Percent = class1Percent;
        this.class2Percent = class2Percent;
        this.class3Percent = class3Percent;
        this.class1 = class1;
        this.class2 = class2;
        this.class3 = class3;
    }

    @Override
    public String toString() {
        String string = "[" + class1 + " " + class1Percent + "%";
        if (class2Percent > 0) {
            string = string.concat( ", " + class2 + " " + class2Percent + "%");
        }
        if (class3Percent > 0) {
            string = string.concat(", " + class3 + " " + class3Percent + "%");
        }
        string = string.concat("]");
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentClass that = (AssessmentClass) o;
        return Double.compare(that.class1Percent, class1Percent) == 0 && Double.compare(that.class2Percent, class2Percent) == 0 && Double.compare(that.class3Percent, class3Percent) == 0 && class1.equals(that.class1) && Objects.equals(class2, that.class2) && Objects.equals(class3, that.class3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(class1Percent, class2Percent, class3Percent, class1, class2, class3);
    }

    public double getClass1Percent() {
        return class1Percent;
    }

    public double getClass2Percent() {
        return class2Percent;
    }

    public double getClass3Percent() {
        return class3Percent;
    }

    public String getClass1() {
        return class1;
    }

    public String getClass2() {
        return class2;
    }

    public String getClass3() {
        return class3;
    }
}
