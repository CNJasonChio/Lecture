package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SelecteLectureCommentActivity extends BaseActivity {

	TitleLayout titleLayout;
	Button titleFirstButton;

	List<LectureDB> lecturelist=new ArrayList<>();

	ListView listView;
	LectureAdapter mAdapter;

	String response;

	DaoSession daoSession;

	UserDBDao mUserDao;

	LectureDBDao mLectureDao;

	Handler handler;

	int lectureRequestResult=-1;

	int mywantedResult=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecte_lecture_tocomment);

		initWidget();
		//初始化视图
		initView();

		showWantedLecture();

		titleFirstButton.setOnClickListener(this);


		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (mywantedResult == 0) {
							showWantedLecture();
						} else if (mywantedResult == 1) {
							Toasty.error(SelecteLectureCommentActivity.this, "数据库无更新").show();
						} else {
							Toasty.error(SelecteLectureCommentActivity.this, "服务器出错，请稍候再试").show();
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


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LectureDB lecture=lecturelist.get(position);
				Intent intent=new Intent(SelecteLectureCommentActivity.this,AddCommentActivity.class);
				intent.putExtra("lecture_id",lecture.getLectureTitle());
				startActivity(intent);
				finish();
			}
		});


	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("请选择要评论的讲座");
		listView.setAdapter(mAdapter);
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.select_lecture_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		listView=(ListView)findViewById(R.id.select_lecture_list);
		mAdapter=new LectureAdapter(SelecteLectureCommentActivity.this,R.layout.lecure_listitem,lecturelist);

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mLectureDao=daoSession.getLectureDBDao();
		mUserDao=daoSession.getUserDBDao();
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
					response = HttpUtil.MyWantedRequest(ConstantClass.ADDRESS, ConstantClass.MYWANTED_LECTURE_REQUEST_PORT,4);
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

					long lastLecureID= Utility.lastLetureinDB(mLectureDao);

					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);

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

		String[] wantedLecture = Utility.getStrings(temp);

		if(wantedLecture.length==0){
			MywantedRequest();
			return;
		}else{
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
		}
	}
}
