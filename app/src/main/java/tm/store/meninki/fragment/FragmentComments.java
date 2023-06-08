package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.samsungprovider.AXSamsungEmojiProvider;
import com.aghajari.emojiview.view.AXEmojiView;

import tm.store.meninki.R;
import tm.store.meninki.databinding.FragmentCommentsBinding;

public class FragmentComments extends Fragment {
    private FragmentCommentsBinding b;
    private boolean isEmojiOpen;

    public static FragmentComments newInstance() {
        FragmentComments fragment = new FragmentComments();
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
        b = FragmentCommentsBinding.inflate(inflater, container, false);
        setEmojiKeyboard();
        return b.getRoot();
    }

    private void setEmojiKeyboard() {
        if (getContext() == null) return;
        AXEmojiManager.install(getContext(), new AXSamsungEmojiProvider(getContext()));

        AXEmojiView emojiView = new AXEmojiView(getContext());
        emojiView.setEditText(b.edtComment);
        b.layout.initPopupView(emojiView);
        b.layout.hidePopupView();
        b.smileBtn.setOnClickListener(v -> {
            if (!isEmojiOpen) {
                b.layout.show();
                b.smileImg.setImageResource(R.drawable.ic_keyboard);
                isEmojiOpen = true;
            } else {
                b.smileImg.setImageResource(R.drawable.ic_emoji);
                b.layout.hideAndOpenKeyboard();
                isEmojiOpen = false;
            }
        });

        b.grayKeyboard.setOnClickListener(v -> {
            hideSoftKeyboard(getActivity());
            b.layout.hidePopupView();
            isEmojiOpen = false;
        });

    }

}