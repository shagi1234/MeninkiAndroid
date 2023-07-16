package tm.store.meninki.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;
import tm.store.meninki.databinding.FragmentFilterAdvertisementBinding;

public class FragmentAdvertisement extends Fragment {
    FragmentAdvertisementsBinding b;

    public FragmentAdvertisement() {
        // Required empty public constructor
    }


    public static FragmentAdvertisement newInstance(String param1, String param2) {
        FragmentAdvertisement fragment = new FragmentAdvertisement();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
        b = FragmentAdvertisementsBinding.inflate(inflater, container, false);
        return b.getRoot();
    }
}