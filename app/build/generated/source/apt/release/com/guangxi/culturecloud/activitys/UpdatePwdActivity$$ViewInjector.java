// Generated code from Butter Knife. Do not modify!
package com.guangxi.culturecloud.activitys;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class UpdatePwdActivity$$ViewInjector<T extends com.guangxi.culturecloud.activitys.UpdatePwdActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689614, "field 'ivBack'");
    target.ivBack = finder.castView(view, 2131689614, "field 'ivBack'");
    view = finder.findRequiredView(source, 2131689630, "field 'login'");
    target.login = finder.castView(view, 2131689630, "field 'login'");
    view = finder.findRequiredView(source, 2131689631, "field 'ivRefresh'");
    target.ivRefresh = finder.castView(view, 2131689631, "field 'ivRefresh'");
    view = finder.findRequiredView(source, 2131689567, "field 'title'");
    target.title = finder.castView(view, 2131689567, "field 'title'");
    view = finder.findRequiredView(source, 2131689762, "field 'oldpwd'");
    target.oldpwd = finder.castView(view, 2131689762, "field 'oldpwd'");
    view = finder.findRequiredView(source, 2131689763, "field 'pwd'");
    target.pwd = finder.castView(view, 2131689763, "field 'pwd'");
    view = finder.findRequiredView(source, 2131689764, "field 'btn'");
    target.btn = finder.castView(view, 2131689764, "field 'btn'");
  }

  @Override public void reset(T target) {
    target.ivBack = null;
    target.login = null;
    target.ivRefresh = null;
    target.title = null;
    target.oldpwd = null;
    target.pwd = null;
    target.btn = null;
  }
}
