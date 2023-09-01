package tm.store.meninki.api.data;

import java.util.ArrayList;

import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.data.ShopDTO;

public class ResponseOrderGetAll {
    private String id;
    private ShopDTO shop;
    private ArrayList<ResponseCard> products;

    public ArrayList<ResponseCard> getProducts() {
        return products;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProducts(ArrayList<ResponseCard> products) {
        this.products = products;
    }

    public void setShop(ShopDTO shop) {
        this.shop = shop;
    }
}
