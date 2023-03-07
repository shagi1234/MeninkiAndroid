package tm.store.meninki.api.response;

import java.util.ArrayList;

import tm.store.meninki.data.ShopDTO;

public class ResponseHomeShops {
    private ShopDTO shop;
    private ArrayList<ResponseCard> products;

    public ShopDTO getShop() {
        return shop;
    }

    public ArrayList<ResponseCard> getProducts() {
        return products;
    }
}
