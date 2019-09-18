package se.moza.cafeeka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.moza.cafeeka.model.audit.DateAudit;

import javax.persistence.*;

@Entity
public class Drink extends DateAudit {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private float price;
    private boolean alcoholic;
    private String photoUrl;
    private boolean expired;

    @JsonIgnore
    @Lob
    private byte[] photo;


    public Drink(){ }

    public Drink(String name, String description, float price, boolean alcoholic) {
        super();
        this.name = name;
        this.description = description;
        this.price = price;
        this.alcoholic = alcoholic;
        this.expired = false;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @JsonIgnore
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Drink [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + ", alcoholic=" + alcoholic + ", photoUrl=" + photoUrl + "]" + ", expired=" + expired;
    }

}
