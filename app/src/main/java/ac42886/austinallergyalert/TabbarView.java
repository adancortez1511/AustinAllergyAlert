//package ac42886.austinallergyalert;

/**
 * Created by Adan on 7/7/16.
 */

//import android.app.Activity;
//
//package com.kpbird.tabbarcontrol;
//
//import android.app.Activity;
//import android.content.Intent;
//
//import com.tabwidget.Tab;
//import com.tabwidget.TabHostProvider;
//import com.tabwidget.TabView;
//
//public class TabbarView extends TabHostProvider {
//
//    private Tab firstTab;
//    private Tab secondTab;
//    private Tab thirdTab;
//    private Tab fourTab;
//    private TabView tabView;
//    public TabbarView(Activity context) {
//        super(context);
//        // TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public TabView getTabHost(String category) {
//        tabView = new TabView(context);
//        tabView.setBackgroundID(R.drawable.tab_background_55);
//
//        firstTab = new Tab(context, "One");
//        firstTab.setIntent(new Intent(context,FirstTab.class));
//        firstTab.setIcon(R.drawable.help);
//        firstTab.setIconSelected(R.drawable.help_selected);
//
//        secondTab = new Tab(context, "Two");
//        secondTab.setIntent(new Intent(context,SecondTab.class));
//        secondTab.setIcon(R.drawable.help);
//        secondTab.setIconSelected(R.drawable.help_selected);
//
//        thirdTab = new Tab(context, "Three");
//        thirdTab.setIntent(new Intent(context,ThirdTab.class));
//        thirdTab.setIcon(R.drawable.help);
//        thirdTab.setIconSelected(R.drawable.help_selected);
//
//        fourTab = new Tab(context, "Four");
//        fourTab.setIntent(new Intent(context,FourTab.class));
//        fourTab.setIcon(R.drawable.help);
//        fourTab.setIconSelected(R.drawable.help_selected);
//
//        tabView.addTab(firstTab);
//        tabView.addTab(secondTab);
//        tabView.addTab(thirdTab);
//        tabView.addTab(fourTab);
//
//        tabView.setCurrentView(R.layout.firsttab);
//        return tabView;
//    }
//
//}