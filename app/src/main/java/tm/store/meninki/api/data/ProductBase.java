package tm.store.meninki.api.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ProductBase{

	@SerializedName("shop")
	private Object shop;

	@SerializedName("comments")
	private List<Object> comments;

	@SerializedName("favourites")
	private List<Object> favourites;

	@SerializedName("cardType")
	private int cardType;

	@SerializedName("discountPrice")
	private int discountPrice;

	@SerializedName("rating")
	private Object rating;

	@SerializedName("description")
	private String description;

	@SerializedName("moderationStatus")
	private int moderationStatus;

	@SerializedName("userId")
	private Object userId;

	@SerializedName("numberView")
	private int numberView;

	@SerializedName("medias")
	private List<MediasItem> medias;

	@SerializedName("isDeleted")
	private boolean isDeleted;

	@SerializedName("price")
	private int price;

	@SerializedName("name")
	private String name;

	@SerializedName("priceBonus")
	private Object priceBonus;

	@SerializedName("id")
	private String id;

	@SerializedName("shopId")
	private String shopId;

	@SerializedName("categories")
	private List<Object> categories;

	@SerializedName("user")
	private Object user;

	@SerializedName("slug")
	private String slug;

	@SerializedName("createDate")
	private String createDate;

	public Object getShop(){
		return shop;
	}

	public List<Object> getComments(){
		return comments;
	}

	public List<Object> getFavourites(){
		return favourites;
	}

	public int getCardType(){
		return cardType;
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

	public Object getUserId(){
		return userId;
	}

	public int getNumberView(){
		return numberView;
	}

	public List<MediasItem> getMedias(){
		return medias;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public int getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public Object getPriceBonus(){
		return priceBonus;
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

	public Object getUser(){
		return user;
	}

	public String getSlug(){
		return slug;
	}

	public String getCreateDate(){
		return createDate;
	}
}