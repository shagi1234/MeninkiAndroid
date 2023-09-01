package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

public class OrderQuantityProductsItem{

	@SerializedName("productId")
	private String productId;

	@SerializedName("orderId")
	private String orderId;

	@SerializedName("count")
	private int count;

	@SerializedName("personalCharacteristicsId")
	private String personalCharacteristicsId;

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
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
}