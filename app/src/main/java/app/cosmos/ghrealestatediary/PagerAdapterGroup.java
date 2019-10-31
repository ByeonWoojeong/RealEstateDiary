package app.cosmos.ghrealestatediary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapterGroup extends FragmentStatePagerAdapter {

    FragmentManager fragmentManager;
    String[] titles = { "그룹원", "공유매물" };

    public PagerAdapterGroup(FragmentManager fm) {
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
                FirstFragmentGroup tab1 = new FirstFragmentGroup();
                fragmentManager.beginTransaction().add(tab1, "FirstFragmentGroup");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return tab1;
            case 1:
                SecondFragmentGroup tab2 = new SecondFragmentGroup();
                fragmentManager.beginTransaction().add(tab2, "SecondFragmentGroup");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return tab2;
            default:
                return null;
        }
    }
}