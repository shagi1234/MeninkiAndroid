package tm.store.meninki.api.data;

import com.google.gson.annotations.SerializedName;

public class MediasItem{

	@SerializedName("image")
	private Image image;

	@SerializedName("number")
	private int number;

	@SerializedName("productBaseId")
	private String productBaseId;

	@SerializedName("mediaType")
	private int mediaType;

	@SerializedName("id")
	private String id;

	@SerializedName("video")
	private Object video;

	@SerializedName("productBase")
	private ProductBase productBase;

	public Image getImage(){
		return image;
	}

	public int getNumber(){
		return number;
	}

	public String getProductBaseId(){
		return productBaseId;
	}

	public int getMediaType(){
		return mediaType;
	}

	public String getId(){
		return id;
	}

	public Object getVideo(){
		return video;
	}

	public ProductBase getProductBase(){
		return productBase;
	}
}