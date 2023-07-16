package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.getHeightNavIndicator;
import static tm.store.meninki.utils.StaticMethods.hasNavBar;
import static tm.store.meninki.utils.StaticMethods.hideSoftKeyboard;
import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setMarginWithAnim;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.listener.SimplePopupAdapter;
import com.aghajari.emojiview.samsungprovider.AXSamsungEmojiProvider;
import com.aghajari.emojiview.view.AXEmojiView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.adapter.AdapterComment;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.DtoComment;
import tm.store.meninki.api.request.RequestAddComment;
import tm.store.meninki.databinding.FragmentCommentsBinding;
import tm.store.meninki.utils.KeyboardHeightProvider;
import tm.store.meninki.utils.StaticMethods;

public class FragmentComments extends Fragment implements KeyboardHeightProvider.KeyboardHeightListener {
    private FragmentCommentsBinding b;
    private boolean isEmojiOpen;
    private ArrayList<DtoComment> comments;
    private String id;
    private AdapterComment adapterComment;

    public static FragmentComments newInstance(ArrayList<DtoComment> comment, String id) {
        FragmentComments fragment = new FragmentComments();
        Bundle args = new Bundle();
        args.putString("comment", new Gson().toJson(comment));
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("id");
            Type type = new TypeToken<ArrayList<DtoComment>>() {
            }.getType();
            comments = new Gson().fromJson(getArguments().getString("comment"), type);
            if (comments == null) {
                comments = new ArrayList<>();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentCommentsBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setEmojiKeyboard();
        setRecycler();
        initListeners();
        return b.getRoot();
    }

    private void initListeners() {
        b.iconSend.setOnClickListener(view -> {
            b.iconSend.setEnabled(false);
            new Handler().postDelayed(() -> b.iconSend.setEnabled(true), 200);
            clearInput();
            addComment();
        });
    }

    private void clearInput() {
        b.edtComment.setText("");
        hideSoftKeyboard(getActivity());
        b.edtComment.clearFocus();
    }

    private void addComment() {
        RequestAddComment addComment = new RequestAddComment();
        addComment.setText(b.edtComment.getText().toString().trim());
        addComment.setProductBaseId(id);
        Call<DtoComment> call = StaticMethods.getApiHome().addComment(addComment);
        call.enqueue(new RetrofitCallback<DtoComment>() {
            @Override
            public void onResponse(DtoComment response) {
                if (response == null) {
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.error_while_sending_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                comments.add(response);
                adapterComment.notifyItemInserted(comments.size() - 1);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecycler() {
        adapterComment = new AdapterComment(getContext(), comments, getActivity(), b.countComments, b.recComments);
        b.recComments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        b.recComments.setAdapter(adapterComment);
    }


    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
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

        b.layout.setPopupListener(new SimplePopupAdapter() {
            @Override
            public void onDismiss() {
                super.onDismiss();
                b.smileImg.setImageResource(R.drawable.ic_emoji);
                isEmojiOpen = false;
            }

            @Override
            public void onShow() {
                super.onShow();
            }

            @Override
            public void onKeyboardOpened(int height) {
                super.onKeyboardOpened(height);
                if (getContext() == null) return;
                LinearLayout.MarginLayoutParams layout = (LinearLayout.MarginLayoutParams) b.fChatTextAndAudioOnContainer.getLayoutParams();
                if (height > 0) {
                    b.grayKeyboard.setVisibility(View.VISIBLE);
                    if (navigationBarHeight > 0) {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                    } else if (hasNavBar(getContext())) {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height + getHeightNavIndicator(getContext()));
                    } else {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                    }
                } else {
                    b.grayKeyboard.setVisibility(View.GONE);
                    setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                }
                b.smileImg.setImageResource(R.drawable.ic_emoji);
                isEmojiOpen = false;
            }

            @Override
            public void onKeyboardClosed() {
                super.onKeyboardClosed();
            }

            @Override
            public void onViewHeightChanged(int height) {
                super.onViewHeightChanged(height);
                if (getContext() == null) return;
                LinearLayout.MarginLayoutParams layout = (LinearLayout.MarginLayoutParams) b.fChatTextAndAudioOnContainer.getLayoutParams();

                if (height > 0) {
                    b.grayKeyboard.setVisibility(View.VISIBLE);
                    if (navigationBarHeight > 0) {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                    } else if (hasNavBar(getContext())) {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height + getHeightNavIndicator(getContext()));
                    } else {
                        setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                    }
                } else {
                    b.grayKeyboard.setVisibility(View.GONE);
                    setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
                }
            }
        });

    }

    @Override
    public void onKeyboardHeightChanged(int height, boolean isLandscape) {
        LinearLayout.MarginLayoutParams layout = (LinearLayout.MarginLayoutParams) b.fChatTextAndAudioOnContainer.getLayoutParams();

        if (height > 0) {
            if (navigationBarHeight > 0) {
                setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
            } else if (hasNavBar(getContext())) {
                setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height + getHeightNavIndicator(getContext()));
            } else {
                setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
            }
        } else {
            setMarginWithAnim(b.fChatTextAndAudioOnContainer, layout.bottomMargin, height);
        }
    }
}