package com.jplayer.view.util.widget;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ViewPager extends org.kairos.layouts.ViewPager {
    public void setCurrentItem(Class fragmentClass) {
        int index = ((FragmentPagerAdapter) getAdapter()).getFragmentIndex(fragmentClass);
        setCurrentItem(index);
    }

    public void setCurrentItem(Class fragmentClass, HashMap arguments) {
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) this.getAdapter();
        FragmentPagerAdapter.Tab fragmentTab = adapter.getFragmentTab(fragmentClass);

        if (!fragmentTab.equals(FragmentPagerAdapter.Tab.EMPTY)) {
            fragmentTab.arguments = arguments;
            if (fragmentTab.fragmentInstance != null) {
                fragmentTab.fragmentInstance.setArguments(arguments);
            }
            setCurrentItem(fragmentClass);
        }
    }
}