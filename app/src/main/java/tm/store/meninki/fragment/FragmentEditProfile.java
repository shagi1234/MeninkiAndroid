package tm.store.meninki.fragment;

import static tm.store.meninki.utils.StaticMethods.navigationBarHeight;
import static tm.store.meninki.utils.StaticMethods.setBackgroundDrawable;
import static tm.store.meninki.utils.StaticMethods.setPadding;
import static tm.store.meninki.utils.StaticMethods.statusBarHeight;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import retrofit2.Call;
import tm.store.meninki.R;
import tm.store.meninki.api.RetrofitCallback;
import tm.store.meninki.api.data.UserProfile;
import tm.store.meninki.databinding.FragmentEditProfileBinding;
import tm.store.meninki.interfaces.OnBackPressedFragment;
import tm.store.meninki.shared.Account;
import tm.store.meninki.utils.StaticMethods;

public class FragmentEditProfile extends Fragment implements OnBackPressedFragment {
    private FragmentEditProfileBinding b;

    public static FragmentEditProfile newInstance() {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        setPadding(b.getRoot(), 0, statusBarHeight, 0, navigationBarHeight);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentEditProfileBinding.inflate(inflater, container, false);
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        b.btnEdit.setOnClickListener(view -> updateProfile());
        setBackgrounds();
        return b.getRoot();
    }

    private void updateProfile() {
        if (isContentEmpty()) {
            Toast.makeText(getContext(), "Please write your information", Toast.LENGTH_SHORT).show();
            return;
        }
        UserProfile u = new UserProfile();
        u.setName(b.edtFirstName.getText().toString().trim());
        u.setId(Account.newInstance(getContext()).getPrefUserUUID());
        u.setUserName(b.edtUsername.getText().toString().trim());
        u.setPhoneNumber(b.edtPhoneNum.getText().toString().trim());

        Call<Boolean> call = StaticMethods.getApiHome().updateUser(u);
        call.enqueue(new RetrofitCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                if (response) {
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isContentEmpty() {
        return b.edtFirstName.getText().toString().trim().equals("") && b.edtPhoneNum.getText().toString().trim().equals("") && b.edtUsername.getText().toString().trim().equals("");
    }

    private void setBackgrounds() {
        setBackgroundDrawable(getContext(), b.saveButton, R.color.accent, 0, 4, false, 0);
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return false;
    }
}