package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentFilterAdvertisementBinding;
import tm.store.meninki.utils.Dialog;

public class FragmentFilterAdvertisement extends Fragment {
    FragmentFilterAdvertisementBinding b;
    BottomSheetDialog dialog;

    public FragmentFilterAdvertisement() {
        // Required empty public constructor
    }

    public static FragmentFilterAdvertisement newInstance() {
        FragmentFilterAdvertisement fragment = new FragmentFilterAdvertisement();
        Bundle args = new Bundle();

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b = FragmentFilterAdvertisementBinding.inflate(inflater, container, false);
        setBackgrounds();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.backIcon.setOnClickListener(view -> {
            if (getActivity() == null) return;
            b.backIcon.setEnabled(false);
            getActivity().onBackPressed();
            new Handler().postDelayed(() -> b.backIcon.setEnabled(true), 200);
        });

        b.sortedByLay.setOnClickListener(view -> showDialog(b.txtSort.getText().toString().trim(), new String[]{""}));

        b.regionsLay.setOnClickListener(view -> showDialog(b.txtRegion.getText().toString().trim(), new String[]{""}));
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.sortedByLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.regionsLay, R.color.white, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.initialPriceLay, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.tillPriceLay, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.clearButton, R.color.low_contrast, 0, 10, false, 0);
        setBackgroundDrawable(getContext(), b.showBtn, R.color.accent, 0, 10, false, 0);
    }

    private void showDialog(String title, String[] items) {
        if (getContext() == null) return;
        dialog = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_ads_filter);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setDialogComponents(title, items);
        dialog.show();

    }

    private void setDialogComponents(String title, String[] items) {
        LinearLayout root = dialog.findViewById(R.id.root_lay);
        TextView headerText = dialog.findViewById(R.id.title);
        TextView item1 = dialog.findViewById(R.id.item1);
        TextView item2 = dialog.findViewById(R.id.item2);
        TextView item3 = dialog.findViewById(R.id.item3);
        TextView item4 = dialog.findViewById(R.id.item4);
        TextView item5 = dialog.findViewById(R.id.item5);

        headerText.setText(title);
        if (items == null || items.length == 0) return;

//        item1.setText(items[0]);
//        item2.setText(items[2]);
//        item3.setText(items[3]);
//        item4.setText(items[4]);
//        item5.setText(items[5]);

        setPadding(root, 0, 0, 0, navigationBarHeight);

    }
}