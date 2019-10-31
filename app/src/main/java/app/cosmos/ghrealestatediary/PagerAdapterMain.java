package app.cosmos.ghrealestatediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapterMain extends FragmentStatePagerAdapter {

    String[] titles = {
            "전체", "원룸", "투/쓰리룸", "아파트", "사무실", "주택", "상가"
    };

    public PagerAdapterMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }
}