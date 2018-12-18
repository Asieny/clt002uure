// Generated code from Butter Knife. Do not modify!
package com.guangxi.culturecloud.activitys;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LibraryActivity$$ViewInjector<T extends com.guangxi.culturecloud.activitys.LibraryActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689635, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131689635, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131689614, "field 'ivBack'");
    target.ivBack = finder.castView(view, 2131689614, "field 'ivBack'");
    view = finder.findRequiredView(source, 2131689741, "field 'imgLibrary'");
    target.imgLibrary = finder.castView(view, 2131689741, "field 'imgLibrary'");
    view = finder.findRequiredView(source, 2131689694, "field 'mTabLayout'");
    target.mTabLayout = finder.castView(view, 2131689694, "field 'mTabLayout'");
    view = finder.findRequiredView(source, 2131689695, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131689695, "field 'mViewPager'");
  }

  @Override public void reset(T target) {
    target.mTitle = null;
    target.ivBack = null;
    target.imgLibrary = null;
    target.mTabLayout = null;
    target.mViewPager = null;
  }
}
