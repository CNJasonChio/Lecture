package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;;
import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.DynamicsDB;
import com.jasonchio.lecture.gson.AddCommentResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.DynamicsRequestResult;
import com.jasonchio.lecture.gson.MyDynamicsResult;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.KeyboardUtils;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyDynamicsActivity extends BaseActivity implements DynamicsAdapter.OnItemClickListener {

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	DynamicsAdapter mAdapter;
	TitleLayout titleLayout;                    //标题栏

	Button titleSecondButton;                   //标题栏的第二个按钮

	Button titleFirstButton;                    //标题栏的第一个按钮

	List <DynamicsDB> dynamicsList = new ArrayList <>();      //评论列表

	RecyclerView listView;                          //要显示的 listview

	Handler handler;                            //handler 对象

	Dialog myDynamicsLoadDialog;                 //加载对话框

	List<Long> dynamicsIDs;

	int isLikeOrNot=0;

	Dialog addCommentDialog;

	int requestTimes=0;

	ImageView commentImage;
	ImageView likeImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydynamics);

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
		titleLayout.setTitle("我的动态");
	}

	@Override
	void initWidget() {

		swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
		titleLayout = (TitleLayout) findViewById(R.id.mycomment_title_layout);
		titleSecondButton = titleLayout.getSecondButton();
		titleFirstButton = titleLayout.getFirstButton();
		listView = (RecyclerView) findViewById(R.id.swipe_target);
		mAdapter = new DynamicsAdapter(dynamicsList,MyDynamicsActivity.this);
		listView.setLayoutManager(new LinearLayoutManager(MyDynamicsActivity.this));
		listView.setAdapter(mAdapter);
		commentImage=(ImageView)findViewById(R.id.dynamics_comment_image) ;
		likeImage=(ImageView)findViewById(R.id.dynamics_comment_like_image) ;
	}

	@Override
	void initEvent() {

		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if(msg.arg1==0){
							initDynamics();
						}else{
							Toasty.error(MyDynamicsActivity.this,"获取动态失败,请稍后再试").show();
							DialogUtils.closeDialog(myDynamicsLoadDialog);
						}
						break;
					case 2:
						initDynamics();
						break;
					case 3:
						int position=msg.arg1;
						if(msg.arg2==0){
							DynamicsDB dynamicsDB=dynamicsList.get(position);
							if(dynamicsDB.getIsLikeorNot()==0){
								isLikeOrNot=1;
								dynamicsDB.setIsLikeorNot(1);
								dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()+1);
							}else{
								isLikeOrNot=0;
								dynamicsDB.setIsLikeorNot(0);
								dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()-1);
							}
							dynamicsList.set(position,dynamicsDB);
							mAdapter.notifyItemChanged(position);
						}else{
							Toasty.error(MyDynamicsActivity.this,"请稍后再试").show();
						}
						break;
					case 4:
						int pos=msg.arg1;
						if(msg.arg2==0){
							DynamicsDB dynamics=dynamicsList.get(pos);
							dynamics.setCommentNum(dynamics.getCommentNum()+1);
							dynamicsList.set(pos,dynamics);
							mAdapter.notifyItemChanged(pos);
							Toasty.success(MyDynamicsActivity.this,"评论成功").show();
						}else{
							Toasty.error(MyDynamicsActivity.this,"评论失败，请稍后再试").show();
						}
						DialogUtils.closeDialog(addCommentDialog);
						break;
					default:
				}
				return true;
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				myDynamicsLoadDialog=DialogUtils.createLoadingDialog(MyDynamicsActivity.this,"正在加载");
				MyDynamicsRequest();
				swipeToLoadLayout.setRefreshing(false);
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
	public void onItemClick(int position,View view) {
		DynamicsDB dynamicsDB=dynamicsList.get(position);
		switch (view.getId()){
			case R.id.dynamics_comment_image:
				addCommentDialog=DialogUtils.createLoadingDialog(MyDynamicsActivity.this,"正在评论");
				addComment(position);
				break;
			case R.id.dynamics_comment_like_image:
				isLikeOrNot=dynamicsDB.getIsLikeorNot();
				if(isLikeOrNot==0){
					LikeOrNotChange(position,dynamicsDB.getId(),ConstantClass.TYPE_DYNAMICS,1);
				}else{
					LikeOrNotChange(position,dynamicsDB.getId(),ConstantClass.TYPE_DYNAMICS,0);
				}
				break;
			case R.id.dynamics_layout:
				Intent intent=new Intent(MyDynamicsActivity.this,DynamicsDetailActivity.class);
				intent.putExtra("id",dynamicsDB.getId());
				intent.putExtra("userHead",dynamicsDB.getUserHead());
				intent.putExtra("userName",dynamicsDB.getUserName());
				intent.putExtra("dynamicsContent",dynamicsDB.getDynamicsContent());
				intent.putExtra("time",dynamicsDB.getTime());
				intent.putExtra("likeNum",dynamicsDB.getLikerNum());
				intent.putExtra("commentNum",dynamicsDB.getCommentNum());
				intent.putExtra("isLikeorNot",dynamicsDB.getIsLikeorNot());
				startActivity(intent);
				break;
		}
	}

	//“我的点评”请求
	private void MyDynamicsRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.MyMessageRequest(ConstantClass.ADDRESS,ConstantClass.MYDYNAMICS_REQUEST_COM,ConstantClass.userOnline);
					//解析和处理服务器返回的数据
					Gson gson=new Gson();
					MyDynamicsResult result=gson.fromJson(response,MyDynamicsResult.class);
					Message message=new Message();
					message.what=1;
					if(result!=null){
						if(result.getState()==0){
							dynamicsIDs=result.getDynamic_id();
						}
						message.arg1=result.getState();
					}else{
						message.arg1=1;
					}
					//处理结果
					handler.sendMessage(message);
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


	private void initDynamics() {
		if(requestTimes<dynamicsIDs.size()){
			DynamicsRequest(ConstantClass.REQUEST_SINGLE,dynamicsIDs.get(requestTimes));
			requestTimes++;
		}else{
			mAdapter.notifyDataSetChanged();
			DialogUtils.closeDialog(myDynamicsLoadDialog);
		}
	}

	//请求动态信息
	private void DynamicsRequest(final int requestType, final long dynamicsID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回的数据
					String response = HttpUtil.DynamicsRequest(ConstantClass.ADDRESS, ConstantClass.DYNAMICS_REQUEST_COM, ConstantClass.userOnline, dynamicsID,requestType);

					Gson gson=new Gson();
					DynamicsRequestResult requestResult=gson.fromJson(response,DynamicsRequestResult.class);
					Message message=new Message();
					message.what=2;
					if(requestResult!=null){
						if(requestResult.getState()==0){
							List<DynamicsRequestResult.DynamicBean> dynamicBeanList=requestResult.getDynamic() ;
							for(DynamicsRequestResult.DynamicBean dynamicBean: dynamicBeanList){
								DynamicsDB dynamicsDB=new DynamicsDB();
								dynamicsDB.setId(dynamicBean.getDynamic_id());
								dynamicsDB.setCommentNum(dynamicBean.getDynamic_comment_amount());
								dynamicsDB.setDynamicsContent(dynamicBean.getDynamic_information());
								dynamicsDB.setIsLikeorNot(dynamicBean.getDynamic_user_like());
								dynamicsDB.setTime(dynamicBean.getDynamic_time());
								dynamicsDB.setUserHead(dynamicBean.getDynamic_user_face_url());
								dynamicsDB.setUserName(dynamicBean.getDynamic_user());
								dynamicsList.add(dynamicsDB);
							}
						}
					}else{
						message.arg1=1;
					}
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

	//添加评论
	private void addComment(final int position){
		KeyboardUtils.showCommentEdit(MyDynamicsActivity.this, listView, new KeyboardUtils.liveCommentResult() {
			@Override
			public void onResult(boolean confirmed, String commentContent) {
				if (confirmed) {
					addCommentDialog=DialogUtils.createLoadingDialog(MyDynamicsActivity.this,"正在发表评论");
					CommentDynamicsRequest(position,commentContent);
				}
			}
		});
	}

	//点赞或者取消点赞
	private void LikeOrNotChange(final int position, final long objectID, final int objectType, final int isLikeOrNot) {
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
					message.what=3;
					message.arg1=position;
					message.arg2=result.getState();
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

	//评论动态
	private void CommentDynamicsRequest(final int position, final String dynamicsContent) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//获取服务器返回的数据
					String response = HttpUtil.CommentDynamicsRequest(ConstantClass.ADDRESS, ConstantClass.ADD_COMMENT_COM, dynamicsList.get(position).getId(), ConstantClass.userOnline,dynamicsContent, Utility.getNowTime());
					Gson gson=new Gson();
					AddCommentResult result=gson.fromJson(response,AddCommentResult.class);
					Message message=new Message();
					message.arg1=position;
					message.arg2=result.getState();
					message.what=4;
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
