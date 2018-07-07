package com.jasonchio.lecture;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.LinearLayout;

import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class MainPageActivity extends BaseActivity{

	public FragmentTabHost fragmentTabHost;     //FragmentTabHost对象

	private String[] TabTags = {"首页", "动态", "我的"};

	private Integer[] ImgTab = {R.layout.tab_main_home, R.layout.tab_main_dynamics, R.layout.tab_main_me};

	private Class[] ClassTab = {HomeFragment.class, DynamicsFragment.class, MeFragment.class};

	private Integer[] StyleTab = {R.color.white, R.color.white, R.color.white, R.color.white};

	int myinfoRequestResult;        //用户信息请求结果

	DaoSession daoSession;          //数据库操作对象

	UserDBDao mUserDao;             //用户表操作对象

	LectureDBDao mLectureDao;       //讲座表操作对象

	long startTime = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*//初始化 Logger
		addLogAdapter(new AndroidLogAdapter());*/
		setContentView(R.layout.tabmaintabs);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//请求用户信息
		MyinfoRequest();
		//初始化事件响应
		initEvent();
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

		Bundle bundle = new Bundle();

		for (int i = 0; i < TabTags.length; i++) {
			View indicator = getIndicatorView(i);
			fragmentTabHost.addTab(fragmentTabHost.newTabSpec(TabTags[i]).setIndicator(indicator), ClassTab[i], bundle);
		}
		fragmentTabHost.getTabWidget().setDividerDrawable(R.color.white);
	}

	@Override
	void initWidget() {
		fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		daoSession=((MyApplication)getApplication()).getDaoSession();
		mUserDao=daoSession.getUserDBDao();
		mLectureDao=daoSession.getLectureDBDao();
	}

	@Override
	void initEvent() {

	}

	private void MyinfoRequest(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String response = HttpUtil.UserInfoRequest(ConstantClass.ADDRESS, ConstantClass.MYINFO_REQUEST_COM,ConstantClass.userOnline );
					myinfoRequestResult= Utility.handleUserInfoResponse(response,mUserDao);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 重写点击返回建的事件处理，连续点击返回键两次退出APP
	 * */

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - startTime) >= 2000) {
			Toasty.info(MainPageActivity.this,"再按一次退出程序").show();
			startTime = currentTime;
		} else {
			finish();
		}
	}

}
