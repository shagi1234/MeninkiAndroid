package tm.store.meninki.api.data;

import java.util.ArrayList;

public class PersonalCharacterDto {
    private String id;
    private ArrayList<OptionDto> options;
    private int count;
    private int priceBonus;
    private int price;
    private int discountPrice;
    private String productId;

    public String getId() {
        return id;
    }

    public ArrayList<OptionDto> getOptions() {
        return options;
    }

    public int getCount() {
        return count;
    }

    public int getPriceBonus() {
        return priceBonus;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public String getProductId() {
        return productId;
    }
}
