package com.awkwardlydevelopedapps.unicharsheet.stat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.awkwardlydevelopedapps.unicharsheet.fragments.StatsPageFragment;

import java.util.ArrayList;
import java.util.List;

public class StatTabsAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public StatTabsAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title, int position) {
        fragmentList.add(position, fragment);
        fragmentTitleList.add(position, title);
    }

    public void removeFragment(int position) {
        fragmentList.remove(position);
        fragmentTitleList.remove(position);
    }

    public StatsPageFragment getStatPage(int i) {
        return (StatsPageFragment) fragmentList.get(i);
    }
}
