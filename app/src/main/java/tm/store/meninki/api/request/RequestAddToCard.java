package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

public class RequestAddToCard {

	@SerializedName("productId")
	private String productId;

	@SerializedName("count")
	private int count;

	@SerializedName("personalCharacteristicsId")
	private String personalCharacteristicsId;

	@SerializedName("shopId")
	private String shopId;

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setPersonalCharacteristicsId(String personalCharacteristicsId){
		this.personalCharacteristicsId = personalCharacteristicsId;
	}

	public String getPersonalCharacteristicsId(){
		return personalCharacteristicsId;
	}

	public void setShopId(String shopId){
		this.shopId = shopId;
	}

	public String getShopId(){
		return shopId;
	}
}