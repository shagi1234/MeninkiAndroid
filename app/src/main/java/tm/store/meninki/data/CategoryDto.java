package tm.store.meninki.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.store.meninki.api.data.ProductDto;

public class CategoryDto {
    @SerializedName("nameRu")
    private String name;
    @SerializedName("nameTm")
    private String nameRu;
    private String nameEn;
    private String categoryImage;
    private String parentId;
    private String id;
    private String parent;
    private ArrayList<CategoryDto> subCategories;
    private ArrayList<ShopDTO> shops;
    private ArrayList<ProductDto> products;
    private boolean isActive;

    public CategoryDto(String name, String nameRu, String nameEn, String categoryImage, String parentId, String id, String parent, ArrayList<CategoryDto> subCategories, ArrayList<ShopDTO> shops, ArrayList<ProductDto> products, boolean isActive) {
        this.name = name;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.categoryImage = categoryImage;
        this.parentId = parentId;
        this.id = id;
        this.parent = parent;
        this.subCategories = subCategories;
        this.shops = shops;
        this.products = products;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<CategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<CategoryDto> subCategories) {
        this.subCategories = subCategories;
    }

    public ArrayList<ShopDTO> getShops() {
        return shops;
    }

    public void setShops(ArrayList<ShopDTO> shops) {
        this.shops = shops;
    }

    public ArrayList<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDto> products) {
        this.products = products;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
