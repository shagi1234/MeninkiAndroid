package tm.store.meninki.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import tm.store.meninki.api.data.DtoBanner;
import tm.store.meninki.api.data.ResponsePostGetAllItem;
import tm.store.meninki.api.data.response.ResponseCard;
import tm.store.meninki.api.data.response.ResponseHomeShops;

public class HomeArray {
   private DtoBanner banner;
   private ArrayList<ResponseCard> popularProducts=new ArrayList<>();
   @SerializedName("popularPost")
   private ArrayList<ResponsePostGetAllItem> popularPosts=new ArrayList<>();
   private ArrayList<ResponseHomeShops> shops=new ArrayList<>();
   private ArrayList<ResponseCard> newProducts=new ArrayList<>();

   public DtoBanner getBanner() {
      return banner;
   }

   public void setBanner(DtoBanner banner) {
      this.banner = banner;
   }

   public ArrayList<ResponseCard> getPopularProducts() {
      return popularProducts;
   }

   public ArrayList<ResponsePostGetAllItem> getPopularPosts() {
      return popularPosts;
   }

   public ArrayList<ResponseHomeShops> getShops() {
      return shops;
   }

   public ArrayList<ResponseCard> getNewProducts() {
      return newProducts;
   }
}
