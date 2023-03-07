package tm.store.meninki.interfaces;

import tm.store.meninki.data.CategoryDto;

public interface OnCategoryChecked {
    void onChecked(boolean isChecked, CategoryDto categoryDto);
}
