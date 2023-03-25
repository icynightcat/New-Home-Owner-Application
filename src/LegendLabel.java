import javafx.scene.paint.Color;

public class LegendLabel {
    private final String name;
    private final Color color;


    public LegendLabel(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

}
