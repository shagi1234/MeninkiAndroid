package tm.store.meninki.adapter;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.StaticMethods.dpToPx;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setMargins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import tm.store.meninki.R;
import tm.store.meninki.data.TabItemCustom;
import tm.store.meninki.databinding.ItemTabBinding;
import tm.store.meninki.fragment.FragmentMyShops;
import tm.store.meninki.interfaces.OnTabClicked;

import java.util.ArrayList;

public class AdapterTabLayout extends RecyclerView.Adapter<AdapterTabLayout.TabLayoutHolder> {
    private Context context;
    private ArrayList<TabItemCustom> tabs = new ArrayList<>();
    private final ViewPager viewPager;
    public boolean isClicked;
    int leftMargin;
    public int lastClicked;

    public AdapterTabLayout(Context context, ViewPager viewPager) {
        this.context = context;
        this.viewPager = viewPager;
    }

    @NonNull
    @Override
    public TabLayoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ItemTabBinding tab = ItemTabBinding.inflate(layoutInflater, parent, false);
        return new AdapterTabLayout.TabLayoutHolder(tab);
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
        private final ItemTabBinding b;

        public TabLayoutHolder(@NonNull ItemTabBinding itemView) {
            super(itemView.getRoot());
            b = itemView;
        }

        public void bind() {

            if (viewPager == null) leftMargin = 20;
            else leftMargin = 10;

            if (getAdapterPosition() == 0) {
                setMargins(b.getRoot(), dpToPx(leftMargin, context), dpToPx(10, context), dpToPx(4, context), 20);
            } else if (getAdapterPosition() == getItemCount() - 1) {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(10, context), dpToPx(leftMargin, context), 20);
            } else {
                setMargins(b.getRoot(), dpToPx(4, context), dpToPx(10, context), dpToPx(4, context), 20);
            }

            b.title.setText(tabs.get(getAdapterPosition()).getTitle());

            setActive(tabs.get(getAdapterPosition()).isActive());

            b.getRoot().setOnClickListener(v -> {

                Fragment shops = mainFragmentManager.findFragmentByTag(FragmentMyShops.class.getName());

                if (tabs.get(getAdapterPosition()).isActive()) return;

                isClicked = true;

                tabs.get(getAdapterPosition()).setActive(true);
                tabs.get(lastClicked).setActive(false);

                notifyItemChanged(getAdapterPosition());
                notifyItemChanged(lastClicked);

                if (shops instanceof OnTabClicked) {
                    ((OnTabClicked) shops).onClick(getAdapterPosition());
                    lastClicked = getAdapterPosition();
                    return;
                }

                setCurrentItemViewPager(getAdapterPosition());
            });

        }

        private void setCurrentItemViewPager(int adapterPosition) {
            if (viewPager == null) return;
            viewPager.setCurrentItem(adapterPosition, true);
        }

        private void setActive(boolean isActive) {
            if (isActive) {
                setBackgroundDrawable(context, b.title, R.color.custom, 0, 4, false, 0);
                b.title.setTextColor(context.getResources().getColor(R.color.dark));
            } else {
                setBackgroundDrawable(context, b.title, R.color.hover, 0, 4, false, 0);
                b.title.setTextColor(context.getResources().getColor(R.color.caption));
            }
        }
    }

    public void setTabs(ArrayList<TabItemCustom> tabs) {
        this.tabs = tabs;
        notifyDataSetChanged();
    }
}
