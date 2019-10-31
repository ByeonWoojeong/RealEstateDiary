package app.cosmos.ghrealestatediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapterMyDiary extends FragmentStatePagerAdapter {

    FragmentManager fragmentManager;
    String[] titles = {
            "기본설정", "그룹관리", "기타"
    };

    public PagerAdapterMyDiary(FragmentManager fm) {
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
                FirstFragmentMyDiary tab1 = new FirstFragmentMyDiary();
                fragmentManager.beginTransaction().add(tab1, "FirstFragmentMyDiary");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return tab1;
            case 1:
                ThirdFragmentMyDiary tab3 = new ThirdFragmentMyDiary();
                return tab3;
            case 2:
                SecondFragmentMyDiary tab2 = new SecondFragmentMyDiary();
                fragmentManager.beginTransaction().add(tab2, "SecondFragmentMyDiary");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return tab2;
            default:
                return null;
        }
    }
}