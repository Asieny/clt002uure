// Generated code from Butter Knife. Do not modify!
package com.guangxi.culturecloud.activitys;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ChooseCityActivity$$ViewInjector<T extends com.guangxi.culturecloud.activitys.ChooseCityActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689643, "field 'searchContentEt'");
    target.searchContentEt = finder.castView(view, 2131689643, "field 'searchContentEt'");
    view = finder.findRequiredView(source, 2131689642, "field 'mToolbar'");
    target.mToolbar = finder.castView(view, 2131689642, "field 'mToolbar'");
    view = finder.findRequiredView(source, 2131689644, "field 'totalCityLv'");
    target.totalCityLv = finder.castView(view, 2131689644, "field 'totalCityLv'");
    view = finder.findRequiredView(source, 2131689645, "field 'lettersLv'");
    target.lettersLv = finder.castView(view, 2131689645, "field 'lettersLv'");
    view = finder.findRequiredView(source, 2131689646, "field 'searchCityLv'");
    target.searchCityLv = finder.castView(view, 2131689646, "field 'searchCityLv'");
    view = finder.findRequiredView(source, 2131689647, "field 'noSearchDataTv'");
    target.noSearchDataTv = finder.castView(view, 2131689647, "field 'noSearchDataTv'");
  }

  @Override public void reset(T target) {
    target.searchContentEt = null;
    target.mToolbar = null;
    target.totalCityLv = null;
    target.lettersLv = null;
    target.searchCityLv = null;
    target.noSearchDataTv = null;
  }
}
