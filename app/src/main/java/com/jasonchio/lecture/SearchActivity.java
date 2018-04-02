package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

	private SearchView searchView;

	String response;

	EditText searchEdit;

	String searchStr;
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

				searchLectureRequest();
			/*	Intent intent=new Intent(SearchActivity.this,ResultSiftActivity.class);
				startActivity(intent);*/

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
		searchEdit = (EditText) findViewById(R.id.et_search);
	}

	@Override
	public void onClick(View v) {

	}
	private void searchLectureRequest(){

		//先从数据库查找是否有数据，按时间排列，加载前十条，没有则从服务器请求，并保存
		//showLectureInfo();
		/*
		 * 同时与服务器数据库更新时间比对，先发更新时间对比请求，有更新则保存到本地数据库*/

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					searchStr=searchEdit.getText().toString();

					response = HttpUtil.SearchLectureRequest(ConstantClass.ADDRESS, ConstantClass.SEARCH_LECTURE_REQUEST_PORT,searchStr);

					Logger.json(response);

					//lectureRequestResult= Utility.handleLectureResponse(response,getContext());

				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}



