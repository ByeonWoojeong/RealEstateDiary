package app.cosmos.ghrealestatediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapterToS extends FragmentStatePagerAdapter {

    FragmentManager fragmentManager;
    String[] titles = {
            "이용약관", "개인정보처리방침", "위치기반서비스"
    };

    public PagerAdapterToS(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
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
        switch (position) {
            case 0:
                FirstFragmentToS tab1 = new FirstFragmentToS();
                return tab1;
            case 1:
                SecondFragmentToS tab2 = new SecondFragmentToS();
                return tab2;
            case 2:
                ThirdFragmentToS tab3 = new ThirdFragmentToS();
                return tab3;
            default:
                return null;
        }
    }
}