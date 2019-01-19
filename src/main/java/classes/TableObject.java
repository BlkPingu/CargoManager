package classes;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class TableObject implements Serializable {


    private final SimpleStringProperty type;
    private final SimpleStringProperty customer;
    private final SimpleIntegerProperty position;
    private final SimpleIntegerProperty size;
    private final SimpleBooleanProperty radioactive;
    private final SimpleBooleanProperty flammable;
    private final SimpleBooleanProperty toxic;
    private final SimpleBooleanProperty explosive;
    private final SimpleStringProperty properties;


    public TableObject(String type, String customer, int position, int size,  boolean radioactive, boolean flammable, boolean toxic, boolean explosive, String properties) {
        this.type = new SimpleStringProperty(type);
        this.customer = new SimpleStringProperty(customer);
        this.position = new SimpleIntegerProperty(position);
        this.size = new SimpleIntegerProperty(size);
        this.radioactive = new SimpleBooleanProperty(radioactive);
        this.flammable = new SimpleBooleanProperty(flammable);
        this.toxic = new SimpleBooleanProperty(toxic);
        this.explosive = new SimpleBooleanProperty(explosive);
        this.properties = new SimpleStringProperty(properties);
    }


    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getCustomer() {
        return customer.get();
    }

    public SimpleStringProperty customerProperty() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public int getPosition() {
        return position.get();
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public int getSize() {
        return size.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public boolean isRadioactive() {
        return radioactive.get();
    }

    public SimpleBooleanProperty radioactiveProperty() {
        return radioactive;
    }

    public void setRadioactive(boolean radioactive) {
        this.radioactive.set(radioactive);
    }

    public boolean isFlammable() {
        return flammable.get();
    }

    public SimpleBooleanProperty flammableProperty() {
        return flammable;
    }

    public void setFlammable(boolean flammable) {
        this.flammable.set(flammable);
    }

    public boolean isToxic() {
        return toxic.get();
    }

    public SimpleBooleanProperty toxicProperty() {
        return toxic;
    }

    public void setToxic(boolean toxic) {
        this.toxic.set(toxic);
    }

    public boolean isExplosive() {
        return explosive.get();
    }

    public SimpleBooleanProperty explosiveProperty() {
        return explosive;
    }

    public void setExplosive(boolean explosive) {
        this.explosive.set(explosive);
    }

    public String getProperties() {
        return properties.get();
    }

    public SimpleStringProperty propertiesProperty() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties.set(properties);
    }

}
