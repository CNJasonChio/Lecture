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

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	TitleLayout titleLayout;                    //标题栏

	Button titleFirstButton;                    //标题栏的第一个按钮

	LectureAdapter mAdapter;                    //讲座适配器

	ListView listView;                          //要显示的 listview

	int mywantedResult=-1;                      //请求“我的想看”结果

	int lectureRequestResult=-1;                //讲座请求结果

	List<LectureDB> lecturelist=new ArrayList<>();  //讲座列表

	DaoSession daoSession;                      //数据库操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	LectureDBDao mLectureDao;                   //讲座表操作对象

	Handler handler;                            //handler 对象

	Dialog mywantedLoadDialog;                  //加载对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mywanted);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应事件
		initEvent();
		//自动刷新
		autoRefresh();
	}

	//自动刷新
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

		mAdapter = new LectureAdapter(MywantedActivity.this,lecturelist);

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
						}else{
							DialogUtils.closeDialog(mywantedLoadDialog);
						}
					default:
						break;
				}
				return true;
			}
		});

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

	//“我的想看”请求
	private void MywantedRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.MyWantedRequest(ConstantClass.ADDRESS, ConstantClass.MYWANTED_LECTURE_REQUEST_COM,ConstantClass.userOnline);
					//解析和处理服务器返回的数据
					mywantedResult= Utility.handleWantedLectureResponse(response,mUserDao);
					//结果处理
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

	//讲座请求
	private void LectureRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取数据库中最后一条讲座的 id
					long lastLecureID=Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回数据
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline ,lastLecureID);
					//解析和处理服务器返回的数据
					lectureRequestResult=Utility.handleLectureResponse(lectureresponse,mLectureDao);
					//处理结果
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

	//显示“我的想看”
	private void showWantedLecture() {

		//从数据库中查找用户想看的讲座
		String temp=Utility.getUserWanted(ConstantClass.userOnline,mUserDao);
		//如果数据库中没有，则想服务器请求
		if(temp==null || temp.length()==0){
			MywantedRequest();
			return;
		}
		//将用户想看的讲座转换成字符串数组，方便操作
		String[] wantedLecture = Utility.getStrings(temp);
		if(wantedLecture.length==0){
			Toasty.error(MywantedActivity.this,"解析用户想看的讲座失败，请联系开发者").show();
			return;
		}else{
			//如果想看的讲座列表不为空，则清空
			if (!lecturelist.isEmpty()) {
				lecturelist.removeAll(lecturelist);
			}
			//依次将用户想看的讲座添加到 lecturelist 中
			for(int i=0;i<wantedLecture.length;i++){
				LectureDB lecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(wantedLecture[i])).build().unique();
				//如果数据库中没有对应的讲座，则向服务器请求
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
