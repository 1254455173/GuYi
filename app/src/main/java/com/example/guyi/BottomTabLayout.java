package com.example.guyi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

/**
 * Created by 陈 on 2020/7/9.
 */

public class BottomTabLayout extends BottomNavigationBar {
    /* 底部导航栏成员 */
    private final static String TAG="MainActivity";
    private BottomNavigationBar bottom_navigation_bar;
    private LinearLayout fl_content;
    private BadgeItem badge;

    private int lastSelectedPosition = 0;


    public BottomTabLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_tab_layout, this);
        // 初始化底部导航
        findView();
        initBottomNavigationBar();


    }

    /**
     * 底部导航栏
     */
    private void initBottomNavigationBar() {
        // 初始化控件时，根据当前 Activity 对象设置首选项

        if ((getContext().getClass()).equals(MainActivity.class)) {
            lastSelectedPosition = MainActivity.MAIN_ACTIVITY_TAB_ID;
        }else if(getContext().getClass().equals(ShoppingActivity.class)){
            lastSelectedPosition = ShoppingActivity.SHOPPING_ACTIVITY_TAB_ID;
        }else{
            lastSelectedPosition = SocialActivity.SOCIAL_ACTIVITY_TAB_ID;
        }

        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //设置默认颜色
        bottom_navigation_bar
                .setInActiveColor(R.color.colorInActive)//设置未选中的Item的颜色，包括图片和文字
                .setActiveColor(R.color.colorPrimary)
                .setBarBackgroundColor(R.color.colorBarBg);//设置整个控件的背景色
        //设置徽章
        badge=new BadgeItem()
//                .setBorderWidth(2)//Badge的Border(边界)宽度
//                .setBorderColor("#FF0000")//Badge的Border颜色
//                .setBackgroundColor("#9ACD32")//Badge背景颜色
//                .setGravity(Gravity.RIGHT| Gravity.TOP)//位置，默认右上角
                .setText("2")//显示的文本
//                .setTextColor("#F0F8FF")//文本颜色
//                .setAnimationDuration(2000)
//                .setHideOnSelect(true)//当选中状态时消失，非选中状态显示
        ;
        //添加选项
        bottom_navigation_bar.addItem(new BottomNavigationItem(R.mipmap.ic_home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_car, "购物车").setBadgeItem(badge))
                .addItem(new BottomNavigationItem(R.mipmap.ic_social, "社区"))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();//初始化BottomNavigationButton,所有设置需在调用该方法前完成
        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {//未选中 -> 选中
                //lastSelectedPosition = position;
                switch(position){
                    case 0:
                        // 首页
                        if(! getContext().equals(MainActivity.class)){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                            /**
                             * 这里值得优化啊
                             */
                            ((Activity)getContext()).finish();
                            ((Activity)getContext()).overridePendingTransition(0,0);
                        }
                        break;

                    case 1:
                        // 购物车
                        if(! getContext().equals(ShoppingActivity.class)){
                            Intent intent = new Intent(getContext(), ShoppingActivity.class);
                            getContext().startActivity(intent);
                            ((Activity)getContext()).finish();
                            ((Activity)getContext()).overridePendingTransition(0,0);
                        }

                        break;


                    case 2:
                        // 社区
                        if(! getContext().equals(SocialActivity.class)){
                            Intent intent = new Intent(getContext(), SocialActivity.class);
                            getContext().startActivity(intent);
                            ((Activity)getContext()).finish();
                            ((Activity)getContext()).overridePendingTransition(0,0);
                        }


                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {//选中 -> 未选中
            }

            @Override
            public void onTabReselected(int position) {//选中 -> 选中
            }
        });

    }

    public void hide(View view){
        bottom_navigation_bar.hide();
    }
    public void show(View view){
        bottom_navigation_bar.unHide();
        badge.toggle();
        Snackbar.make(bottom_navigation_bar,"你好啊",Snackbar.LENGTH_SHORT).show();
    }


    private void findView(){
        fl_content = (LinearLayout) findViewById(R.id.fl_content);
        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }
}
