package ac42886.austinallergyalert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import ac42886.austinallergyalert.AllergyAlertClass;
import ac42886.austinallergyalert.CalendarActivity;
import ac42886.austinallergyalert.GraphData;
import ac42886.austinallergyalert.Notes;

/**
 * Created by Adan on 7/7/16.
 */
public class MyTabHostProvider extends TabHostProvider
{
    private Tab homeTab;
    private Tab calendarTab;
    private Tab graphDataTab;
    private Tab notesTab;

    private TabView tabView;
    private GradientDrawable gradientDrawable, transGradientDrawable;

    public MyTabHostProvider(Activity context) {
        super(context);
    }

    @Override
    public TabView getTabHost(String category)
    {
        tabView = new TabView(context);
        tabView.setOrientation(TabView.Orientation.BOTTOM);
        tabView.setBackgroundID(R.drawable.tab_background_gradient);

        gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0xFFB2DA1D, 0xFF85A315});
        gradientDrawable.setCornerRadius(0f);
        gradientDrawable.setDither(true);

        transGradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x00000000, 0x00000000});
        transGradientDrawable.setCornerRadius(0f);
        transGradientDrawable.setDither(true);

        homeTab = new Tab(context, category);
        homeTab.setIcon(R.drawable.home_sel);
        homeTab.setIconSelected(R.drawable.home_sel);
        homeTab.setBtnText("Home");
        homeTab.setBtnTextColor(Color.WHITE);
        homeTab.setSelectedBtnTextColor(Color.BLACK);
//		homeTab.setBtnColor(Color.parseColor("#00000000"));
//		homeTab.setSelectedBtnColor(Color.parseColor("#0000FF"));
        homeTab.setBtnGradient(transGradientDrawable);
        homeTab.setSelectedBtnGradient(gradientDrawable);
        homeTab.setIntent(new Intent(context, AustinAllergyAlert.class));

        calendarTab = new Tab(context, category);
        calendarTab.setIcon(R.drawable.menu_sel);
        calendarTab.setIconSelected(R.drawable.menu_sel);
        calendarTab.setBtnText("Calendar");
        calendarTab.setBtnTextColor(Color.WHITE);
        calendarTab.setSelectedBtnTextColor(Color.BLACK);
//		calendarTab.setBtnColor(Color.parseColor("#00000000"));
//		calendarTab.setSelectedBtnColor(Color.parseColor("#0000FF"));
        calendarTab.setBtnGradient(transGradientDrawable);
        calendarTab.setSelectedBtnGradient(gradientDrawable);
        calendarTab.setIntent(new Intent(context, CalendarActivity.class));

        graphDataTab = new Tab(context, category);
        graphDataTab.setIcon(R.drawable.home_sel);
        graphDataTab.setIconSelected(R.drawable.home_sel);
        graphDataTab.setBtnText("Graph");
        graphDataTab.setBtnTextColor(Color.WHITE);
        graphDataTab.setSelectedBtnTextColor(Color.BLACK);
//		graphDataTab.setBtnColor(Color.parseColor("#00000000"));
//		graphDataTab.setSelectedBtnColor(Color.parseColor("#0000FF"));
//        graphDataTab.setBtnGradient(transGradientDrawable);
        graphDataTab.setSelectedBtnGradient(gradientDrawable);
        graphDataTab.setIntent(new Intent(context, GraphData.class));

        notesTab = new Tab(context, category);
        notesTab.setIcon(R.drawable.more_sel);
        notesTab.setIconSelected(R.drawable.more_sel);
        notesTab.setBtnText("Notes");
        notesTab.setBtnTextColor(Color.WHITE);
        notesTab.setSelectedBtnTextColor(Color.BLACK);
//		notesTab.setBtnColor(Color.parseColor("#00000000"));
//		notesTab.setSelectedBtnColor(Color.parseColor("#0000FF"));
        notesTab.setBtnGradient(transGradientDrawable);
        notesTab.setSelectedBtnGradient(gradientDrawable);
        notesTab.setIntent(new Intent(context, Notes.class));

        tabView.addTab(homeTab);
        tabView.addTab(calendarTab);
        tabView.addTab(graphDataTab);
        tabView.addTab(notesTab);

        return tabView;
    }
}