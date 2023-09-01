package tm.store.meninki.api.request;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RequestCreateOrder{

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("orderQuantityProducts")
	private List<OrderQuantityProductsItem> orderQuantityProducts;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("adress")
	private String adress;

	@SerializedName("shopId")
	private String shopId;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setOrderQuantityProducts(List<OrderQuantityProductsItem> orderQuantityProducts){
		this.orderQuantityProducts = orderQuantityProducts;
	}

	public List<OrderQuantityProductsItem> getOrderQuantityProducts(){
		return orderQuantityProducts;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setAdress(String adress){
		this.adress = adress;
	}

	public String getAdress(){
		return adress;
	}

	public void setShopId(String shopId){
		this.shopId = shopId;
	}

	public String getShopId(){
		return shopId;
	}
}