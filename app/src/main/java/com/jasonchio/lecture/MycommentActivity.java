package com.jasonchio.lecture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MycommentActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycomment);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
	}

	@Override
	void initWidget() {

	}

	@Override
	public void onClick(View v) {

	}
}
