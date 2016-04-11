package com.jplayer.view.util.widget;

public class ViewPager extends org.kairos.layouts.ViewPager {
    public void setCurrentItem(Class fragmentClass) {
        int index = ((FragmentPagerAdapter) getAdapter()).getFragmentIndex(fragmentClass);
        setCurrentItem(index);
    }
}
