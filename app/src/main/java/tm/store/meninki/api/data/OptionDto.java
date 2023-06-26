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

    public void setId(String id) {
        this.id = id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setOptionsImageId(String optionsImageId) {
        this.optionsImageId = optionsImageId;
    }

    public void setOptionType(int optionType) {
        this.optionType = optionType;
    }

    public void setOptionLevel(int optionLevel) {
        this.optionLevel = optionLevel;
    }
}
