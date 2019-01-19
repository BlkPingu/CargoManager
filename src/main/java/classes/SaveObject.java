package serialization;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class SaveObject implements Serializable {

    static final long serialVersionUID=1L;

    private String type;
    private String customer;
    private Integer position;
    private Integer size;
    private Boolean radioactive;
    private Boolean flammable;
    private Boolean toxic;
    private Boolean explosive;
    private String properties;


    public SaveObject(String type, String customer, Integer position, Integer size, Boolean radioactive, Boolean flammable, Boolean toxic, Boolean explosive, String properties) {
        this.type = type;
        this.customer = customer;
        this.position = position;
        this.size = size;
        this.radioactive = radioactive;
        this.flammable = flammable;
        this.toxic = toxic;
        this.explosive = explosive;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getRadioactive() {
        return radioactive;
    }

    public void setRadioactive(Boolean radioactive) {
        this.radioactive = radioactive;
    }

    public Boolean getFlammable() {
        return flammable;
    }

    public void setFlammable(Boolean flammable) {
        this.flammable = flammable;
    }

    public Boolean getToxic() {
        return toxic;
    }

    public void setToxic(Boolean toxic) {
        this.toxic = toxic;
    }

    public Boolean getExplosive() {
        return explosive;
    }

    public void setExplosive(Boolean explosive) {
        this.explosive = explosive;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
