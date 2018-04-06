package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

import es.dmoral.toasty.Toasty;

public class MyFocuseActivity extends BaseActivity implements FocuseLibraryAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	TitleLayout titleLayout;

	Button titleFirstButton;

	SwipeToLoadLayout swipeToLoadLayout;

	FocuseLibraryAdapter mAdapter;

	LibraryDB library = new LibraryDB("武汉大学图书馆");

	List<LibraryDB> librarylist = new ArrayList<>();

	ListView listView;

	String response;

	int isFocuse=1;

	Button addCancelButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_focuse);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		MyFouceseRequest();

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);

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
				librarylist.add(library);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				librarylist.add(library);
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setLoadingMore(false);
			}
		});

		autoRefresh();
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

		mAdapter = new FocuseLibraryAdapter(librarylist,MyFocuseActivity.this);

		addCancelButton=(Button)findViewById(R.id.myfocuse_add_cancel_button);
	}

	@Override
	public void onClick(View v) {

	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		LibraryDB library= librarylist.get(position);
		//	LectureDB lecture1=comment.getLecture();
		Intent intent=new Intent(MyFocuseActivity.this,LibraryDetailActivity.class);
		startActivity(intent);
	}


	public void itemClick(View v) {
		int position;

		position = (Integer) v.getTag();

		LibraryDB library= librarylist.get(position);

		switch (v.getId()){
			case R.id.myfocuse_add_cancel_button:

/*					titleSecondButton.setText("已关注");
					titleSecondButton.setTextColor(Color.argb(255,255,157,0));
					titleSecondButton.setBackgroundResource(R.drawable.button_shape_origin);*/
					isFocuse=0;
					Toasty.info(MyFocuseActivity.this,"已取消关注");
					FocuseChangeRequest();
					librarylist.remove(position);
					mAdapter.notifyDataSetChanged();
				break;
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

	private void FocuseChangeRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.AddLibraryFocusedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_FOCUSE_REQUEST_PORT,4,10,isFocuse);
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




