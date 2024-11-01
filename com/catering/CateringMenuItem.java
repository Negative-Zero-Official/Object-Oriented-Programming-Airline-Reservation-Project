package com.catering;

public class CateringMenuItem {
    public String name;
    public CateringType type;
    public String allergens;

    public CateringMenuItem(String name, CateringType type, String allergens) {
        this.name = name;
        this.type = type;
        this.allergens = allergens;
    }

    @Override
    public String toString() {
        return "Dish name: "+name+"\n Type: "+type+"\nAlergens: "+allergens;
    }
}