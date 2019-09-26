package se.moza.cafeeka.service;


import se.moza.cafeeka.model.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {

    List<Menu> lunchMenus = new ArrayList<>();
    List<Menu> breakfastMenus = new ArrayList<>();

    public MenuDTO() {}

    public MenuDTO(List<Menu> lunchMenus, List<Menu> breakfastMenus) {
        super();
        this.lunchMenus = lunchMenus;
        this.breakfastMenus = breakfastMenus;
    }

    public List<Menu> getLunchMenus() {
        return lunchMenus;
    }

    public void setLunchMenus(List<Menu> lunchMenus) {
        this.lunchMenus = lunchMenus;
    }

    public List<Menu> getBreakfastMenus() {
        return breakfastMenus;
    }

    public void setBreakfastMenus(List<Menu> breakfastMenus) {
        this.breakfastMenus = breakfastMenus;
    }

    public void addToLunchMenus(Menu menu) {
        lunchMenus.add(menu);
    }

    public void addToBreakfastMenus(Menu menu) {
        breakfastMenus.add(menu);
    }

    public boolean isEmpty() {
        if(breakfastMenus.isEmpty() && lunchMenus.isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "MenuDTO [lunchMenus=" + lunchMenus + ", breakfastMenus=" + breakfastMenus + "]";
    }
}
