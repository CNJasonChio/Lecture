package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.searchview.ICallBack;
import com.jasonchio.lecture.util.searchview.SearchView;
import com.jasonchio.lecture.util.searchview.bCallBack;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;


public class SearchActivity extends BaseActivity {

	SearchView searchView;      //搜索视图

	EditText searchEdit;        //搜索条件

	String searchStr;           //搜索条件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件响应
		initEvent();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
	}

	@Override
	void initWidget() {
		searchView = (SearchView) findViewById(R.id.search_view);
		searchEdit = (EditText) findViewById(R.id.et_search);
	}

	@Override
	void initEvent() {

		searchEdit.setFocusable(true);
		searchEdit.setFocusableInTouchMode(true);
		searchEdit.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		//进行搜索
		searchView.setOnClickSearch(new ICallBack() {
			@Override
			public void SearchAciton(String string) {
				searchStr=searchEdit.getText().toString();
				Intent intent=new Intent(SearchActivity.this,ResultSiftActivity.class);
				intent.putExtra("search_key",searchStr);
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
	public void onClick(View v) {

	}
}



