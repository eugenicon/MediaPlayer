package com.jplayer.view.util.widget;

import javafx.scene.layout.Pane;
import org.kairos.FragmentStatePagerAdapter;
import org.kairos.core.Fragment;
import org.kairos.core.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentPager extends FragmentStatePagerAdapter {

    private Pane container;
    private ArrayList<Tab> tabs;

    public void setContainer(Pane container) {
        this.container = container;
        this.container.setId(container.toString());
    }

    @Override
    public Object instantiateItem(Pane container, int position) {
        if (this.container != null){
            return super.instantiateItem(this.container, position);
        }
        return super.instantiateItem(container, position);
    }

    public FragmentPager(FragmentManager fragmentManager) {
        super(fragmentManager);
        tabs=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        try {
            Tab tab = tabs.get(i);

            if (tab.fragmentInstance == null){
                tab.fragmentInstance = tab.fragment.newInstance();
                tab.fragmentInstance.setArguments(tab.arguments);
            }

            return tab.fragmentInstance;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getPageTitle(int i) {
        return tabs.get(i).title;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    public void addTab(Tab tab){
        tabs.add(tab);
    }

    public void addTab(String tabLabel, Class<? extends Fragment> fragmentClass, HashMap arguments){
        tabs.add(new FragmentPager.Tab(tabLabel, fragmentClass, arguments));
    }

    public void addTab(String tabLabel, Class<? extends Fragment> fragmentClass){
        tabs.add(new FragmentPager.Tab(tabLabel, fragmentClass));
    }

    public static class Tab{
        protected String title;
        protected Class<? extends Fragment> fragment;
        protected Fragment fragmentInstance;
        protected HashMap arguments;

        public Tab(String title,Class<? extends Fragment > fragment){
            this.title=title;
            this.fragment=fragment;
            this.arguments = new HashMap();
        }

        public Tab(String title,Class<? extends Fragment > fragment, HashMap arguments){
            this(title, fragment);
            this.arguments = arguments;
        }
    }
}
