package com.jasonchio.lecture;

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
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MycommentActivity extends BaseActivity implements CommentAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	SwipeToLoadLayout swipeToLoadLayout;

	CommentAdapter mAdapter;

	TitleLayout titleLayout;

	Button titleSecondButton;

	Button titleFirstButton;

	int islike;

	List<CommentDB> commentList =new ArrayList<>();
	List<LectureDB> lectureList =new ArrayList<>();

	ListView listView;

	String response;

	DaoSession daoSession;

	CommentDBDao mCommentDao;

	LectureDBDao mLectureDao;

	UserDBDao mUserDao;

	int myCommentRequestResult;

	int commentRequestResult;

	int lectureRequestResult;

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycomment);

		//初始化控件
		initWidget();
		//初始化视图
		initView();

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);
		titleFirstButton.setOnClickListener(this);

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				showCommentInfoToTop();
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


		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if ( myCommentRequestResult == 0) {
							showCommentInfoToTop();
						} else if ( myCommentRequestResult == 1) {
							Toasty.error(MycommentActivity.this, "数据库无更新").show();
						} else if(myCommentRequestResult==3){
							Toasty.info(MycommentActivity.this, "你还没有点评过讲座呢，先去看个讲座点评一下吧").show();
						}else {
							Toasty.error(MycommentActivity.this, "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if(lectureRequestResult==0){
							showCommentInfoToTop();
						}
						break;
					case 3:
						if(commentRequestResult==0){
							showCommentInfoToTop();
						}
				}
				return true;
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
		titleLayout.setTitle("我的点评");
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)findViewById(R.id.mycomment_title_layout);
		titleSecondButton=titleLayout.getSecondButton();
		titleFirstButton=titleLayout.getFirstButton();
		listView = (ListView) findViewById(R.id.swipe_target);
		mAdapter = new CommentAdapter(listView,commentList,lectureList,mUserDao,mCommentDao,MycommentActivity.this);
		listView.setAdapter(mAdapter);

		daoSession=((MyApplication)getApplication()).getDaoSession();
		mCommentDao=daoSession.getCommentDBDao();
		mLectureDao=daoSession.getLectureDBDao();
		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:
				finish();
				break;
				default:
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CommentDB comment= commentList.get(position);
		Intent intent=new Intent(MycommentActivity.this,LectureDetailActivity.class);
		intent.putExtra("lecture_id",(int) comment.getCommentlecureId());
		startActivity(intent);
	}

	@Override
	public void itemClick(View v) {
		int position;

		position = (Integer) v.getTag();

		CommentDB comment= commentList.get(position);

		switch (v.getId()){
			case R.id.comment_user_layout:
				Toast.makeText(MycommentActivity.this,"查看评论人资料",Toast.LENGTH_SHORT).show();
				break;
			case R.id.comment_lecture_layout:
				Intent intent=new Intent(MycommentActivity.this,LectureDetailActivity.class);
				intent.putExtra("lecture_id",comment.getCommentlecureId());
				startActivity(intent);
				break;
			case R.id.comment_like_layout:
				if(islike==1){
					Toast.makeText(MycommentActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
					/*comment.setCommentLikers(comment.getCommentLikers()-1);
					comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like);
					mAdapter.notifyDataSetChanged();*/
					islike=0;
				}else{
					Toast.makeText(MycommentActivity.this,"点赞",Toast.LENGTH_SHORT).show();
					/*comment.setCommentLikers(comment.getCommentLikers()+1);
					comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like_selected);
					mAdapter.notifyDataSetChanged()*/;
					islike=1;
				}
				break;
			default:
		}
	}

	private void MyCommentRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					//response = HttpUtil.MycommentRequest(ConstantClass.ADDRESS, ConstantClass.MYCOMMENT_REQUEST_PORT,ConstantClass.userOnline);
					response = HttpUtil.MycommentRequest(ConstantClass.ADDRESS, ConstantClass.MYCOMMENT_REQUEST_COM,ConstantClass.userOnline);
					Logger.json(response);
					//解析和处理服务器返回的数据
					myCommentRequestResult = Utility.handleMyCommentResponse(response,mUserDao);
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

	private void CommentRequest(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastCommentID=Utility.lastCommentinDB(mCommentDao);

					Logger.d("lastCommentID "+lastCommentID);

					//response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_PORT,lastCommentID);
					response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_COM, ConstantClass.userOnline,lastCommentID);
					Logger.json(response);

					commentRequestResult = Utility.handleCommentResponse(response,mCommentDao);

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

					Logger.d("lastLecureID"+lastLecureID);

					//response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM,  ConstantClass.userOnline,lastLecureID);
					Logger.json(response);

					lectureRequestResult = Utility.handleLectureResponse(response,mLectureDao);

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

		String userComment=Utility.getUserComment(ConstantClass.userOnline,mUserDao);

		if(userComment==null || userComment.length()==0){
			MyCommentRequest();
			return;
		}
		String[] myComment = Utility.getStrings(userComment);

		if(myComment.length==0){
			Toasty.error(MycommentActivity.this,"解析用户的点评失败，请联系开发者").show();
			return;
		}else{
			if (!commentList.isEmpty()) {
				commentList.removeAll(commentList);
			}
			CommentDB comment;
			for(int i=0;i<myComment.length;i++){
				comment=mCommentDao.queryBuilder().where(CommentDBDao.Properties.CommentId.eq(myComment[i])).build().unique();
				if(comment ==null){
					CommentRequest();
					return;
				}else{
					LectureDB lecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(comment.getCommentlecureId())).build().unique();
					if(lecture!=null){
						lectureList.add(lecture);
						commentList.add(comment);
					}else{
						LectureRequest();
						return;
					}

				}
			}
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}

	}
}
