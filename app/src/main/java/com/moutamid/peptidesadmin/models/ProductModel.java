package com.moutamid.peptidesadmin.models;

public class ProductModel {
    String ID, name, category, bodyType, shortDesc, longDesc, image;
    boolean isSARMS;
    public ProductModel() {
    }

    public ProductModel(String ID, String name, String category, String bodyType, String shortDesc, String longDesc, String image, boolean isSARMS) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.bodyType = bodyType;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.image = image;
        this.isSARMS = isSARMS;
    }

    public boolean isSARMS() {
        return isSARMS;
    }

    public void setSARMS(boolean SARMS) {
        isSARMS = SARMS;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
