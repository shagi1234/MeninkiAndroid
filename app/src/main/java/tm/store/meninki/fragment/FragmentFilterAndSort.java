package tm.store.meninki.fragment;

import static tm.store.meninki.utils.Const.mainFragmentManager;
import static tm.store.meninki.utils.FragmentHelper.addFragment;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentFilterAndSortBinding;
import tm.store.meninki.utils.StaticMethods;

public class FragmentFilterAndSort extends Fragment {

    private FragmentFilterAndSortBinding b;

    public static FragmentFilterAndSort newInstance() {
        FragmentFilterAndSort fragment = new FragmentFilterAndSort();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentFilterAndSortBinding.inflate(inflater, container, false);
        setSpinners();
        initListeners();
        setBackgrounds();
        return b.getRoot();
    }

    private void initListeners() {
        b.saveButton.setOnClickListener(view -> addFragment(mainFragmentManager, R.id.fragment_container_main, FragmentNewShop.newInstance("")));
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.radio1, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.spinnner, R.color.white, R.color.neutral_dark, 4, false, 0);
        setBackgroundDrawable(getContext(), b.spinnerTwo, R.color.white, R.color.neutral_dark, 4, false, 0);
        setBackgroundDrawable(getContext(), b.radio2, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.radio3, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.maxMoney, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.minMoney, R.color.neutral_dark, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.erase, R.color.white, 0, 4, false, 0);
    }

    private void setSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnner.setAdapter(adapter);
        b.spinnerTwo.setAdapter(adapter);
    }
}