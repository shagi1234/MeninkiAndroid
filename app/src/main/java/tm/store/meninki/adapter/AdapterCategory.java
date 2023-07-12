package tm.store.meninki.adapter;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.ItemCategoriesBinding;
import tm.store.meninki.databinding.ItemShopBindingBinding;
import tm.store.meninki.fragment.FragmentAddProduct;
import tm.store.meninki.fragment.FragmentCategoryList;
import tm.store.meninki.fragment.FragmentNewShop;
import tm.store.meninki.fragment.FragmentOpenCategory;
import tm.store.meninki.interfaces.OnCategoryChecked;
import tm.store.meninki.interfaces.OnShopChecked;

public class AdapterCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<CategoryDto> categories = new ArrayList<>();
    private ArrayList<UserProfile> shops = new ArrayList<>();
    private int type;
    private MaterialCheckBox lastChecked;
    Activity activity;

    public AdapterCategory(Context context, int type, Activity activity) {
        this.type = type;
        this.context = context;
        this.activity = activity;
    }

    //test
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (type == FragmentCategoryList.TYPE_SHOP) {
            ItemShopBindingBinding i = ItemShopBindingBinding.inflate(layoutInflater, parent, false);
            return new AdapterCategory.ShopHolder(i);
        }

        ItemCategoriesBinding i = ItemCategoriesBinding.inflate(layoutInflater, parent, false);
        return new AdapterCategory.CategoryHolder(i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (type == FragmentCategoryList.TYPE_SHOP) {
            ((ShopHolder) holder).bind();
            return;
        }

        ((CategoryHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        if (type == FragmentCategoryList.TYPE_SHOP) {
            if (shops == null) {
                return 0;
            }
            return shops.size();
        }

        if (categories == null) {
            return 0;
        }

        return categories.size();
    }

    public void setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setShops(ArrayList<UserProfile> shops) {
        this.shops = shops;
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding b;

        public CategoryHolder(@NonNull ItemCategoriesBinding itemView) {
            super(itemView.getRoot());
            this.b = itemView;
        }

        public void bind() {
            if (type == FragmentCategoryList.TYPE_SUBCATEGORY) {
                b.title.setVisibility(View.GONE);
                b.titleTxt.setVisibility(View.VISIBLE);

                b.titleTxt.setText(categories.get(getAdapterPosition()).getName());

                b.click.setOnClickListener(v -> {
                    b.click.setEnabled(false);

                    addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentOpenCategory.newInstance(categories.get(getAdapterPosition()).getName(), categories.get(getAdapterPosition()).getId()));
                    new Handler().postDelayed(() -> b.click.setEnabled(true), 200);

                });
                return;
            }
            b.title.setVisibility(View.VISIBLE);
            b.titleTxt.setVisibility(View.GONE);
            b.title.setText(categories.get(getAdapterPosition()).getName());

            b.title.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Fragment addProduct = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
                Fragment addShop = mainFragmentManager.findFragmentByTag(FragmentNewShop.class.getName());
                if (addProduct instanceof OnCategoryChecked) {
                    ((OnCategoryChecked) addProduct).onChecked(isChecked, categories.get(getAdapterPosition()));
                }
                if (addShop instanceof OnCategoryChecked) {
                    ((OnCategoryChecked) addShop).onChecked(isChecked, categories.get(getAdapterPosition()));
                }
            });

            if (categories.get(getAdapterPosition()).getSubCategories() != null && categories.get(getAdapterPosition()).getSubCategories().size() != 0) {
                b.layNext.setVisibility(View.VISIBLE);
                b.nextBtn.setOnClickListener(v -> {
                    b.nextBtn.setEnabled(false);

                    addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentCategoryList.newInstance(categories.get(getAdapterPosition()).getSubCategories(), FragmentCategoryList.TYPE_CATEGORY));
                    new Handler().postDelayed(() -> b.nextBtn.setEnabled(true), 200);
                });
                return;
            }

            b.layNext.setVisibility(View.GONE);

        }
    }

    public class ShopHolder extends RecyclerView.ViewHolder {
        ItemShopBindingBinding b;

        public ShopHolder(ItemShopBindingBinding i) {
            super(i.getRoot());
            this.b = i;
        }

        public void bind() {
            b.title.setText(shops.get(getAdapterPosition()).getName());

            b.title.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (lastChecked != null && lastChecked != b.title) {
                    lastChecked.setChecked(false);
                }

                Fragment addProduct = mainFragmentManager.findFragmentByTag(FragmentAddProduct.class.getName());
                if (addProduct instanceof OnShopChecked) {
                    ((OnShopChecked) addProduct).onShopChecked(isChecked, shops.get(getAdapterPosition()));
//                    activity.onBackPressed();
                }

                lastChecked = b.title;

            });
        }
    }
}
