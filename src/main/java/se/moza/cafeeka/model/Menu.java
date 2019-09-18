package se.moza.cafeeka.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.moza.cafeeka.model.audit.DateAudit;

@Entity
public class Menu extends DateAudit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private MenuType menuType;
    private boolean vego;
    private boolean glutenfree;
    private String name;
    private String title;
    private String description;
    private String ingredients;
    private float lowerPrice;
    private float higherPrice;
    private String photoUrl;
    private boolean expired;

    @JsonIgnore
    @Lob
    private byte[] photo;



    public Menu() {	}

    public Menu(MenuType menuType, boolean vego, boolean glutenfree, String name, String title, String description, String ingredients, float lowerPrice, float higherPrice) {
        this.menuType = menuType;
        this.vego = vego;
        this.glutenfree = glutenfree;
        this.name = name;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.lowerPrice = lowerPrice;
        this.higherPrice = higherPrice;
        this.expired = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public boolean isVego() {
        return vego;
    }

    public void setVego(boolean vego) {
        this.vego = vego;
    }

    public boolean isGlutenfree() {
        return glutenfree;
    }

    public void setGlutenfree(boolean glutenfree) {
        this.glutenfree = glutenfree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public float getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(float lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public float getHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(float higherPrice) {
        this.higherPrice = higherPrice;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @JsonIgnore
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", menuType=" + menuType +
                ", vego=" + vego +
                ", glutenfree=" + glutenfree +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", lowerPrice=" + lowerPrice +
                ", higherPrice=" + higherPrice +
                ", photoUrl='" + photoUrl + '\'' +
                ", expired=" + expired +
                '}';
    }
}
