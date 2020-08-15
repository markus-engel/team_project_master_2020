package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class LegendItem {

    Circle circle;
    StringProperty label;

    public LegendItem(Circle circle, String label) {
        this.circle=circle;
        this.label = new SimpleStringProperty(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LegendItem that = (LegendItem) o;
        return Objects.equals(circle.getFill(), that.circle.getFill());
    }

    @Override
    public int hashCode() {
        return Objects.hash(circle);
    }

    public Circle getCircle() {
        return circle;
    }

    public String getLabel() {
        return label.get();
    }
}
