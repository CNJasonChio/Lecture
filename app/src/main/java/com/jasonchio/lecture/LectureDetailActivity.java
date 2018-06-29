package com.jasonchio.lecture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.LectureMessageDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.NetUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LectureDetailActivity extends BaseActivity implements LectureMessageAdapter.OnItemClickListener {

	TitleLayout titleLayout;        //标题栏

	Button titleFirstButton;        //标题栏的第一个按钮
	Button titleSecondButton;       //标题栏的第二个 ann

	TextView lectureTitle;          //讲座标题
	TextView lectureSource;         //讲座来源
	TextView lectureTime;           //讲座时间
	TextView lecturePlace;          //讲座地点
	TextView lectureContent;        //讲座正文
	TextView lectureOriginal;       //讲座原文链接

	int isWanted = 0;                //是否已经被收藏

	String original;                //讲座原文链接

	String source;                  //讲座来源

	long lectureId;                 //讲座 id

	DaoSession daoSession;          //数据库操作对象

	LectureDBDao mLectureDao;       //讲座表操作对象

	UserDBDao mUserDao;             //用户表操作对象

	int changeWantedResult;         //加入或取消“我的想看”操作的结果

	LectureMessageAdapter lectureMessageAdapter;

	List<LectureMessageDB> messageDBList=new ArrayList <>();

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	RecyclerView recyclerView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_detail);

		//获得上文传递的讲座 id
		Intent intent = getIntent();
		lectureId = intent.getIntExtra("lecture_id", 1);

		initMessage();
		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应时间
		initEvent();
		//初始化讲座详情
		initLectureDetail();
	}

	@Override
	void initWidget() {
		titleLayout = (TitleLayout) findViewById(R.id.lecture_detail_title_layout);
		titleFirstButton = titleLayout.getFirstButton();
		titleSecondButton = titleLayout.getSecondButton();
		lectureTitle = (TextView) findViewById(R.id.lecture_detail_title_text);
		lectureSource = (TextView) findViewById(R.id.lecture_detail_source_text);
		lectureTime = (TextView) findViewById(R.id.lecture_detail_time_text);
		lecturePlace = (TextView) findViewById(R.id.lecture_detail_place_text);
		lectureContent = (TextView) findViewById(R.id.lecture_detail_content_text);
		lectureOriginal = (TextView) findViewById(R.id.lecture_detail_original_text);

		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();

		recyclerView=(RecyclerView)findViewById(R.id.swipe_target);
		LinearLayoutManager layoutManager=new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		lectureMessageAdapter=new LectureMessageAdapter(messageDBList,LectureDetailActivity.this);
		recyclerView.setNestedScrollingEnabled(false);
		recyclerView.setAdapter(lectureMessageAdapter);
	}

	@Override
	void initEvent() {
		//设置标题栏返回按钮点击监听事件
		titleFirstButton.setOnClickListener(this);
		//设置标题栏添加我想看按钮点击监听事件
		titleSecondButton.setOnClickListener(this);
		//设置讲座详情页查看原文按钮点击监听事件
		lectureOriginal.setOnClickListener(this);
		//设置讲座详情页的发布图书馆按钮点击监听事件
		lectureSource.setOnClickListener(this);
		lectureMessageAdapter.setItemClickListener(this);
	}

	@Override
	void initView() {

		//隐藏系统标题栏
		HideSysTitle();
		titleLayout.setTitle("讲座详情");
	}

	@Override
	public void onClick(View v) {
			switch (v.getId()) {
				case R.id.title_first_button: {
					finish();
					break;
				}
				case R.id.title_second_button: {

					if (isWanted == 1) {
						titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);
						Toasty.success(LectureDetailActivity.this, "已从“我的想看”移除").show();
						isWanted = 0;
						WantedChangeRequest();
					} else {
						titleSecondButton.setBackgroundResource(R.drawable.ic_myinfo_mywanted);
						Toasty.success(LectureDetailActivity.this, "已加入“我的想看”").show();
						isWanted = 1;
						WantedChangeRequest();
					}
					break;
				}
				case R.id.lecture_detail_original_text: {
					//打开讲座原文
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(original));
					startActivity(intent);
					break;
				}
				case R.id.lecture_detail_source_text: {
					if (source.equals("暂无来源")) {
						Toasty.info(LectureDetailActivity.this, "暂无详情");
					} else {
						Intent intent = new Intent(LectureDetailActivity.this, LibraryDetailActivity.class);
						intent.putExtra("library_name", source);
						Logger.d(source);
						startActivity(intent);
					}
					break;
				}
				default:
			}

	}

	private void WantedChangeRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.AddLectureWantedRequest(ConstantClass.ADDRESS, ConstantClass.ADD_CANCEL_WANTED_REQUEST_COM, ConstantClass.userOnline, lectureId, isWanted);
					//解析和处理服务器返回的数据
					changeWantedResult = Utility.handleWantedChangeResponse(response, lectureId, mUserDao, mLectureDao, isWanted);
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

	//初始化讲座详情
	private void initLectureDetail() {
		/*
		 * 从数据库中查找
		 * */
		LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lectureId)).build().unique();

		if (lecture != null) {
			lectureTitle.setText(lecture.getLectureTitle());
			if (lecture.getLecutreSource() == null || lecture.getLecutreSource() == "") {
				lectureSource.setText("暂无来源");
			} else {
				lectureSource.setText(lecture.getLecutreSource());
			}
			lectureTime.setText(lecture.getLectureTime());
			lecturePlace.setText(lecture.getLectureLocation());
			lectureContent.setText(lecture.getLectureContent());
			source = lecture.getLecutreSource();
			original = lecture.getLectureUrl();
			isWanted = lecture.getIsWanted();
			//判断是否已经添加想看
			if (isWanted == 1) {
				titleSecondButton.setBackgroundResource(R.drawable.ic_myinfo_mywanted);
			} else {
				titleSecondButton.setBackgroundResource(R.drawable.ic_lecture_likes);
			}
		} else {
			Toasty.error(LectureDetailActivity.this, "加载讲座信息出错");
			finish();
		}
	}

	@Override
	public void onItemClick(int position) {
		Toasty.info(LectureDetailActivity.this,"you clicked"+position).show();
	}

	public void initMessage(){
		for(int i=0;i<2;i++){
			LectureMessageDB messageDB=new LectureMessageDB();
			messageDB.setMessageContent("测试测试https://nanbusuidao.com/link/C4EGvGaCKFfzaJDq?mu=1https://nanbusuidao.com/link/C4EGvGaCKFfzaJDq?mu=1https://nanbusuidao.com/link/C4EGvGaCKFfzaJDq?mu=1" +
					"https://nanbusuidao.com/link/C4EGvGaCKFfzaJDq?mu=1");
			messageDB.setMessageLikeorNot(0);
			messageDB.setMessageLikersNum(8);
			messageDB.setUserName("赵耀邦");
			if(i==1){
				messageDB.setMessageLikeorNot(1);
			}
			messageDBList.add(messageDB);
		}
	}
}