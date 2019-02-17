package com.djzhao.smarttool.adapter.qrcode;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.djzhao.smarttool.fragment.qrcodecard.DIYCodeFragment;
import com.djzhao.smarttool.fragment.qrcodecard.VisitingCardFragment;

/**
 * Created by djzhao on 18/05/04.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments = new Fragment[]{new VisitingCardFragment(), new DIYCodeFragment()};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
