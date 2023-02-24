package tm.store.meninki.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tm.store.meninki.data.FragmentPager;

import java.util.ArrayList;

public class AdapterViewPager extends FragmentPagerAdapter {

    private ArrayList<FragmentPager> fragmentList;

    public AdapterViewPager(@NonNull FragmentManager fm, ArrayList<FragmentPager> mFragment) {
        super(fm);
        this.fragmentList = mFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public Fragment accessPage(int position) {
        return fragmentList.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getTitle();
    }

    public void changePageTitle(int position,String title){
        fragmentList.get(position).setTitle(title);
    }
}
