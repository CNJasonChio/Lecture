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

		initEvent();

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
		mAdapter=new LectureAdapter(SelecteLectureCommentActivity.this,lecturelist);

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mLectureDao=daoSession.getLectureDBDao();
		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {
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
						} else if(mywantedResult==3){
							Toasty.info(SelecteLectureCommentActivity.this, "仅能评论“我的想看”里的讲座,\n先去找找想看的讲座吧！").show();
						}
						else {
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
				Intent intent=new Intent(SelecteLectureCommentActivity.this,AddDynamicsActivity.class);
				intent.putExtra("lecture_id",(int) lecture.getLectureId());
				startActivity(intent);
				finish();
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

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID= Utility.lastLetureinDB(mLectureDao);

					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM,  ConstantClass.userOnline,lastLecureID,ConstantClass.REQUEST_FIRST);

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
			Toasty.error(SelecteLectureCommentActivity.this,"解析用户想看的讲座失败，请联系开发者").show();
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
