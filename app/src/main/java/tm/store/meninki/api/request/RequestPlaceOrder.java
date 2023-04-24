package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

public class RequestPlaceOrder{

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("adress")
	private String adress;

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
}