package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jasonchio.lecture.util.searchview.ICallBack;
import com.jasonchio.lecture.util.searchview.SearchView;
import com.jasonchio.lecture.util.searchview.bCallBack;


public class SearchActivity extends BaseActivity {

	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		searchView.setOnClickSearch(new ICallBack() {
			@Override
			public void SearchAciton(String string) {
				Toast.makeText(SearchActivity.this,"you click search"+ string,Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(SearchActivity.this,ResultSiftActivity.class);
				startActivity(intent);

			}
		});

		// 设置点击返回按键后的操作（通过回调接口）
		searchView.setOnClickBack(new bCallBack() {
			@Override
			public void BackAciton() {
				finish();
			}
		});

	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
	}

	@Override
	void initWidget() {
		searchView = (SearchView) findViewById(R.id.search_view);
	}

	@Override
	public void onClick(View v) {

	}
}



