package com.example.playerslidding.data;

import java.util.ArrayList;

public class ShopDTO {
    private String image;
    private String name;
    private String desc;
    private ArrayList<GridDto> product;

    public ShopDTO(String image, String name, String desc, ArrayList<GridDto> product) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.product = product;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<GridDto> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<GridDto> product) {
        this.product = product;
    }
}
