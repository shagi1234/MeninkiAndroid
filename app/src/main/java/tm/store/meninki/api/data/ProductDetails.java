package tm.store.meninki.api.data;

import java.util.ArrayList;

import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.data.ShopDTO;

public class ProductDetails {
    private String id;
    private String name;
    private String description;
    private int count;
    private int inCartCount;
    private int price;
    private int discountPrice;
    private boolean isFavorited;
    private UserDto user;
    private ShopDTO shop;
    private Rating rating;
    private ArrayList<OptionDto> options;
    private ArrayList<CategoryDto> categories;
    private ArrayList<PersonalCharacterDto> personalCharacteristics;
    private ArrayList<String> optionTitles;
    private ArrayList<MediaDto> medias;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public int getInCartCount() {
        return inCartCount;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public UserDto getUser() {
        return user;
    }

    public ShopDTO getShop() {
        return shop;
    }

    public Rating getRating() {
        return rating;
    }

    public ArrayList<OptionDto> getOptions() {
        return options;
    }

    public ArrayList<CategoryDto> getCategories() {
        return categories;
    }

    public ArrayList<PersonalCharacterDto> getPersonalCharacteristics() {
        return personalCharacteristics;
    }

    public ArrayList<String> getOptionTitles() {
        return optionTitles;
    }

    public void setOptionTitles(ArrayList<String> optionTitles) {
        this.optionTitles = optionTitles;
    }

    public ArrayList<MediaDto> getMedias() {
        return medias;
    }
}
