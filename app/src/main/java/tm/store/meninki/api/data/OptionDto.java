package tm.store.meninki.api.data;

public class OptionDto {

    private String id;
    private String imagePath;
    private String value;
    private String productId;
    private String optionsImageId;
    private int optionType;
    private int optionLevel;

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getValue() {
        return value;
    }

    public String getProductId() {
        return productId;
    }

    public String getOptionsImageId() {
        return optionsImageId;
    }

    public int getOptionType() {
        return optionType;
    }

    public int getOptionLevel() {
        return optionLevel;
    }
}
