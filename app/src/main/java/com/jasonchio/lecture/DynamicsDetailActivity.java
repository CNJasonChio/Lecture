package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.DynamicsDB;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.gson.AddCommentResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.DynamicsResult;
import com.jasonchio.lecture.gson.ReplyCommentResult;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.CommentExpandableListView;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.KeyboardUtils;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DynamicsDetailActivity extends BaseActivity {

	TitleLayout titleLayout;    //标题栏
	Button titleFirstButton;    //标题栏的第一个按钮
	DynamicsDB dynamicsDB=new DynamicsDB();

	CircleImageView userHead;
	TextView userName;
	TextView dynamicsContent;
	TextView dynamicsTime;
	TextView likesNum;
	TextView commentNum;
	ImageView likeImage;
	ImageView commentImage;
	LinearLayout dynamicsLayout;

	CommentExpandableListView commentListView;  //
	TextView commentDynamicsText;
	CommentExpandAdapter adapter;
	List<DynamicsResult.DataBean> commentsList;

	Dialog loadDialog;
	Dialog commentDynamicsDialog;
	Dialog replyCommentDialog;
	Handler handler;

	UserDBDao mUserDao;             //用户表操作对象
	DaoSession daoSession;          //数据库操作对象

	//测试用的json
	private String testJson = "{\n" +
			"\t\"code\": 1000,\n" +
			"\t\"message\": \"查看评论成功\",\n" +
			"\t\"data\": {\n" +
			"\t\t\"total\": 3,\n" +
			"\t\t\"tlist\": [{\n" +
			"\t\t\t\t\"id\": 42,\n" +
			"\t\t\t\t\"nickName\": \"程序猿\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 1,\n" +
			"\t\t\t\t\"createDate\": \"三分钟前\",\n" +
			"\t\t\t\t\"replyList\": [{\n" +
			"\t\t\t\t\t\"nickName\": \"沐風\",\n" +
			"\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\t\"id\": 40,\n" +
			"\t\t\t\t\t\"commentId\": \"42\",\n" +
			"\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
			"\t\t\t\t\t\"status\": \"01\",\n" +
			"\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
			"\t\t\t\t}]\n" +
			"\t\t\t},\n" +
			"\t\t\t{\n" +
			"\t\t\t\t\"id\": 41,\n" +
			"\t\t\t\t\"nickName\": \"设计狗\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 1,\n" +
			"\t\t\t\t\"createDate\": \"一天前\",\n" +
			"\t\t\t\t\"replyList\": [{\n" +
			"\t\t\t\t\t\"nickName\": \"沐風\",\n" +
			"\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\t\"commentId\": \"41\",\n" +
			"\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
			"\t\t\t\t\t\"status\": \"01\",\n" +
			"\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
			"\t\t\t\t}]\n" +
			"\t\t\t},\n" +
			"\t\t\t{\n" +
			"\t\t\t\t\"id\": 40,\n" +
			"\t\t\t\t\"nickName\": \"产品喵\",\n" +
			"\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
			"\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
			"\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
			"\t\t\t\t\"replyTotal\": 0,\n" +
			"\t\t\t\t\"createDate\": \"三天前\",\n" +
			"\t\t\t\t\"replyList\": []\n" +
			"\t\t\t}\n" +
			"\t\t]\n" +
			"\t}\n" +
			"}";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamics_detail);

		initDynamicsData();

		initWidget();

		initView();

		initEvent();

		loadDialog= DialogUtils.createLoadingDialog(DynamicsDetailActivity.this,"正在加载");
		CommentAndReplyRequest();
	}

	@Override
	void initWidget() {
		titleLayout=(TitleLayout)findViewById(R.id.dynamics_detail_title_layout);
		titleFirstButton=(Button)findViewById(R.id.title_first_button);

		userHead=(CircleImageView) findViewById(R.id.dynamics_userhead_image);
		userName=(TextView)findViewById(R.id.dynamics_username_text);
		dynamicsContent=(TextView)findViewById(R.id.dynamics_content_text);
		dynamicsTime=(TextView)findViewById(R.id.dynamics_time_text);
		likesNum=(TextView)findViewById(R.id.dynamics_like_num_text);
		commentNum=(TextView)findViewById(R.id.dynamics_comment_num_text);
		likeImage=(ImageView)findViewById(R.id.dynamics_comment_like_image);
		commentImage=(ImageView)findViewById(R.id.dynamics_comment_image);
		dynamicsLayout=(LinearLayout)findViewById(R.id.dynamics_layout);
		commentListView =(CommentExpandableListView) findViewById(R.id.dynamics_detail_comment_list);
		commentDynamicsText=(TextView)findViewById(R.id.dynamics_detail_docomment_text);

		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mUserDao = daoSession.getUserDBDao();
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("评论");

		if(dynamicsDB.getIsLikeorNot()==1){
			likeImage.setImageResource(R.drawable.ic_dynamics_like_seletcted);
		}
		if(dynamicsDB.getUserHead()!=null && !dynamicsDB.getUserHead().isEmpty()){
			Glide.with(DynamicsDetailActivity.this).load(dynamicsDB.getUserHead()).into(userHead);
		}else{
			userHead.setImageResource(R.drawable.ic_defult_userhead);
		}
		userName.setText(dynamicsDB.getUserName());
		dynamicsContent.setText(dynamicsDB.getDynamicsContent());
		dynamicsTime.setText(dynamicsDB.getTime());
		likesNum.setText(String.valueOf(dynamicsDB.getLikerNum()));
		commentNum.setText(String.valueOf(dynamicsDB.getCommentNum()));

		likeImage.setOnClickListener(this);
		titleFirstButton.setOnClickListener(this);
		commentImage.setOnClickListener(this);
		commentDynamicsText.setOnClickListener(this);
	}

	@Override
	void initEvent() {
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						//加载评论内容
						if(msg.arg1==0){
							initExpandableListView(commentsList);
						}
						DialogUtils.closeDialog(loadDialog);
						break;
					case 2:
						//点赞
						break;
					case 3:
						//回复评论
						int position=msg.arg1;
						String replyContent= (String) msg.obj;
						if(msg.arg2==0){
							UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
							DynamicsResult.DataBean.ReplyListBean replyBean = new DynamicsResult.DataBean.ReplyListBean(userDB.getUserName(),replyContent);
							adapter.addTheReplyData(replyBean, position);
							commentListView.expandGroup(position);
							Toasty.success(DynamicsDetailActivity.this,"回复成功").show();
							commentDynamicsText.setVisibility(View.VISIBLE);
							commentListView.setSelectedChild(position,adapter.getChildrenCount(position),true);
						}else{
							Toasty.error(DynamicsDetailActivity.this,"回复失败，请稍后再试").show();
						}
						DialogUtils.closeDialog(replyCommentDialog);
						break;
					case 4:
						//评论动态
						String commentContent= (String) msg.obj;
						if(msg.arg1==0){
							UserDB userDB=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
							DynamicsResult.DataBean detailBean = new DynamicsResult.DataBean(userDB.getUserName(), commentContent,Utility.getNowTime());
							detailBean.setUserLogo(userDB.getUserPhotoUrl());
							adapter.addTheCommentData(detailBean);
							Toasty.success(DynamicsDetailActivity.this,"评论成功").show();
							commentDynamicsText.setVisibility(View.VISIBLE);
							dynamicsDB.setCommentNum(dynamicsDB.getCommentNum()+1);
							commentNum.setText(String.valueOf(dynamicsDB.getCommentNum()));
							commentListView.setSelectedGroup(adapter.getGroupCount());
						}else{
							Toasty.error(DynamicsDetailActivity.this,"评论失败，请稍后再试").show();
						}
						DialogUtils.closeDialog(commentDynamicsDialog);
						break;
					default:
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:
				finish();
				break;
			case R.id.dynamics_comment_like_image:
				if(dynamicsDB.getIsLikeorNot()==0){
					dynamicsDB.setIsLikeorNot(1);
					dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()+1);
					likesNum.setText(String.valueOf(dynamicsDB.getLikerNum()));
					likeImage.setImageResource(R.drawable.ic_dynamics_like_seletcted);
					LikeOrNotChange(2,ConstantClass.TYPE_DYNAMICS,1);
				}else{
					dynamicsDB.setIsLikeorNot(0);
					dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()-1);
					likesNum.setText(String.valueOf(dynamicsDB.getLikerNum()));
					likeImage.setImageResource(R.drawable.ic_dynamics_like);
					LikeOrNotChange(2,ConstantClass.TYPE_DYNAMICS,0);
				}
				break;
			case R.id.dynamics_comment_image:
				addComment();
				break;
			case R.id.dynamics_detail_docomment_text:
				addComment();
				commentDynamicsText.setVisibility(View.INVISIBLE);
			default:

		}
	}

	//初始化动态的数据
	public void initDynamicsData(){
		Intent intent=getIntent();
		dynamicsDB.setId(intent.getLongExtra("id",1));
		dynamicsDB.setUserHead(intent.getStringExtra("userHead"));
		dynamicsDB.setUserName(intent.getStringExtra("userName"));
		dynamicsDB.setDynamicsContent(intent.getStringExtra("dynamicsContent"));
		dynamicsDB.setCommentNum(intent.getIntExtra("commentNum",0));
		dynamicsDB.setTime(intent.getStringExtra("time"));
		dynamicsDB.setLikerNum(intent.getIntExtra("likeNum",0));
		dynamicsDB.setIsLikeorNot(intent.getIntExtra("isLikeorNot",0));
	}

	/**
	 * 初始化评论和回复列表
	 */
	private void initExpandableListView(final List<DynamicsResult.DataBean> commentList){
		commentListView.setGroupIndicator(null);
		//默认展开所有回复
		adapter = new CommentExpandAdapter(this, commentList);
		commentListView.setAdapter(adapter);
		for(int i = 0; i<commentList.size(); i++){
			commentListView.expandGroup(i);
		}
		commentListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
				addReply(groupPosition);
				return true;
			}
		});

		commentListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
				addReply(groupPosition);
				return false;
			}
		});

		commentListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				//toast("展开第"+groupPosition+"个分组");

			}
		});
	}

