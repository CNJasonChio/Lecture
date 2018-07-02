package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.jasonchio.lecture.greendao.DynamicsDB;
import com.jasonchio.lecture.gson.AddCommentResult;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.jasonchio.lecture.gson.DynamicsRequestResult;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.KeyboardUtils;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;

/**
 * /**
 * <p>
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━by:zhaoyaobang
 * <p>
 * Created by zhaoyaobang on 2018/3/6.
 */

/*public class DynamicsFragment extends Fragment implements View.OnClickListener, CommentAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {

	View rootview;                              //根视图
	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局
	CommentAdapter mAdapter;                    //评论适配器
	TitleLayout titleLayout;                    //标题栏
	Button titleSecondButton;                   //标题栏的第二个按钮
	ListView listView;                          //要显示的listview
	List <CommentDB> commentList = new ArrayList <>();  //评论列表
	List <LectureDB> lectureList = new ArrayList <>();  //评论对应的讲座列表
	Handler handler;                            //Handler 对象
	int commentRequestResult;                   //评论信息请求的服务器返回结果
	int lectureRequestResult;                   //评论对应的讲座信息请求的服务器返回结果
	int likeThisCommentRequest;                 //点赞或取消点赞的服务器返回结果
	DaoSession daoSession;                      //数据库操作对象
	LectureDBDao mLectureDao;                   //讲座表操作对象
	CommentDBDao mCommentDao;                   //评论表操作对象
	Dialog commentLoadDialog;                   //加载对话框

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_discovery, null);
		}
		ViewGroup parent = (ViewGroup) rootview.getParent();
		if (parent != null) {
			parent.removeView(rootview);
		}

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件
		initEvent();
		//自动刷新
		autoRefresh();

		return rootview;
	}

	//初始化控件
	void initWidget() {

		daoSession = ((MyApplication) getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mCommentDao = daoSession.getCommentDBDao();

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		titleLayout = (TitleLayout) rootview.findViewById(R.id.discovery_title_layout);

		titleSecondButton = titleLayout.getSecondButton();
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_addcomment);

		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new CommentAdapter(listView, commentList, lectureList, mCommentDao, getContext());

		listView.setAdapter(mAdapter);
	}

	//初始化视图
	void initView() {
		titleLayout.setFirstButtonVisible(View.GONE);
		titleLayout.setTitle("动态");
	}

	//初始化事件响应
	void initEvent() {



		mAdapter.setOnInnerItemOnClickListener(this);

		listView.setOnItemClickListener(this);

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				commentLoadDialog = DialogUtils.createLoadingDialog(getContext(), "正在加载");
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

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (commentRequestResult == 0) {
							showCommentInfoToTop();
						} else if (commentRequestResult == 1) {
							DialogUtils.closeDialog(commentLoadDialog);
							Toasty.info(getActivity(), "暂无更新").show();
						} else {
							DialogUtils.closeDialog(commentLoadDialog);
							Toasty.error(getActivity(), "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (lectureRequestResult == 0) {
							showCommentInfoToTop();
						} else {
							Toasty.error(getActivity(), "动态加载失败，请稍候再试").show();
							DialogUtils.closeDialog(commentLoadDialog);
						}
						break;
					case 3:
						if (likeThisCommentRequest == 0) {
							Logger.d("点赞或取消点赞成功");
						} else {
							Logger.d("点赞或取消点赞失败");
						}
						break;
				}
				return true;
			}
		});
	}

	//评论点击处理
	@Override
	public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
		//获得点击的评论
		CommentDB comment = commentList.get(position);
		//获得点击的评论对应的讲座
		long lectureID = comment.getCommentlecureId();
		//展示对应的讲座
		Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
		intent.putExtra("lecture_id", (int) lectureID);
		startActivity(intent);
	}

	//内部 item 点击处理
	@Override
	public void itemClick(View v) {

		//获得点击的位置
		int position;
		position = (Integer) v.getTag();

		//获得点击的评论
		CommentDB comment = commentList.get(position);

		switch (v.getId()) {
			*//*case R.id.comment_user_layout:
				Toast.makeText(getActivity(), "查看评论人资料", Toast.LENGTH_SHORT).show();
				break;*//*
			case R.id.comment_lecture_layout:
				//显示对应的讲座详情
				Intent intent = new Intent(getContext(), LectureDetailActivity.class);
				intent.putExtra("lecture_id", (int) comment.getCommentlecureId());
				startActivity(intent);
				break;
			case R.id.comment_like_layout:
				if (comment.getIsLike() == 1) {
					LikeThisComment(comment.getCommentId(), 0);
					mAdapter.changeCommentLike(position, 0);
					//mAdapter.notifyDataSetChanged();
				} else {
					LikeThisComment(comment.getCommentId(), 1);
					mAdapter.changeCommentLike(position, 1);

				}
				break;
			default:
		}
	}

	//请求评论信息
	private void CommentRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//得到评论表中最后一条评论的 id
					long lastCommentID = Utility.lastCommentinDB(mCommentDao);
					//获取服务器返回数据
					String response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_COM, ConstantClass.userOnline, lastCommentID);
					//解析和处理服务器返回的数据
					commentRequestResult = Utility.handleCommentResponse(response, mCommentDao);
					//处理结果
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//为评论点赞或取消点赞
	private void LikeThisComment(final long commentID, final int islike) {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.LikeThisComment(ConstantClass.ADDRESS, ConstantClass.LIKE_COMMENT_COM, commentID, ConstantClass.userOnline, islike);
					//解析和处理服务器返回的数据
					likeThisCommentRequest = Utility.handleLikeChangeResponse(response, commentID, mCommentDao, islike);
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

	//将从数据库中查找到的评论显示到界面中
	private void showCommentInfoToTop() {

		List <CommentDB> commentDBList = mCommentDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(CommentDBDao.Properties.CommentId).build().list();

		List <CommentDB> commentDBS;
		if (commentDBList == null || commentDBList.isEmpty()) {
			CommentRequest();
			return;
		} else {
			commentDBList.clear();
			lectureList.clear();
			mAdapter.notifyDataSetChanged();
			commentDBS = mCommentDao.queryBuilder().orderDesc(CommentDBDao.Properties.CommentId).build().list();
		}

		for (CommentDB commentDB : commentDBS) {
			LectureDB lecture = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(commentDB.getCommentlecureId())).build().unique();
			if (lecture != null) {
				lectureList.add(lecture);
			} else {
				LectureRequest();
				return;
			}
		}
		commentList.addAll(0, commentDBS);
		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
		DialogUtils.closeDialog(commentLoadDialog);
	}

	//请求评论对应的讲座
	private void LectureRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//得到讲座表中最后一条讲座的 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回数据
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID);
					//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(lectureresponse, mLectureDao);
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


}*/
public class DynamicsFragment extends BaseFragment implements View.OnClickListener,DynamicsAdapter.OnItemClickListener{

