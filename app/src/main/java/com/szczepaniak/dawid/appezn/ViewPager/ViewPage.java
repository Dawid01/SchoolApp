package com.szczepaniak.dawid.appezn.ViewPager;

import com.szczepaniak.dawid.appezn.R;

public enum ViewPage {

    POST(R.string.app_name, R.layout.post_page),
    NOTICE(R.string.app_name, R.layout.notice_page),
    PLANS(R.string.app_name, R.layout.plans_page),
    NOTIFICATIONS(R.string.app_name, R.layout.notifications_page);

    private int mTitleResId;
    private int mLayoutResId;

    ViewPage(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
