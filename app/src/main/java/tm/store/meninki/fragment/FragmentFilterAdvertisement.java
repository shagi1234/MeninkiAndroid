package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.api.enums.SortType;
import tm.store.meninki.api.request.RequestAllAdvertisement;
import tm.store.meninki.data.CategoryDto;
import tm.store.meninki.databinding.FragmentFilterAdvertisementBinding;
import tm.store.meninki.interfaces.GetAllAdvertisement;
import tm.store.meninki.interfaces.OnUpdateFragment;

public class FragmentFilterAdvertisement extends Fragment {
    FragmentFilterAdvertisementBinding b;
    BottomSheetDialog dialog;
    ArrayList<String> regions = new ArrayList<>();
    ArrayList<String> sortList = new ArrayList<>();
    private int selectedSortType;
    private Integer selectedRegionId;
    private String categoryId;
    private RequestAllAdvertisement requestAllAdvertisement;

    public static FragmentFilterAdvertisement newInstance(RequestAllAdvertisement requestAllAdvertisement) {
        FragmentFilterAdvertisement fragment = new FragmentFilterAdvertisement();
        Bundle args = new Bundle();
        args.putString("request_all_ad", new Gson().toJson(requestAllAdvertisement));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            setPadding(b.headerLay, 0, statusBarHeight, 0, 0);
            setPadding(b.getRoot(), 0, 0, 0, navigationBarHeight);
        }, 50);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestAllAdvertisement = new Gson().fromJson(getArguments().getString("request_all_ad"), RequestAllAdvertisement.class);
            if (requestAllAdvertisement.getCategoryIds() != null)
                categoryId = requestAllAdvertisement.getCategoryIds()[0];

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        b = FragmentFilterAdvertisementBinding.inflate(inflater, container, false);
        setBackgrounds();
        getRegions();
        initListeners();
        return b.getRoot();
    }


    private void getRegions() {
        regions.add("Ahal");
        regions.add("Lebap");
        regions.add("Balkan");
        regions.add("Dashoguz");
        regions.add("Mary");
        regions.add("Ashgabat");
        regions.add("All");

        if (getActivity() == null) return;

        sortList.add(getActivity().getResources().getString(R.string.sort_by_date));
        sortList.add(getActivity().getResources().getString(R.string.sort_by_view_count));
        sortList.add(getActivity().getResources().getString(R.string.sort_by_rating));
        sortList.add(getActivity().getResources().getString(R.string.sort_by_price));
    }

    private void initListeners() {
        b.backIcon.setOnClickListener(view -> {
            if (getActivity() == null) return;
            b.backIcon.setEnabled(false);
            getActivity().onBackPressed();
            new Handler().postDelayed(() -> b.backIcon.setEnabled(true), 200);
        });

        b.sortedByLay.setOnClickListener(view -> showDialog(b.txtSort.getText().toString().trim(), sortList, 0));

        b.clearButton.setOnClickListener(view -> clearFilter());

        b.regionsLay.setOnClickListener(view -> showDialog(b.txtRegion.getText().toString().trim(), regions, 1));

        b.showBtn.setOnClickListener(view -> {
            Fragment fragment = mainFragmentManager.findFragmentByTag(FragmentMain.class.getSimpleName());

            if (fragment instanceof OnUpdateFragment) {
                getActivity().onBackPressed();

                requestAllAdvertisement.setDescending(false);
                requestAllAdvertisement.setPageNumber(1);
                requestAllAdvertisement.setTake(20);

                if (!b.minPrice.getText().toString().trim().isEmpty())
                    requestAllAdvertisement.setMin(Integer.parseInt(b.minPrice.getText().toString().trim()));
                if (!b.maxPrice.getText().toString().trim().isEmpty())
                    requestAllAdvertisement.setMax(Integer.parseInt(b.maxPrice.getText().toString().trim()));

                if (selectedRegionId != null && selectedRegionId < regions.size())
                    requestAllAdvertisement.setWelayats(new int[]{selectedRegionId});

                Log.e(" ", "initListeners: "+selectedSortType);
                requestAllAdvertisement.setSortType(selectedSortType);

                if (categoryId != null)
                    requestAllAdvertisement.setCategoryIds(new String[]{});

                ((OnUpdateFragment) fragment).onUpdated(requestAllAdvertisement);

            }
        });
    }

    private void clearFilter() {
        b.minPrice.setText("");
        b.maxPrice.setText("");
        selectedRegionId = 0;

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.sortedByLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.regionsLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.initialPriceLay, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.tillPriceLay, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.clearButton, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.showBtn, R.color.accent, 0, 10, false, 0);
    }

    private void showDialog(String title, ArrayList<String> items, int i) {
        if (getContext() == null) return;
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_ads_filter);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setDialogComponents(title, items, i);
        dialog.show();

    }

    private void setDialogComponents(String title, ArrayList<String> items, int i) {
        LinearLayout root = dialog.findViewById(R.id.root_lay);
        TextView headerText = dialog.findViewById(R.id.title);
        LinearLayout itemsContent = dialog.findViewById(R.id.items_content);
        headerText.setText(title);

        if (items.size() == 0 || itemsContent == null || getContext() == null) return;

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(0, 10, 0, 0);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.inter_medium);
        int textColor = ContextCompat.getColor(getContext(), R.color.accent);

        for (int j = 0; j < items.size(); j++) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(textParams);
            textView.setTypeface(typeface);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setText(items.get(j));
            itemsContent.addView(textView);

            int finalJ = j;

            textView.setOnClickListener(view -> {
                if (i == 1) {
                    b.regionTv.setText(items.get(finalJ));
                    selectedRegionId = finalJ;
                } else {
                    b.sortedByTv.setText(items.get(finalJ));
                    selectedSortType = finalJ;
                }
                dialog.dismiss();
            });

        }

        setPadding(root, 0, 0, 0, navigationBarHeight);

    }

}