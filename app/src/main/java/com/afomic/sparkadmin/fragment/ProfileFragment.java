package com.afomic.sparkadmin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afomic.sparkadmin.R;

public class ProfileFragment extends Fragment {
    String[] titles={"Executive","Parliament","Lecturer"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile_layout_fragment,container,false);
        AppCompatActivity activity=(AppCompatActivity) getActivity();
        Toolbar mToolbar=(Toolbar) v.findViewById(R.id.profile_toolbar);
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Profile");


        TabLayout tabs=(TabLayout) v.findViewById(R.id.profile_tab);

        ViewPager pager =(ViewPager) v.findViewById(R.id.profile_pager);
        pager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PersonViewerFragment.getInstance(position);
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        tabs.setupWithViewPager(pager);

        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
        }
        return super.onOptionsItemSelected(item);
    }
}
