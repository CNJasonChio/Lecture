package com.jasonchio.lecture;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.hjm.bottomtabbar.BottomTabBar;

public class MainPageActivity extends AppCompatActivity {

	BottomTabBar mBottomTabBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainpage);

		//隐藏自带标题栏
		if(Build.VERSION.SDK_INT>=21){
			View decorView=getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar=getSupportActionBar();
		if(actionBar!=null){
			actionBar.hide();
		}


		mBottomTabBar = (BottomTabBar) findViewById(R.id.mainpage_bottom_tab_bar);
		mBottomTabBar.init(getSupportFragmentManager())
				.setImgSize(54,54)
				.setFontSize(10)
				.setTabPadding(5,0,5)
				.isShowDivider(true)
				.setDividerHeight(5)
				.setTabBarBackgroundColor(Color.argb(255,229,229,229))
				.setCurrentTab(0)
				.setChangeColor(Color.argb(255,250,157,37),Color.BLACK)
				.addTabItem("首页", R.drawable.ic_bottom_home_selected,R.drawable.ic_bottom_home, HomeFragment.class)
				.addTabItem("发现", R.drawable.ic_bottom_discover_selected,R.drawable.ic_bottom_discover, DiscoverFragment.class)
				.addTabItem("我的", R.drawable.ic_bottom_me_selected,R.drawable.ic_bottom_me, MeFragment.class);

	}

	private void replaceFragment(Fragment fragment) {
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		transaction.commit();

	}
}

