package com.jplayer.view.controller.fragments;


import com.jplayer.view.util.fxml.FxmlUtils;
import com.jplayer.view.util.fxml.SceneContent;
import com.jplayer.view.util.fxml.FragmentPager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.kairos.core.Fragment;

import java.util.HashMap;

@SceneContent("settings")
public class Settings extends Fragment {

    @FXML
    private VerticalSlidingTabLayout tabLayout1;
    @FXML
    private org.kairos.layouts.ViewPager viewPager1;

    @Override
    public void onCreateView(FXMLLoader fxmlLoader) {
        System.out.println("onCreateView");
        if (viewPager1 == null) {
            FxmlUtils.setupScene(fxmlLoader);

            FragmentPager pagerAdapter = new FragmentPager(getFragmentManager());
            pagerAdapter.setContainer(viewPager1);
            viewPager1.setAdapter(pagerAdapter);

            HashMap<String, Object> args = new HashMap();
            args.put("textLabel", "Parameter from Settings");

            pagerAdapter.addTab(new FragmentPager.Tab("General", AppFragment.class, args));
            pagerAdapter.addTab(new FragmentPager.Tab("LastFM",  AppFragment.class));
            tabLayout1.setViewPager(viewPager1);

            viewPager1.setCurrentItem(0);
        }
    }

}

