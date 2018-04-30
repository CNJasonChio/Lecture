package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.LibraryDB;
import com.jasonchio.lecture.greendao.LibraryDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MywantedActivity extends BaseActivity {

	SwipeToLoadLayout swipeToLoadLayout;
	TitleLayout titleLayout;
	Button titleFirstButton;
	LectureAdapter mAdapter;

	ListView listView;


	String response;

	int mywantedResult=-1;

	int lectureRequestResult=-1;

	List<LectureDB> lecturelist=new ArrayList<>();

	DaoSession daoSession;

	UserDBDao mUserDao;

	LectureDBDao mLectureDao;

	Handler handler;

	Dialog mywantedLoadDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywanted);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initEvent();

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mywantedLoadDialog= DialogUtils.createLoadingDialog(MywantedActivity.this,"正在加载");
				showWantedLecture();
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

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
		titleLayout.setTitle("我的想看");
		listView.setAdapter(mAdapter);
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)findViewById(R.id.mywanted_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);

		mAdapter = new LectureAdapter(MywantedActivity.this, listView,lecturelist,mLectureDao);

		daoSession=((MyApplication)getApplication()).getDaoSession();

		mUserDao=daoSession.getUserDBDao();
		mLectureDao=daoSession.getLectureDBDao();
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LectureDB lecture=lecturelist.get(position);
				Intent intent=new Intent(MywantedActivity.this,LectureDetailActivity.class);
				intent.putExtra("lecture_id",(int) lecture.getLectureId());
				startActivity(intent);

			}
		});

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (mywantedResult == 0) {
							showWantedLecture();
						} else if (mywantedResult == 1) {
							Toasty.error(MywantedActivity.this, "数据库无更新").show();
						} else if(mywantedResult==3){
							Toasty.info(MywantedActivity.this,"还没有想看的讲座哟，快去讲座推荐看一看吧！").show();
							swipeToLoadLayout.setRefreshing(false);
						}else {
							Toasty.error(MywantedActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if(lectureRequestResult==0){
							showWantedLecture();
						}
					default:
						break;
				}
				return true;
			}
		});
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

	private void MywantedRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					//response = HttpUtil.MyWantedRequest(ConstantClass.ADDRESS, ConstantClass.MYWANTED_LECTURE_REQUEST_PORT,ConstantClass.userOnline);
					response = HttpUtil.MyWantedRequest(ConstantClass.ADDRESS, ConstantClass.MYWANTED_LECTURE_REQUEST_COM,ConstantClass.userOnline);
					Logger.json(response);
					//解析和处理服务器返回的数据
					mywantedResult= Utility.handleWantedLectureResponse(response,mUserDao);

					handler.sendEmptyMessage(1);
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

	private void LectureRequest() {

		//先从数据库查找是否有数据，按时间排列，加载前十条，没有则从服务器请求，并保存
		//showLectureInfo();
		/*
		 * 同时与服务器数据库更新时间比对，先发更新时间对比请求，有更新则保存到本地数据库*/

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID=Utility.lastLetureinDB(mLectureDao);

					//String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline ,lastLecureID);

					lectureRequestResult=Utility.handleLectureResponse(lectureresponse,mLectureDao);

					handler.sendEmptyMessage(2);
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

	private void showWantedLecture() {

		String temp=Utility.getUserWanted(ConstantClass.userOnline,mUserDao);
		if(temp==null || temp.length()==0){
			MywantedRequest();
			return;
		}
		String[] wantedLecture = Utility.getStrings(temp);
		if(wantedLecture.length==0){
			Toasty.error(MywantedActivity.this,"解析用户想看的讲座失败，请联系开发者").show();
			return;
		}else{
			if (!lecturelist.isEmpty()) {
				lecturelist.removeAll(lecturelist);
			}
			for(int i=0;i<wantedLecture.length;i++){
				LectureDB lecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(wantedLecture[i])).build().unique();

				if(lecture==null){
					LectureRequest();
					return;
				}else{
					lecturelist.add(lecture);
				}
			}

			listView.setSelection(0);

			mAdapter.notifyDataSetChanged();

			DialogUtils.closeDialog(mywantedLoadDialog);
		}
	}
}
