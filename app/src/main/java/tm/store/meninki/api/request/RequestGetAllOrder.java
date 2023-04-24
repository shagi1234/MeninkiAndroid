package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

public class RequestGetAllOrder{

	@SerializedName("orderStatus")
	private int orderStatus;

	@SerializedName("shopId")
	private String shopId;

	public void setOrderStatus(int orderStatus){
		this.orderStatus = orderStatus;
	}

	public int getOrderStatus(){
		return orderStatus;
	}

	public void setShopId(String shopId){
		this.shopId = shopId;
	}

	public String getShopId(){
		return shopId;
	}
}