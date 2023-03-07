package tm.store.meninki.interfaces;

import tm.store.meninki.api.data.UserProfile;

public interface OnShopChecked {
    void onShopChecked(boolean isChecked, UserProfile shop);
}
