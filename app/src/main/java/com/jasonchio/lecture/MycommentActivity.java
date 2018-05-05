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
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;;
import com.jasonchio.lecture.greendao.CommentDB;
import com.jasonchio.lecture.greendao.CommentDBDao;
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

public class MycommentActivity extends BaseActivity implements CommentAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	CommentAdapter mAdapter;                    //评论适配器

	TitleLayout titleLayout;                    //标题栏

	Button titleSecondButton;                   //标题栏的第二个按钮

	Button titleFirstButton;                    //标题栏的第一个按钮

	List <CommentDB> commentList = new ArrayList <>();      //评论列表
	List <LectureDB> lectureList = new ArrayList <>();      //评论对应的讲座列表

	ListView listView;                          //要显示的 listview

	DaoSession daoSession;                      //数据库操作对象

	CommentDBDao mCommentDao;                   //评论表操作对象

	LectureDBDao mLectureDao;                   //讲座表操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	int myCommentRequestResult;                 //“我的点评”请求结果

	int commentRequestResult;                   //评论请求结果

	int lectureRequestResult;                   //评论对应的讲座请求结果

	Handler handler;                            //handler 对象

	Dialog myCommentLoadDialog;                 //加载对话框

	int likeThisCommentResult;                 //给评论点赞或取消点赞结构

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycomment);

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
		titleLayout.setTitle("我的点评");
	}

	@Override
	void initWidget() {
		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mCommentDao = daoSession.getCommentDBDao();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();

		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleLayout = (TitleLayout) findViewById(R.id.mycomment_title_layout);
		titleSecondButton = titleLayout.getSecondButton();
		titleFirstButton = titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);
		mAdapter = new CommentAdapter(listView, commentList, lectureList, mCommentDao, MycommentActivity.this);
		listView.setAdapter(mAdapter);
	}

	@Override
	void initEvent() {

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);
		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (myCommentRequestResult == 0) {
							showCommentInfoToTop();
						} else if (myCommentRequestResult == 1) {
							DialogUtils.closeDialog(myCommentLoadDialog);
							Toasty.error(MycommentActivity.this, "数据库无更新").show();
						} else if (myCommentRequestResult == 3) {
							DialogUtils.closeDialog(myCommentLoadDialog);
							Toasty.info(MycommentActivity.this,
									"你还没有点评过讲座呢，先去看个讲座点评一下吧").show();
						} else {
							DialogUtils.closeDialog(myCommentLoadDialog);
							Toasty.error(MycommentActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (lectureRequestResult == 0) {
							showCommentInfoToTop();
						}
						break;
					case 3:
						if (commentRequestResult == 0) {
							showCommentInfoToTop();
						}
				}
				return true;
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				myCommentLoadDialog=DialogUtils.createLoadingDialog(MycommentActivity.this,"正在加载");
				showCommentInfoToTop();
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
		switch (v.getId()) {
			case R.id.title_first_button:
				finish();
				break;
			default:
		}
	}

	@Override
	public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
		//打开对应的讲座详情
		CommentDB comment = commentList.get(position);
		Intent intent = new Intent(MycommentActivity.this, LectureDetailActivity.class);
		intent.putExtra("lecture_id", (int) comment.getCommentlecureId());
		startActivity(intent);
	}

	@Override
	public void itemClick(View v) {
		int position;

		position = (Integer) v.getTag();

		CommentDB comment = commentList.get(position);

		switch (v.getId()) {
			case R.id.comment_user_layout:
				Toast.makeText(MycommentActivity.this, "查看评论人资料", Toast.LENGTH_SHORT).show();
				break;
			case R.id.comment_lecture_layout:
				Intent intent = new Intent(MycommentActivity.this, LectureDetailActivity.class);
				intent.putExtra("lecture_id", (int) comment.getCommentlecureId());
				startActivity(intent);
				break;
			case R.id.comment_like_layout:
				if (comment.getIsLike() == 1) {
					Toast.makeText(MycommentActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
					Logger.d(position);
					mAdapter.changeCommentLike(position, 0);
					LikeThisComment(comment.getCommentId(), 0);
				} else {
					Toast.makeText(MycommentActivity.this, "点赞", Toast.LENGTH_SHORT).show();
					Logger.d(position);
					LikeThisComment(comment.getCommentId(), 1);
					mAdapter.changeCommentLike(position, 1);
				}
				break;
			default:
		}
	}

	//“我的点评”请求
	private void MyCommentRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.MycommentRequest(ConstantClass.ADDRESS, ConstantClass.MYCOMMENT_REQUEST_COM, ConstantClass.userOnline);
					//解析和处理服务器返回的数据
					myCommentRequestResult = Utility.handleMyCommentResponse(response, mUserDao);
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

	//评论请求
	private void CommentRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取数据库中最后一条评论的 id
					long lastCommentID = Utility.lastCommentinDB(mCommentDao);
					//获取服务器返回数据
					String response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_COM, ConstantClass.userOnline, lastCommentID);
					//解析和处理服务器返回的数据
					commentRequestResult = Utility.handleCommentResponse(response, mCommentDao);
					//处理结果
					handler.sendEmptyMessage(3);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//评论对应的讲座请求
	private void LectureRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取数据库中最后一条讲座的 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回数据
					String response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID);
					//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
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

	//将从数据库中查找到的讲座显示到界面中
	private void showCommentInfoToTop() {
		//从数据库查询用户的评论
		String userComment = Utility.getUserComment(ConstantClass.userOnline, mUserDao);
		//如果本地没有就从服务器请求
		if (userComment == null || userComment.length() == 0) {
			MyCommentRequest();
			return;
		}
		//将用户的评论 id 解析成字符串数组，方便操作
		String[] myComment = Utility.getStrings(userComment);
		if (myComment.length == 0) {
			Toasty.error(MycommentActivity.this, "解析用户的点评失败，请联系开发者").show();
			return;
		} else {
			//如果评论列表不为空，则清空
			if (!commentList.isEmpty()) {
				commentList.clear();
			}
			CommentDB comment;
			//从数据库中逐个查找用户的评论
			for (int i = 0; i < myComment.length; i++) {
				comment = mCommentDao.queryBuilder().where(CommentDBDao.Properties.CommentId.eq(myComment[i])).build().unique();
				//如果数据库中没有该条评论，就从服务器请求
				if (comment == null) {
					CommentRequest();
					return;
				} else {
					//从数据库中查找该评论对应的讲座信息
					LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(comment.getCommentlecureId())).build().unique();
					//如果有对应的信息，将该条评论及对应的讲座添加到对应的列表中
					if (lecture != null) {
						lectureList.add(lecture);
						commentList.add(comment);
					} else {
						//如果没有该评论对应的讲座信息就从服务器请求
						LectureRequest();
						return;
					}

				}
			}

			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
			DialogUtils.closeDialog(myCommentLoadDialog);
		}

	}

	//给评论点赞或取消
	private void LikeThisComment(final long commentID, final int islike) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.LikeThisComment(ConstantClass.ADDRESS, ConstantClass.LIKE_COMMENT_COM, commentID, ConstantClass.userOnline, islike);
					//解析和处理服务器返回的数据
					likeThisCommentResult = Utility.handleLikeChangeResponse(response, commentID, mCommentDao, islike);
					//处理结果
					handler.sendEmptyMessage(3);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
