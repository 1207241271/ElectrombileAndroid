package com.xiaoantech.electrombile.ui.ViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yangxu on 2017/3/30.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;

    public ViewPagerAdapter (List<View> views){
        this.views = views;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public int getCount() {
        if (views != null){
            return views.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager)container).addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  (view == object);
    }
}
