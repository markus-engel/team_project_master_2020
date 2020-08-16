package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class SelectedContig {

    StringProperty ID;
    StringProperty taxonomicName;

    public SelectedContig(String ID, String taxName) {
        this.ID = new SimpleStringProperty(ID);
        this.taxonomicName = new SimpleStringProperty(taxName);
    }

    public String getID() { return ID.get(); }

    public String getTaxonomicName() { return taxonomicName.get(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedContig that = (SelectedContig) o;
        return Objects.equals(ID.get(), that.ID.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
