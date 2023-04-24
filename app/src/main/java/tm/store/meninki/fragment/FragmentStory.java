package tm.store.meninki.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentStoryBinding;
import tm.store.meninki.utils.StaticMethods;


public class FragmentStory extends Fragment {

    private FragmentStoryBinding b;

    public static FragmentStory newInstance() {
        FragmentStory fragment = new FragmentStory();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentStoryBinding.inflate(inflater);
        setBackground();
        return b.getRoot();
    }

    private void setBackground() {
        StaticMethods.setBackgroundDrawable(getContext(), b.buttonSubscribe, R.color.bg, 0, 6, false, 0);
    }
}