package com.jasonchio.lecture;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		titleFirstButton.setOnClickListener(this);

	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
		titleLayout.setTitle("设置");
		titleLayout.setSecondButtonVisible(View.GONE);
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.setting_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			default:
		}
	}
}