/*	*//**
	 * by moos on 2018/04/20
	 * func:弹出评论框
	 *//*
	private void showCommentDialog(){
		dialog = new BottomSheetDialog(this);
		View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
		final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
		final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
		dialog.setContentView(commentView);
		*//**
		 * 解决bsd显示不全的情况
		 *//*
		bt_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String commentContent = commentText.getText().toString().trim();
				if(!TextUtils.isEmpty(commentContent)){
					//commentOnWork(commentContent);
					dialog.dismiss();
					CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent,"刚刚");
					adapter.addTheCommentData(detailBean);
					Toasty.success(DynamicsDetailActivity.this,"评论成功").show();
				}else {
					Toasty.success(DynamicsDetailActivity.this,"评论内容不能为空").show();
				}
			}
		});
		commentText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
					bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
				}else {
					bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		dialog.show();

	}

	*//**
	 * by moos on 2018/04/20
	 * func:弹出回复框
	 *//*
	private void showReplyDialog(final int position){
		dialog = new BottomSheetDialog(this);
		View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
		final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
		final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
		commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");

		dialog.setContentView(commentView);
		bt_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String replyContent = commentText.getText().toString().trim();
				if(!TextUtils.isEmpty(replyContent)){

					dialog.dismiss();
					ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent);
					adapter.addTheReplyData(detailBean, position);
					commentListView.expandGroup(position);
					Toasty.success(DynamicsDetailActivity.this,"回复成功").show();
				}else {
					Toasty.success(DynamicsDetailActivity.this,"回复内容不能为空").show();
				}
			}
		});
		commentText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
					bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
				}else {
					bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
		dialog.show();
	}*/

	//添加评论
	private void addComment(){
		KeyboardUtils.showCommentEdit(DynamicsDetailActivity.this, commentDynamicsText, new KeyboardUtils.liveCommentResult() {
			@Override
			public void onResult(boolean confirmed, String commentContent) {
				if (confirmed) {
					if(!TextUtils.isEmpty(commentContent)){
						commentDynamicsDialog=DialogUtils.createLoadingDialog(DynamicsDetailActivity.this,"正在评论");
						CommentDynamicsRequest(dynamicsDB.getId(),commentContent);
					}else {
						Toasty.success(DynamicsDetailActivity.this,"评论内容不能为空").show();
					}
				}
			}
		});
	}

	//添加回复
	private void addReply(final int position){
		KeyboardUtils.showCommentEdit(DynamicsDetailActivity.this, commentDynamicsText, new KeyboardUtils.liveCommentResult() {
			@Override
			public void onResult(boolean confirmed, String replyContent) {
				if (confirmed) {
					if(!TextUtils.isEmpty(replyContent)){
						replyCommentDialog=DialogUtils.createLoadingDialog(DynamicsDetailActivity.this,"正在回复");
						ReplyCommentRequest(commentsList.get(position).getId(),replyContent,position);
					}else {
						Toasty.success(DynamicsDetailActivity.this,"回复内容不能为空").show();
					}
				}
			}
		});
	}

	//评论动态
	private void CommentDynamicsRequest(final long dynamicsID, final String commentContent) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					/*//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);*/
					//获取服务器返回的数据
					String response = HttpUtil.CommentDynamicsRequest(ConstantClass.ADDRESS, ConstantClass.ADD_COMMENT_COM, dynamicsID, ConstantClass.userOnline,commentContent, Utility.getNowTime());
					Gson gson=new Gson();
					AddCommentResult result=gson.fromJson(response,AddCommentResult.class);
					Message message=new Message();
					message.what=4;
					message.arg1=result.getState();
					message.obj=commentContent;
					handler.sendMessage(message);
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

	//回复评论
	private void ReplyCommentRequest(final long commentID, final String replyContent, final int position) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					/*//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);*/
					//获取服务器返回的数据
					String response = HttpUtil.ReplyCommentRequest(ConstantClass.ADDRESS, ConstantClass.ADD_REPLY_COM, dynamicsDB.getId(),commentID, ConstantClass.userOnline,replyContent,ConstantClass.TYPE_COMMENT,Utility.getNowTime());

					Gson gson=new Gson();
					ReplyCommentResult result=gson.fromJson(response,ReplyCommentResult.class);

					Message message=new Message();
					message.what=3;
					message.arg1=position;
					message.arg2=result.getState();
					message.obj=replyContent;
					handler.sendMessage(message);

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

	//点赞或者取消点赞
	private void LikeOrNotChange(final long objectID,final int objectType, final int isLikeOrNot) {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的结果
					String response = HttpUtil.LikeOrNotChangeRequest(ConstantClass.ADDRESS, ConstantClass.LIKEORNOT_CHAGNE_COM,ConstantClass.userOnline,objectID,objectType,isLikeOrNot);
					Gson gson=new Gson();
					CommonStateResult result=gson.fromJson(response,CommonStateResult.class);
					Message message=new Message();
					message.what=2;
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

	//请求动态信息
	private void CommentAndReplyRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					/*//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);*/
					//获取服务器返回的数据
					String response = HttpUtil.CommentAndReplyRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REPLY_REQUEST_COM, ConstantClass.userOnline, dynamicsDB.getId());
					Log.d("test",response);
					Gson gson=new Gson();
					DynamicsResult result=gson.fromJson(response,DynamicsResult.class);
					commentsList=result.getData();
					Message message=new Message();
					message.what=1;
					message.arg1=result.getState();
					handler.sendMessage(message);
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
}