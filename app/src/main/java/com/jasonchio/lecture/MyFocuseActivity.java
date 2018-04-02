package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.database.LibraryDB;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyFocuseActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;
	SwipeToLoadLayout swipeToLoadLayout;
	FocuseLibraryAdapter mAdapter;

	//LibraryDB library = new LibraryDB("武汉大学图书馆");

	List<LibraryDB> librarylist = new ArrayList<>();

	ListView listView;

	String response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_focuse);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		MyFouceseRequest();

		titleFirstButton.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LibraryDB library=librarylist.get(position);
				Intent intent=new Intent(MyFocuseActivity.this,LibraryDetailActivity.class);
				startActivity(intent);
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				//librarylist.add(library);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				//librarylist.add(library);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setLoadingMore(false);
			}
		});

		//autoRefresh();
	}

	private void autoRefresh() {
		swipeToLoadLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(true);
			}
		});
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("我的关注");
		listView.setAdapter(mAdapter);
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.myfocuse_title_layout);
		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleFirstButton=titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);

		mAdapter = new FocuseLibraryAdapter(MyFocuseActivity.this,R.layout.myfoucse_listitem,librarylist);
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

	private void MyFouceseRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_PORT,4);
					Logger.json(response);
					//解析和处理服务器返回的数据
					//signinResult = Utility.handleSigninRespose(response, SigninWithPhoneActivity.this);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("连接失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}
}




