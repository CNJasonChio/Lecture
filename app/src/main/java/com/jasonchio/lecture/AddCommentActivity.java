package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
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

public class AddCommentActivity extends BaseActivity {

	TitleLayout titleLayout;        //标题栏

	Button titleFirstButton;        //标题栏的第一个按钮

	Button titleSecondButton;       //标题栏的第二个按钮

	TextView comment_text;          //评论内容

	List <LectureDB> lectureList = new ArrayList <>();  //评论的讲座

	LectureAdapter lectureAdapter;  //讲座适配器

	String contents;                //评论内容字符串

	String response;                //添加评论请求的服务器返回数据

	ListView listView;              //listView

	long lectureID = 0;             //初始化评论对应的讲座 ID

	DaoSession daoSession;          //数据库操作对象

	LectureDBDao mLectureDao;       //讲座表操作对象

	UserDBDao mUserDao;             //用户表操作对象

	int addCommentResult;           //添加评论的服务器返回结果

	String commentTime;             //评论的时间

	Dialog addCommentLoadDialog;    //加载对话框

	Handler handler;                //Handler 对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_comment);

		//获得上文传递的要评论的讲座 ID
		Intent intent = getIntent();
		lectureID = intent.getIntExtra("lecture_id", 1);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//显示要评论的讲座信息
		showLecture();
		//初始化事件
		initEvent();
	}

	@Override
	void initView() {
		//BaseActivity方法，隐藏系统标题栏
		HideSysTitle();
		titleLayout.setTitle("发表我的评论");
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_send_comment);
	}

	@Override
	void initWidget() {
		titleLayout = (TitleLayout) findViewById(R.id.add_comment_title_layout);
		titleFirstButton = titleLayout.getFirstButton();
		titleSecondButton = titleLayout.getSecondButton();
		comment_text = (TextView) findViewById(R.id.comment_edit);
		listView = (ListView) findViewById(R.id.comment_lecture_selected_list);
		lectureAdapter = new LectureAdapter(AddCommentActivity.this,lectureList);
		listView.setAdapter(lectureAdapter);
		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {

		titleFirstButton.setOnClickListener(this);
		titleSecondButton.setOnClickListener(this);

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (addCommentResult == 0) {
							DialogUtils.closeDialog(addCommentLoadDialog);
							Toasty.success(AddCommentActivity.this, "发表成功").show();
							finish();
						} else if (addCommentResult == 1) {
							DialogUtils.closeDialog(addCommentLoadDialog);
							Toasty.error(AddCommentActivity.this, "发表失败，请稍候再试").show();
						} else {
							DialogUtils.closeDialog(addCommentLoadDialog);
							Toasty.error(AddCommentActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_first_button: {
				finish();
				break;
			}
			case R.id.title_second_button: {
				commentTime = Utility.getNowTime();
				AddCommentRequest();
				break;
			}
			default:
		}
	}

	//添加评论请求
	private void AddCommentRequest() {

		//显示加载对话框
		addCommentLoadDialog= DialogUtils.createLoadingDialog(AddCommentActivity.this,"正在发表评论");
		//获得评论的内容
		contents = comment_text.getText().toString();

		//创建新线程向服务器发送添加讲座请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					response = HttpUtil.AddCommentRequest(ConstantClass.ADDRESS, ConstantClass.ADD_COMMENT_COM, contents, ConstantClass.userOnline, lectureID, commentTime);
					//解析和处理服务器返回的数据
					addCommentResult = Utility.handleAddCommentResponse(response, mUserDao);
					//处理结果
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

	//显示要评论的讲座
	private void showLecture() {
		LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(lectureID)).build().unique();
		lectureList.add(lecture);
	}
}
