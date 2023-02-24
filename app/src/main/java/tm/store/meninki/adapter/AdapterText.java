package tm.store.meninki.adapter;

import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tm.store.meninki.R;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.ItemTextBinding;
import tm.store.meninki.fragment.FragmentOpenCategory;

import java.util.ArrayList;

import tm.store.meninki.utils.Const;
import tm.store.meninki.utils.FragmentHelper;
import tm.store.meninki.utils.StaticMethods;

public class AdapterText extends RecyclerView.Adapter<AdapterText.TabLayoutHolder> {
    private Context context;
    private ArrayList<CategoryDto> tabs = new ArrayList<>();

    public AdapterText(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TabLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemTextBinding tab = ItemTextBinding.inflate(layoutInflater, parent, false);
        return new AdapterText.TabLayoutHolder(tab);
    }

    @Override
    public void onBindViewHolder(@NonNull TabLayoutHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        if (tabs == null) {
            return 0;
        }
        return tabs.size();
    }

    public class TabLayoutHolder extends RecyclerView.ViewHolder {
        private final ItemTextBinding b;

        public TabLayoutHolder(@NonNull ItemTextBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }

        public void bind() {

            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(20, context), StaticMethods.dpToPx(30, context), StaticMethods.dpToPx(20, context), 0);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), StaticMethods.dpToPx(20, context), 0, StaticMethods.dpToPx(20, context), StaticMethods.dpToPx(30, context));
            } else {
                setMargins(b.getRoot(), StaticMethods.dpToPx(20, context), 0, StaticMethods.dpToPx(20, context), 0);
            }


            b.title.setText(tabs.get(getAdapterPosition()).getName());

            setActive(tabs.get(getAdapterPosition()).isActive());

            b.getRoot().setOnClickListener(v -> FragmentHelper.addFragment(Const.mainFragmentManager, R.id.fragment_container_main, FragmentOpenCategory.newInstance(tabs.get(getAdapterPosition()).getName(), "")));

        }

        private void setActive(boolean active) {
            if (active) {
                b.title.setTextColor(context.getResources().getColor(R.color.alert));
                b.point.setVisibility(View.VISIBLE);
            } else {
                b.point.setVisibility(View.GONE);
                b.title.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

    }

    public void setTabs(ArrayList<CategoryDto> tabs) {
        this.tabs = tabs;
        notifyDataSetChanged();
    }
}
