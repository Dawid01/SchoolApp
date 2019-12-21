package com.szczepaniak.dawid.appezn.ViewPager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szczepaniak.dawid.appezn.R;

public class PageAdapter extends PagerAdapter {

    private Context context;

    public PageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewPage page = ViewPage.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(page.getLayoutResId(), collection, false);
        collection.addView(layout);

        switch (page.getLayoutResId()){

            case R.layout.post_page:
                new PostPage(context, layout);
                break;
            case R.layout.notice_page:
                new PageNotice(context, layout);
                break;
            case R.layout.plans_page:
                new PagePlans(context, layout);
                break;
            case R.layout.notifications_page:
                break;
        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ViewPage.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ViewPage customPagerEnum = ViewPage.values()[position];
        return context.getString(customPagerEnum.getTitleResId());
    }

}

