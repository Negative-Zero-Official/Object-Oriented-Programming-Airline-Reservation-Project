package com.dutyfree;

public class DutyFreeItem {
    String name;
    DutyFreeType section;
    float price;

    public DutyFreeItem(String name, DutyFreeType section, float price) {
        this.name = name;
        this.price = price;
        this.section = section;
    }
}