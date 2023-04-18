package tm.store.meninki.fragment;
/* todo hemmme null gelyan yeri duzedildi contructorlar ayryldy shahruh*/

import static tm.store.meninki.utils.StaticMethods.getHeightNavIndicator;
import static tm.store.meninki.utils.StaticMethods.getWindowWidth;
import static tm.store.meninki.utils.StaticMethods.hasNavBar;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setMarginWithAnim;
import static tm.store.meninki.utils.StaticMethods.setNavBarIconsBlack;
import static tm.store.meninki.utils.StaticMethods.slidingX;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterCountry;
import tm.store.meninki.data.CountryDto;
import tm.store.meninki.databinding.FragmentCountryCodeBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.utils.CountryInfo;
import tm.store.meninki.utils.KeyboardHeightProvider;
import tm.store.meninki.utils.StaticMethods;


public class FragmentCountryCode extends Fragment implements OnBackPressedFragment, KeyboardHeightProvider.KeyboardHeightListener {
    private FragmentCountryCodeBinding b;
    private AdapterCountry adapter;
    private KeyboardHeightProvider keyboardHeightProvider;
    private SlidrInterface slidrInterface;
    private int type;
    public static final int TYPE_LANGUAGE = 0;
    public static final int TYPE_COUNTRY_CODE = 1;
    private ArrayList<CountryDto> temp = new ArrayList<>();
    private ArrayList<CountryDto> lang = new ArrayList<>();

    public static FragmentCountryCode newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        FragmentCountryCode fragment = new FragmentCountryCode();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        keyboardHeightProvider.start();

        StaticMethods.setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);

        setNavBarIconsBlack(getActivity(), getContext());

        if (slidrInterface == null && getView() != null)
            slidrInterface = Slidr.replace(getView().findViewById(
                    R.id.frame_layout_country_code), new SlidrConfig.Builder().position(SlidrPosition.LEFT).build());

    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null) return;
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
        lang.add(new CountryDto("Turkmen", "+993"));
        lang.add(new CountryDto("Russian", "+7"));
        lang.add(new CountryDto("English", "+1"));

        keyboardHeightProvider = new KeyboardHeightProvider(getContext(), getActivity().getWindowManager(), getActivity().getWindow().getDecorView(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b = FragmentCountryCodeBinding.inflate(inflater, container, false);

        setProgress(false);

        checkUI();

        setSwipeLayout();

        initClick();

        search();

        return b.getRoot();

    }

    private void checkUI() {
        if (type == TYPE_LANGUAGE) {
            setRecycler(lang);
            b.btnSearch.setVisibility(View.GONE);
        } else {
            new Handler().postDelayed(() -> setRecycler(CountryInfo.getCountries()), 200);
            b.btnSearch.setVisibility(View.VISIBLE);
        }

    }

    private void setProgress(boolean check) {
        if (check) {
            b.layRec.setVisibility(View.VISIBLE);
            b.progressBar.setVisibility(View.GONE);
        } else {
            b.progressBar.setVisibility(View.VISIBLE);
            b.layRec.setVisibility(View.GONE);
        }
    }


    public void setSwipeLayout() {
        if (getActivity() == null) return;
        LinearLayout.LayoutParams swipePar = (LinearLayout.LayoutParams) b.swipeLayout.getLayoutParams();
        swipePar.width = getWindowWidth(getActivity()) * 2;
        b.swipeLayout.setLayoutParams(swipePar);

        ConstraintLayout.LayoutParams page1P = (ConstraintLayout.LayoutParams) b.page1.getLayoutParams();
        page1P.width = getWindowWidth(getActivity());
        b.page1.setLayoutParams(page1P);

        ConstraintLayout.LayoutParams page2P = (ConstraintLayout.LayoutParams) b.page2.getLayoutParams();
        page2P.width = getWindowWidth(getActivity());
        b.page2.setLayoutParams(page2P);


    }

    private void search() {
        b.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.edtSearch.length() >= 1) {
                    b.btnClear.setVisibility(View.VISIBLE);
                } else {
                    b.btnClear.setVisibility(View.GONE);
                }

                temp.clear();
                if (getActivity() == null) return;

                for (int j = 0; j < CountryInfo.getCountries().size(); j++) {
                    if (CountryInfo.getCountries().get(j).getName().toLowerCase().contains(charSequence.toString().trim().toLowerCase()) || CountryInfo.getCountries().get(j).getCode().toLowerCase().contains(charSequence.toString().trim().toLowerCase())) {
                        temp.add(CountryInfo.getCountries().get(j));
                    }
                    Log.e("TAG", "onTextChanged: " + temp.size());
                }
                setRecycler(temp);
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        b.btnBackCountrySearch.setOnClickListener(v -> {
            slidingX(b.swipeLayout, 0);
            slidrInterface.unlock();
            hideSoftKeyboard(getActivity());
        });
    }

    private void setRecycler(ArrayList<CountryDto> temp) {
        if (temp.size() != 0 && type != TYPE_LANGUAGE) {
            b.sticky.setVisibility(View.VISIBLE);
            b.sticky.setText(temp.get(0).getName().toUpperCase().substring(0, 1));
        } else {
            b.sticky.setVisibility(View.INVISIBLE);
        }

        adapter = new AdapterCountry(getContext(), temp, getActivity(), type);
        b.recCountries.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recCountries.setAdapter(adapter);

        if (type == TYPE_LANGUAGE) {
            setProgress(true);
            return;
        }

        b.recCountries.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) b.recCountries.getLayoutManager();
                if (lm == null) return;

                int p = lm.findFirstCompletelyVisibleItemPosition();
                if (p != 0) {
                    if (temp.size() == 0) {
                        b.sticky.setVisibility(View.GONE);
                    } else {
                        if (p - 1 < temp.size())
                            b.sticky.setText(temp.get(p - 1).getName().toUpperCase().substring(0, 1));
                    }

                }
            }
        });
        setProgress(true);
    }

    private void initClick() {
        if (getActivity() == null) return;
        b.btnSearch.setOnClickListener(view -> {
            slidrInterface.lock();
            slidingX(b.swipeLayout, -getWindowWidth(getActivity()));

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            b.edtSearch.requestFocus();


        });
        b.btnClear.setOnClickListener(view -> b.edtSearch.setText("")
        );
        b.btnBack.setOnClickListener(v -> {
            hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        });
    }

    @Override
    public boolean onBackPressed() {
        hideSoftKeyboard(getActivity());
        return false;
    }

    @Override
    public void onKeyboardHeightChanged(int height, boolean isLandscape) {
        if (getContext() == null) return;
        LinearLayout.MarginLayoutParams layout = (LinearLayout.MarginLayoutParams) b.recCountries.getLayoutParams();

        if (height > 0) {
            if (navigationBarHeight > 0) {
                setMarginWithAnim(b.recCountries, layout.bottomMargin, height);
            } else if (hasNavBar(getContext())) {
                setMarginWithAnim(b.recCountries, layout.bottomMargin, height + getHeightNavIndicator(getContext()));
            } else {
                setMarginWithAnim(b.recCountries, layout.bottomMargin, height);
            }
        } else {
            setMarginWithAnim(b.recCountries, layout.bottomMargin, height);
        }

    }

}