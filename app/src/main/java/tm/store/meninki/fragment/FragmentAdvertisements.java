package tm.store.meninki.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentAdvertisementsBinding;

public class FragmentAdvertisements extends Fragment {
    private FragmentAdvertisementsBinding b;

    public static FragmentAdvertisements newInstance() {
        FragmentAdvertisements fragment = new FragmentAdvertisements();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAdvertisementsBinding.inflate(inflater, container, false);
        gatAllAdvertisements();
        return b.getRoot();
    }

    private void gatAllAdvertisements() {

    }
}