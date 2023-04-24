package tm.store.meninki.api.request;

import com.google.gson.annotations.SerializedName;

public class RequestAddPost{

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("productBaseId")
	private String productBaseId;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setProductBaseId(String productBaseId){
		this.productBaseId = productBaseId;
	}

	public String getProductBaseId(){
		return productBaseId;
	}
}