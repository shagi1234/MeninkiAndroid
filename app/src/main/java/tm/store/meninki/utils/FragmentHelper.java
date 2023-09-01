package tm.store.meninki.utils;


import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

import tm.store.meninki.R;
import tm.store.meninki.activity.ActivityMain;


public class FragmentHelper {

    public static boolean isAddedFragmentToFeed = false;

    public static void addFragmentWithoutAnim(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {

        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());

        String backStateName = fragment.getClass().getName();
        FragmentManager manager = fragmentManager;
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(containerViewId, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
            manager.executePendingTransactions();
        }
    }

    public static void addFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {

        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());

        new Handler().postDelayed(() -> {
            String backStateName = fragment.getClass().getName();

            FragmentManager manager = fragmentManager;

            FragmentTransaction ft = manager.beginTransaction();
            ft.setCustomAnimations(R.anim.enter_anim, 0, 0, R.anim.pop_exit_anim);
            ft.add(containerViewId, fragment, backStateName);
            ft.addToBackStack(backStateName);

            ft.commit();
        },100);
    }

    public static void addFragmentNoAnim(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());

        FragmentManager manager = fragmentManager;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(containerViewId, fragment, backStateName);
        ft.addToBackStack(backStateName);

        ft.commit();
    }

    public static void addFragmentTag(FragmentManager fragmentManager, int containerViewId, Fragment fragment, String backStateName) {

        FragmentManager manager = fragmentManager;
        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());


        FragmentTransaction ft = manager.beginTransaction();
        ft.add(containerViewId, fragment, backStateName);
        ft.addToBackStack(backStateName);

        ft.commit();
    }

    public static void replaceFragment(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerViewId, fragment);
        ft.commit();
    }

    public static void addFragmentWithAnim(FragmentManager fragmentManager, int containerViewId, Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = fragmentManager;
        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());


        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.open, 0, 0, R.anim.close);
        ft.add(containerViewId, fragment, backStateName);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    public static class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }

    public static void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }

    public static void hideAdd(Fragment fragment, String tagFragmentName, FragmentManager mFragmentManager, int frame) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        StaticMethods.hideSoftKeyboard(ActivityMain.getInstance());

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            if (fragmentTemp.isAdded()) {
                fragmentTransaction.show(fragmentTemp);
            } else {
                fragmentTransaction.add(frame, fragmentTemp, tagFragmentName);
            }
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitAllowingStateLoss();

    }

}
