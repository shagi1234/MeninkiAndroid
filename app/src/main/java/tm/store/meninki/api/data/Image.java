package tm.store.meninki.api.data;

import com.google.gson.annotations.SerializedName;

public class Image{

	@SerializedName("orientationType")
	private int orientationType;

	@SerializedName("directoryCompressed")
	private String directoryCompressed;

	@SerializedName("directoryThumbnails")
	private String directoryThumbnails;

	@SerializedName("postMediaId")
	private String postMediaId;

	@SerializedName("postMedia")
	private Object postMedia;

	@SerializedName("id")
	private String id;

	@SerializedName("directoryOriginal")
	private String directoryOriginal;

	@SerializedName("isAvatar")
	private boolean isAvatar;

	@SerializedName("imageType")
	private int imageType;

	public int getOrientationType(){
		return orientationType;
	}

	public String getDirectoryCompressed(){
		return directoryCompressed;
	}

	public String getDirectoryThumbnails(){
		return directoryThumbnails;
	}

	public String getPostMediaId(){
		return postMediaId;
	}

	public Object getPostMedia(){
		return postMedia;
	}

	public String getId(){
		return id;
	}

	public String getDirectoryOriginal(){
		return directoryOriginal;
	}

	public boolean isIsAvatar(){
		return isAvatar;
	}

	public int getImageType(){
		return imageType;
	}
}