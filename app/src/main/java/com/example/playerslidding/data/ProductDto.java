package com.example.playerslidding.data;

public class ProductDto {
    private String orientation;
    private String imagePath;
    private String title;
    private String price;
    private String oldPrice;
    private String sale;
    private int count;

    public ProductDto(String orientation, String imagePath, String title, String price, String oldPrice, String sale, int count) {
        this.orientation = orientation;
        this.imagePath = imagePath;
        this.title = title;
        this.price = price;
        this.oldPrice = oldPrice;
        this.sale = sale;
        this.count = count;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
