// Generated code from Butter Knife. Do not modify!
package ac42886.austinallergyalert;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CalendarActivity$$ViewBinder<T extends ac42886.austinallergyalert.CalendarActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624035, "field 'widget'");
    target.widget = finder.castView(view, 2131624035, "field 'widget'");
  }

  @Override public void unbind(T target) {
    target.widget = null;
  }
}
