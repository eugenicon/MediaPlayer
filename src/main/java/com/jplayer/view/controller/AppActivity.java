package com.jplayer.view.controller;

import com.jplayer.view.controller.fragments.AppFragment;
import com.jplayer.view.util.FxmlUtils;
import com.jplayer.view.util.SceneContent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.kairos.FragmentStatePagerAdapter;
import org.kairos.Toolbar;
import org.kairos.core.Activity;
import org.kairos.core.Fragment;
import org.kairos.core.FragmentManager;
import org.kairos.layouts.SlidingTabLayout;
import org.kairos.layouts.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;

@SceneContent("app_activity")
public class AppActivity extends Activity{
    @FXML
    Toolbar toolbar;
    @FXML
    private SlidingTabLayout tabLayout;
    @FXML
    private ViewPager viewPager;

    @Override
    public void onCreate() {
        super.onCreate();
        FxmlUtils.setupScene(this);

        toolbar.setTitle("jPlayer");
        toolbar.setDisplayHomeAsUpEnabled(true);

        setActionBar(toolbar);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.addTab(new Tab("Library",     MainActivity.class));
        pagerAdapter.addTab(new Tab("Fragment 1",  AppFragment.class));
        tabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(0);

    }


    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        @Override
        public Object instantiateItem(Pane container, int position) {
            return super.instantiateItem(container, position);
        }

        private ArrayList<Tab> tabs;

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            tabs=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            try {
                Tab tab = tabs.get(i);
                if (tab.fragmentInstance == null){
                    HashMap arguments=new HashMap();
                    arguments.put("textLabel","Fragment #"+(i+1));

                    tab.fragmentInstance = tabs.get(i).fragment.newInstance();
                    tab.fragmentInstance.setArguments(arguments);
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


    }

    private static class Tab{
        protected String title;
        protected Class<? extends Fragment> fragment;
        protected Fragment fragmentInstance;

        public Tab(String title,Class<? extends Fragment > fragment){
            this.title=title;
            this.fragment=fragment;
        }
    }

}
