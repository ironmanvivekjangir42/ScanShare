package com.example.scanshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scanshare.scan.FragmentScan;
import com.example.scanshare.share.FragmentShare;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        TabLayout talentsTitles = (TabLayout) v.findViewById(R.id.titles);
        addTabs(viewPager);
        talentsTitles.setupWithViewPager(viewPager);

        return v;
    }

    private void addTabs(ViewPager viewPager) {
        FragmentHome.ViewPagerAdapter adapter = new FragmentHome.ViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new FragmentShare(), "Share");
        adapter.addFrag(new FragmentScan(), "Scan");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
