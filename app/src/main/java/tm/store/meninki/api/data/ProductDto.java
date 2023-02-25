package tm.store.meninki.api.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductDto {

	@SerializedName("shop")
	private Object shop;

	@SerializedName("favourites")
	private List<Object> favourites;

	@SerializedName("discountPrice")
	private int discountPrice;

	@SerializedName("rating")
	private Object rating;

	@SerializedName("description")
	private String description;

	@SerializedName("moderationStatus")
	private int moderationStatus;

	@SerializedName("posts")
	private List<Object> posts;

	@SerializedName("isDeleted")
	private boolean isDeleted;

	@SerializedName("price")
	private int price;

	@SerializedName("id")
	private String id;

	@SerializedName("shopId")
	private String shopId;

	@SerializedName("categories")
	private List<Object> categories;

	@SerializedName("slug")
	private String slug;

	@SerializedName("createDate")
	private String createDate;

	@SerializedName("orderQuantityProducts")
	private List<Object> orderQuantityProducts;

	@SerializedName("comments")
	private List<Object> comments;

	@SerializedName("count")
	private Object count;

	@SerializedName("cardType")
	private int cardType;

	@SerializedName("userId")
	private Object userId;

	@SerializedName("numberView")
	private int numberView;

	@SerializedName("personalCharacteristics")
	private List<Object> personalCharacteristics;

	@SerializedName("medias")
	private List<MediasItem> medias;

	@SerializedName("name")
	private String name;

	@SerializedName("priceBonus")
	private Object priceBonus;

	@SerializedName("user")
	private Object user;

	public Object getShop(){
		return shop;
	}

	public List<Object> getFavourites(){
		return favourites;
	}

	public int getDiscountPrice(){
		return discountPrice;
	}

	public Object getRating(){
		return rating;
	}

	public String getDescription(){
		return description;
	}

	public int getModerationStatus(){
		return moderationStatus;
	}

	public List<Object> getPosts(){
		return posts;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public int getPrice(){
		return price;
	}

	public String getId(){
		return id;
	}

	public String getShopId(){
		return shopId;
	}

	public List<Object> getCategories(){
		return categories;
	}

	public String getSlug(){
		return slug;
	}

	public String getCreateDate(){
		return createDate;
	}

	public List<Object> getOrderQuantityProducts(){
		return orderQuantityProducts;
	}

	public List<Object> getComments(){
		return comments;
	}

	public Object getCount(){
		return count;
	}

	public int getCardType(){
		return cardType;
	}

	public Object getUserId(){
		return userId;
	}

	public int getNumberView(){
		return numberView;
	}

	public List<Object> getPersonalCharacteristics(){
		return personalCharacteristics;
	}

	public List<MediasItem> getMedias(){
		return medias;
	}

	public String getName(){
		return name;
	}

	public Object getPriceBonus(){
		return priceBonus;
	}

	public Object getUser(){
		return user;
	}
}