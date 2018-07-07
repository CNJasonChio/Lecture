package com.jasonchio.lecture;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.LectureMessageDB;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.LeaveMessageResult;
import com.jasonchio.lecture.gson.LectureMessageResult;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.KeyboardUtils;
import com.jasonchio.lecture.util.MD5Util;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LectureDetailActivity extends BaseActivity implements LectureMessageAdapter.OnItemClickListener, LectureMessageAdapter.OnItemLongClickListener {

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

	List <LectureMessageDB> messageDBList = new ArrayList <>();

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	RecyclerView lectureMsgRecyclerView;

	TextView leaveMessgeText;

	int isLikeOrNot = 0;

	Handler handler;                            // handler 对象

	LectureMessageDB newMessageDB;

	Dialog leaveMsgDialog;

	Dialog loadMsgDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_detail);

		//获得上文传递的讲座 id
		Intent intent = getIntent();
		lectureId = intent.getIntExtra("lecture_id", 1);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应时间
		initEvent();
		//初始化讲座详情
		initLectureDetail();

		loadMsgDialog=DialogUtils.createLoadingDialog(LectureDetailActivity.this,"正在加载");
		lectureMessageRequest();
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

		lectureMsgRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		lectureMsgRecyclerView.setLayoutManager(layoutManager);
		lectureMessageAdapter = new LectureMessageAdapter(messageDBList, LectureDetailActivity.this);
		lectureMsgRecyclerView.setNestedScrollingEnabled(false);
		lectureMsgRecyclerView.setAdapter(lectureMessageAdapter);

		leaveMessgeText = (TextView) findViewById(R.id.lecture_leave_message_text);
		leaveMessgeText.setOnClickListener(this);
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
		lectureMessageAdapter.setItemLongClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if(msg.arg1==0){
							lectureMsgRecyclerView.setAdapter(lectureMessageAdapter);
						}else{
							Toasty.info(LectureDetailActivity.this,"暂无留言").show();
						}
						DialogUtils.closeDialog(loadMsgDialog);
						break;
					case 2:
						if(msg.arg1==0){
							messageDBList.add(0, newMessageDB);
							lectureMessageAdapter.notifyItemInserted(0);
						}else{
							Toasty.error(LectureDetailActivity.this,"留言失败，请稍后再试").show();
						}
						DialogUtils.closeDialog(leaveMsgDialog);
						break;
					case 3:
						if(msg.arg1==0){
							messageDBList.remove(msg.arg1);
							lectureMessageAdapter.notifyItemRemoved(msg.arg1);
						}else{
							Toasty.error(LectureDetailActivity.this,"删除失败，请稍后再试").show();
						}
						break;
					default:
				}
				return true;
			}
		});
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
			case R.id.lecture_leave_message_text:
				leaveMessage();
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
			Toasty.error(LectureDetailActivity.this, "加载讲座信息出错").show();
			finish();
		}
	}

	//添加留言
	private void leaveMessage() {
		KeyboardUtils.showCommentEdit(LectureDetailActivity.this, leaveMessgeText, new KeyboardUtils.liveCommentResult() {
			@Override
			public void onResult(boolean confirmed, String messageContent) {
				if (confirmed) {
					leaveMsgDialog=DialogUtils.createLoadingDialog(LectureDetailActivity.this,"正在留言");
					leaveMessageRequest(messageContent);
				}
			}
		});
	}

	private void likeOrNotLike(int position) {
		LectureMessageDB messageDB = messageDBList.get(position);
		isLikeOrNot = messageDB.getMessageLikeorNot();
		if (isLikeOrNot == 0) {
			isLikeOrNot = 1;
			messageDB.setMessageLikeorNot(1);
			messageDB.setMessageLikersNum(messageDB.getMessageLikersNum() + 1);
		} else {
			isLikeOrNot = 0;
			messageDB.setMessageLikeorNot(0);
			messageDB.setMessageLikersNum(messageDB.getMessageLikersNum() - 1);
		}
		messageDBList.set(position, messageDB);
		lectureMessageAdapter.notifyItemChanged(position);

		LikeOrNotChange(messageDB.getMessageId(), isLikeOrNot);
	}

	private void leaveMessageRequest(final String messageContent) {

		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Logger.d(Utility.getNowTime());
					//获取服务器返回的结果
					String response = HttpUtil.LeaveMessageRequest(ConstantClass.ADDRESS, ConstantClass.LEAVE_MESSAGE_COM, messageContent, ConstantClass.userOnline, lectureId, Utility.getNowTime());

					Gson gson = new Gson();
					LeaveMessageResult result = gson.fromJson(response, LeaveMessageResult.class);
					int state = result.getState();
					if (state == 0) {
						UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
						newMessageDB = new LectureMessageDB();
						newMessageDB.setMessageId(result.getMessage_id());
						newMessageDB.setUserHead(userDB.getUserPhotoUrl());
						newMessageDB.setUserName(userDB.getUserName());
						newMessageDB.setMessageLikeorNot(0);
						newMessageDB.setMessageLikersNum(0);
						newMessageDB.setMessageContent(messageContent);
					}
					Message message=new Message();
					message.what=2;
					message.arg1=state;
					handler.sendMessage(message);
				} catch (IOException e) {
					Logger.d("通信失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("通信失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void onItemClick(View view, int position) {
		switch (view.getId()) {
			case R.id.lecture_message_like_image:
				likeOrNotLike(position);
				break;
		}
	}

	@Override
	public void onItemLongClick(View view, final int position) {
		switch (view.getId()) {
			case R.id.lecture_message_layout:
				AlertDialog.Builder mDialog = new AlertDialog.Builder(LectureDetailActivity.this);
				mDialog.setTitle("删除留言");
				mDialog.setMessage("确认要删除这条留言吗？");
				mDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deteleRequest(messageDBList.get(position).getMessageId(), position);
					}
				}).create().show();

				break;
		}
	}

	private void deteleRequest(final long objectID, final int position) {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的结果
					String response = HttpUtil.DeleteRequest(ConstantClass.ADDRESS, ConstantClass.DELETE_COM, objectID, ConstantClass.TYPE_MESSAGE, ConstantClass.userOnline);
					Gson gson = new Gson();
					CommonStateResult result = gson.fromJson(response, CommonStateResult.class);
					//解析和处理服务器返回的结果
					/*loginResult = Utility.handleLoginRespose(response,userPhone,mUserDao);
					//处理结果*/
					Message message=new Message();
					message.what=3;
					message.arg1=result.getState();
					handler.sendMessage(message);
				} catch (IOException e) {
					Logger.d("通信失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("通信失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void lectureMessageRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的结果
					String response = HttpUtil.MessageRequest(ConstantClass.ADDRESS, ConstantClass.MESSAGE_REQUEST_COM, ConstantClass.userOnline, lectureId);
					Gson gson = new Gson();
					LectureMessageResult result = gson.fromJson(response, LectureMessageResult.class);
					int state = result.getState();
					if (state == 0) {
						List <LectureMessageResult.MessageListBean> messageList = result.getMessageList();
						if (messageList.size() != 0) {
							for (LectureMessageResult.MessageListBean messageBean : messageList) {
								LectureMessageDB lectureMessageDB = new LectureMessageDB();
								lectureMessageDB.setMessageLikersNum(messageBean.getGood_amount());
								lectureMessageDB.setMessageLikeorNot(messageBean.getUserLike());
								lectureMessageDB.setMessageContent(messageBean.getContent());
								lectureMessageDB.setUserName(messageBean.getUserName());
								lectureMessageDB.setUserHead(messageBean.getUserHead());
								Logger.d(messageBean.getUserHead());
								lectureMessageDB.setMessageId(messageBean.getMessageId());
								messageDBList.add(lectureMessageDB);
							}
						}
					}
					Message message = new Message();
					message.what = 1;
					message.arg1 = state;
					handler.sendMessage(message);
				} catch (IOException e) {
					Logger.d("通信失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("通信失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

	//点赞或者取消点赞
	private void LikeOrNotChange(final long objectID, final int isLikeOrNot) {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的结果
					String response = HttpUtil.LikeOrNotChangeRequest(ConstantClass.ADDRESS, ConstantClass.LIKEORNOT_CHAGNE_COM, ConstantClass.userOnline, objectID, ConstantClass.TYPE_MESSAGE, isLikeOrNot);

					//解析和处理服务器返回的结果
					/*loginResult = Utility.handleLoginRespose(response,userPhone,mUserDao);
					//处理结果
					handler.sendEmptyMessage(1);*/
				} catch (IOException e) {
					Logger.d("通信失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					Logger.d("通信失败，JSON error");
					e.printStackTrace();
				}
			}
		}).start();
	}

}