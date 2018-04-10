package com.jasonchio.lecture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class LectureDetailActivity extends BaseActivity {

	TitleLayout titleLayout;

	Button titleFirstButton;
	Button titleSecondButton;

	TextView lectureTitle;
	TextView lectureSource;
	TextView lectureTime;
	TextView lecturePlace;
	TextView lectureContent;
	TextView lectureOriginal;

	int isWanted =0;

	String response;

	String original;

	String source;

	int lectureId;

	DaoSession daoSession;

	LectureDBDao mLectureDao;

	int changeWantedResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_detail);

		Intent intent=getIntent();
		lectureId=intent.getIntExtra("lecture_id",1);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		initLectureDetail(lectureId);

		//设置标题栏返回按钮点击监听事件
		titleFirstButton.setOnClickListener(this);
		//设置标题栏添加我想看按钮点击监听事件
		titleSecondButton.setOnClickListener(this);
		//设置讲座详情页查看原文按钮点击监听事件
		lectureOriginal.setOnClickListener(this);
		//设置讲座详情页的发布图书馆按钮点击监听事件
		lectureSource.setOnClickListener(this);
	}

	protected void initWidget(){
		titleLayout=(TitleLayout)findViewById(R.id.lecture_detail_title_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();
		lectureTitle=(TextView)findViewById(R.id.lecture_detail_title_text) ;
		lectureSource=(TextView)findViewById(R.id.lecture_detail_source_text) ;
		lectureTime=(TextView)findViewById(R.id.lecture_detail_time_text) ;
		lecturePlace=(TextView)findViewById(R.id.lecture_detail_place_text) ;
		lectureContent=(TextView)findViewById(R.id.lecture_detail_content_text) ;
		lectureOriginal=(TextView)findViewById(R.id.lecture_detail_original_text) ;

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mLectureDao=daoSession.getLectureDBDao();
	}
	protected void initView(){

		//隐藏系统标题栏
		HideSysTitle();


		titleLayout.setTitle("讲座详情");

		//判断是否已经添加想看
		if(isWanted==1){
			titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes_selected);
		}else{
			titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:{
				finish();
				break;
			}
			case R.id.title_second_button:{
				if(isWanted==1){
					titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);

					Toast.makeText(LectureDetailActivity.this,"已从“我的想看”移除",Toast.LENGTH_SHORT).show();
					isWanted =0;
					WantedChangeRequest();
				}else{
					titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes_selected);
					Toast.makeText(LectureDetailActivity.this,"已加入“我的想看”",Toast.LENGTH_SHORT).show();
					isWanted =1;
					WantedChangeRequest();
				}
				break;
			}
			case R.id.lecture_detail_original_text:{
				Intent intent=new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(original));
				startActivity(intent);
				break;
			}
			case R.id.lecture_detail_source_text:{
				if(source==null){
					Toasty.info(LectureDetailActivity.this,"暂无详情");
				}else{
					Intent intent=new Intent(LectureDetailActivity.this,LibraryDetailActivity.class);
					intent.putExtra("library_id",source);
					startActivity(intent);
				}
				break;
			}
			default:
		}
	}

	private void WantedChangeRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.AddLectureWantedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_WANTED_REQUEST_PORT,4,42,isWanted);
					Logger.json(response);
					//解析和处理服务器返回的数据
					changeWantedResult = Utility.handleCommonResponse(response);
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

	private void initLectureDetail(long lecutureId){
		/*
		* 从数据库中查找
		* */
		LectureDB lecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lecutureId)).build().unique();

		if(lecture!=null){
			lectureTitle.setText(lecture.getLectureTitle());
			lectureSource.setText(lecture.getLecutreSource());
			lectureTime.setText(lecture.getLectureTime());
			lecturePlace.setText(lecture.getLectureLocation());
			lectureContent.setText(lecture.getLectureContent());
			source=lecture.getLecutreSource();
			original=lecture.getLectureUrl();
		} else{
			Toasty.error(LectureDetailActivity.this,"加载讲座信息出错");
			finish();
		}

	}
}