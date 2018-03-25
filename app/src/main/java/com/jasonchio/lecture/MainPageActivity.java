package com.jasonchio.lecture;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.mob.MobSDK;

public class MainPageActivity extends BaseActivity {

	private String TAG = MainPageActivity.class.getName();

	public FragmentTabHost fragmentTabHost;

	private String[] TabTags = {"首页", "发现", "我的"};

	private Integer[] ImgTab = {R.layout.tab_main_home, R.layout.tab_main_discovery, R.layout.tab_main_me};

	private Class[] ClassTab = {HomeFragment.class, DiscoveryFragment.class, MeFragment.class};

	private Integer[] StyleTab = {R.color.white, R.color.white, R.color.white, R.color.white};


	protected void onCreate(Bundle savedInstanceState) {
		MobSDK.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmaintabs);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initValue();
		setLinstener();
		fillDate();
	}


	private void initValue() {
		InitTanView();
	}

	private void setLinstener() {
		// imv_back.setOnClickListener(this);

	}

	private void fillDate() {

	}


	private void InitTanView() {
		Bundle bundle = new Bundle();

		for (int i = 0; i < TabTags.length; i++) {
			View indicator = getIndicatorView(i);
			fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TabTags[i]).setIndicator(indicator), ClassTab[i], bundle);
		}
		fragmentTabHost.getTabWidget().setDividerDrawable(R.color.white);
	}

	private View getIndicatorView(int i) {
		View view = getLayoutInflater().inflate(ImgTab[i], null);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_back);
		linearLayout.setBackgroundResource(StyleTab[i]);
		return view;
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
		fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
	}

	@Override
	void initWidget() {
		fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	}

	@Override
	public void onClick(View v) {

	}
}
