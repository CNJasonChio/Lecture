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
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

import static com.orhanobut.logger.Logger.addLogAdapter;

public class MainPageActivity extends BaseActivity {

	public FragmentTabHost fragmentTabHost;     //FragmentTabHost对象

	private String[] TabTags = {"首页", "动态", "我的"};

	private Integer[] ImgTab = {R.layout.tab_main_home, R.layout.tab_main_discovery, R.layout.tab_main_me};

	private Class[] ClassTab = {HomeFragment.class, DiscoveryFragment.class, MeFragment.class};

	private Integer[] StyleTab = {R.color.white, R.color.white, R.color.white, R.color.white};

	int myinfoRequestResult;        //用户信息请求结果

	int recommentOrderResult;       //推荐讲座请求结果

	DaoSession daoSession;          //数据库操作对象

	UserDBDao mUserDao;             //用户表操作对象

	LectureDBDao mLectureDao;       //讲座表操作对象

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化 Logger
		addLogAdapter(new AndroidLogAdapter());
		setContentView(R.layout.tabmaintabs);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//请求推荐的讲座
		RecommentLectureRequest();
		//请求用户信息
		MyinfoRequest();
		//初始化事件响应
		initEvent();
		//初始化导航栏值
		initValue();
		//设置监听器
		setLinstener();
		//填充数据
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
		daoSession=((MyApplication)getApplication()).getDaoSession();
		mUserDao=daoSession.getUserDBDao();
		mLectureDao=daoSession.getLectureDBDao();
	}

	@Override
	void initEvent() {

	}

	@Override
	public void onClick(View v) {

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

	private void RecommentLectureRequest(){

		Logger.d("开始获取推荐的讲座");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					Logger.d("RecommentLectureRequest()");

					String recommentResponse = HttpUtil.RecommentRequest(ConstantClass.ADDRESS, ConstantClass.RECOMMENT_COM, ConstantClass.userOnline);

					recommentOrderResult=Utility.handleRecommentLectureResponse(recommentResponse,mUserDao);

				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("解析失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}
}