	View rootview;                              //根视图
	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局
	TitleLayout titleLayout;                    //标题栏
	Button titleSecondButton;                   //标题栏的第二个按钮
	RecyclerView recyclerView;
	DynamicsAdapter dynamicsAdapter;
	List<DynamicsDB> dynamicsDBList=new ArrayList <>();
	ImageView commentImage;
	ImageView likeImage;
	int isLikeOrNot=0;
	Handler handler;                            //Handler 对象

	//测试
	String test="测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
			"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
			"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
			"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
			"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_discovery, null);
		}
		ViewGroup parent = (ViewGroup) rootview.getParent();
		if (parent != null) {
			parent.removeView(rootview);
		}

		DynamicsRequest();
		//initDynamicData();
		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化事件
		initEvent();
		//自动刷新
		autoRefresh();


		DynamicsRequest();

		return rootview;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_second_button:
				Intent intent = new Intent(getActivity(), AddDynamicsActivity.class);
				startActivityForResult(intent,100);
				break;
		}
	}

	@Override
	void initView() {
		titleLayout.setFirstButtonVisible(View.GONE);
		titleLayout.setTitle("动态");
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		titleLayout = (TitleLayout) rootview.findViewById(R.id.discovery_title_layout);
		titleSecondButton = titleLayout.getSecondButton();
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_addcomment);
		recyclerView=(RecyclerView)rootview.findViewById(R.id.swipe_target);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		dynamicsAdapter=new DynamicsAdapter(dynamicsDBList,getContext());
		recyclerView.setAdapter(dynamicsAdapter);
		commentImage=(ImageView)rootview.findViewById(R.id.dynamics_comment_image) ;
		likeImage=(ImageView)rootview.findViewById(R.id.dynamics_comment_like_image) ;
		dynamicsAdapter.setItemClickListener(this);
	}

	@Override
	void initEvent() {
		titleSecondButton.setOnClickListener(this);

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeToLoadLayout.setRefreshing(false);
				DynamicsRequest();
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				swipeToLoadLayout.setLoadingMore(false);
			}
		});

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						recyclerView.setAdapter(dynamicsAdapter);
						break;
					case 2:
						int pos=msg.arg1;
						DynamicsDB dynamics=dynamicsDBList.get(pos);
						dynamics.setCommentNum(dynamics.getCommentNum()+1);
						dynamicsDBList.set(pos,dynamics);
						dynamicsAdapter.notifyItemChanged(pos);
						Toasty.success(getContext(),"评论成功"+pos).show();
						break;
					case 3:
						int position=msg.arg1;
						DynamicsDB dynamicsDB=dynamicsDBList.get(position);
						if(dynamicsDB.getIsLikeorNot()==0){
							isLikeOrNot=1;
							dynamicsDB.setIsLikeorNot(1);
							dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()+1);
						}else{
							isLikeOrNot=0;
							dynamicsDB.setIsLikeorNot(0);
							dynamicsDB.setLikerNum(dynamicsDB.getLikerNum()-1);
						}
						dynamicsDBList.set(position,dynamicsDB);
						dynamicsAdapter.notifyItemChanged(position);
						default:
				}
				return true;
			}
		});
	}

	@Override
	public void fetchData() {

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

	public void initDynamicData(){
		for(int i=0;i<5;i++){
			DynamicsDB dynamicsDB=new DynamicsDB(i,null,"赵耀邦"+i,test,"2017年12月2日17:55",i,i+2,1);
			dynamicsDBList.add(dynamicsDB);
		}
	}

	@Override
	public void onItemClick(int position,View view) {
		DynamicsDB dynamicsDB=dynamicsDBList.get(position);
		switch (view.getId()){
			case R.id.dynamics_comment_image:
				addComment(position);
				break;
			case R.id.dynamics_comment_like_image:
				isLikeOrNot=dynamicsDB.getIsLikeorNot();
				if(isLikeOrNot==0){
					LikeOrNotChange(dynamicsDBList.indexOf(dynamicsDB),ConstantClass.TYPE_COMMENT,1);
				}else{
					LikeOrNotChange(dynamicsDBList.indexOf(dynamicsDB),ConstantClass.TYPE_COMMENT,0);
				}
				break;
			case R.id.dynamics_layout:
				Intent intent=new Intent(getContext(),DynamicsDetailActivity.class);
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

	private void likeOrNot(int positon){

	}

	//添加留言
	private void addComment(final int position){
		KeyboardUtils.showCommentEdit(getActivity(), recyclerView, new KeyboardUtils.liveCommentResult() {
			@Override
			public void onResult(boolean confirmed, String commentContent) {
				if (confirmed) {
					CommentDynamicsRequest(position,commentContent);
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 100:
				if(resultCode==-1){
					Toasty.success(getContext(),"发表动态成功").show();
					autoRefresh();
				}
				break;
				default:
		}
	}


	//请求动态信息
	private void DynamicsRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					/*//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);*/
					//获取服务器返回的数据
					String response = HttpUtil.DynamicsRequest(ConstantClass.ADDRESS, ConstantClass.DYNAMICS_REQUEST_COM, ConstantClass.userOnline, dynamicsDBList.size());
					Gson gson=new Gson();
					DynamicsRequestResult requestResult=gson.fromJson(response,DynamicsRequestResult.class);
					if(requestResult.getState()==0){
						for(DynamicsRequestResult.DynamicBean dynamicBean:requestResult.getDynamic()){
							DynamicsDB dynamicsDB=new DynamicsDB();
							dynamicsDB.setCommentNum(dynamicBean.getDynamic_comment_amount());
							dynamicsDB.setLikerNum(dynamicBean.getDynamic_good_amount());
							dynamicsDB.setIsLikeorNot(dynamicBean.getDynamic_user_like());
							dynamicsDB.setTime(dynamicBean.getDynamic_time());
							dynamicsDB.setDynamicsContent(dynamicBean.getDynamic_information());
							if(dynamicBean.getDynamic_user()==null){
								dynamicBean.setDynamic_user("无名氏");
							}
							dynamicsDB.setUserName(dynamicBean.getDynamic_user());
							dynamicsDB.setUserHead(dynamicBean.getDynamic_user_face_url());
							dynamicsDBList.add(dynamicsDB);
						}
						handler.sendEmptyMessage(1);
					}
					/*//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
					//处理结果
					*/
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

	//评论动态
	private void CommentDynamicsRequest(final int position, final String dynamicsContent) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					/*//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);*/
					//获取服务器返回的数据
					String response = HttpUtil.CommentDynamicsRequest(ConstantClass.ADDRESS, ConstantClass.ADD_COMMENT_COM, dynamicsDBList.get(position).getId(), ConstantClass.userOnline,dynamicsContent, Utility.getNowTime());
					Gson gson=new Gson();
					AddCommentResult result=gson.fromJson(response,AddCommentResult.class);
					if(result.getState()==0){
						Message message=new Message();
						message.arg1=position;
						message.what=2;
						handler.sendMessage(message);
					}
					/*//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
					//处理结果
					*/
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
	private void LikeOrNotChange(final int position,final int objectType, final int isLikeOrNot) {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//获取服务器返回的结果
					String response = HttpUtil.LikeOrNotChangeRequest(ConstantClass.ADDRESS, ConstantClass.LIKEORNOT_CHAGNE_COM,ConstantClass.userOnline,dynamicsDBList.get(position).getId(),objectType,isLikeOrNot);
					Log.d("lecture",response);
					Gson gson=new Gson();
					CommonStateResult result=gson.fromJson(response,CommonStateResult.class);
					if(result.getState()==0) {
						Message message=new Message();
						message.what=3;
						message.arg1=position;
						handler.sendMessage(message);
					}
					//解析和处理服务器返回的结果
					/*loginResult = Utility.handleLoginRespose(response,userPhone,mUserDao);
					//处理结果
					;*/
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

