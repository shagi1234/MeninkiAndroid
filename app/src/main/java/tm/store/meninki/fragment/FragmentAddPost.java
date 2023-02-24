package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterMediaAddPost;
import tm.store.meninki.data.SelectedMedia;
import tm.store.meninki.databinding.FragmentAddPostBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;

public class FragmentAddPost extends Fragment implements OnBackPressedFragment {
    private FragmentAddPostBinding b;
    private AdapterMediaAddPost mediaAddPost;

    public static FragmentAddPost newInstance() {
        FragmentAddPost fragment = new FragmentAddPost();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.layHeader, 0, statusBarHeight, 0, 0);
        setPadding(b.root, 0, 0, 0, navigationBarHeight);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentAddPostBinding.inflate(inflater, container, false);
        setBackgrounds();
        setRecycler();
        initListeners();

        return b.getRoot();
    }

    private void initListeners() {
        b.backBtn.setOnClickListener(view -> getActivity().onBackPressed());
        b.goBasket.setOnClickListener(v -> getActivity().onBackPressed());

    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.title, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.desc, R.color.white, R.color.hover, 4, false, 1);
        setBackgroundDrawable(getContext(), b.layCountControl, R.color.hover, 0, 4, false, 0);
        setBackgroundDrawable(getContext(), b.layDataProduct, R.color.white,  R.color.hover, 4, false, 1);
    }

    private void setRecycler() {
        mediaAddPost = new AdapterMediaAddPost(getContext());
        b.recMedia.setLayoutManager(new GridLayoutManager(getContext(), 2));
        b.recMedia.setAdapter(mediaAddPost);
    }

    @Override
    public boolean onBackPressed() {
        SelectedMedia.getArrayList().clear();
        return false;
    }
}