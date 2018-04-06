package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.database.CommentDB;
import com.jasonchio.lecture.database.LectureDB;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;

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

public class DiscoveryFragment extends Fragment implements CommentAdapter.InnerItemOnclickListener,AdapterView.OnItemClickListener{

	private View rootview;

	SwipeToLoadLayout swipeToLoadLayout;

	CommentAdapter mAdapter;

	TitleLayout titleLayout;

	Button titleSecondButton;

	String contents="十八大以来我国所取得的巨大进入了加速圆梦期，中华民族伟大复兴的中国梦正在由“遥想”“遥望”变为“近看”“凝视”。您是否在为一篇篇手动输入参考文献而痛苦？您是否在用EXCEL等原始手段为文献排序？您是否还在为从电脑成堆的文档中寻找所需要的文献而烦恼？您是否在茫茫文献海洋中迷失";

	int consts=0;

	String title="测试测试测试测试测试测试测试测试测试测试";

	String time="2018年4月03日 晚6：00";

	String source="武汉理工大学图书馆";

	LectureDB lecture=new LectureDB(title,time,source,contents,100);

	int likers =10;

	int lecturelikes=0;

	int islike=0;

	ListView listView;

	String response;

	List<CommentDB> commentList =new ArrayList<>();

	List<LectureDB> lectureList=new ArrayList<>();
	Handler handler;

	int commentRequestResult=-1;

	int lectureRequestResult;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if(rootview == null){
			rootview=inflater.inflate(R.layout.fragment_discovery,null);
			Toast.makeText(getActivity(),"FragmentDiscovery==onCreateView",Toast.LENGTH_SHORT ).show();
		}
		ViewGroup parent=(ViewGroup)rootview.getParent();
		if(parent!=null){
			parent.removeView(rootview);
		}

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		titleLayout=(TitleLayout)rootview.findViewById(R.id.discovery_title_layout);

		titleSecondButton=titleLayout.getSecondButton();
		titleSecondButton.setBackgroundResource(R.drawable.ic_title_addcomment);
		titleLayout.setFirstButtonVisible(View.GONE);
		titleLayout.setTitle("发现");
		titleSecondButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SelecteLectureCommentActivity.class);
				startActivity(intent);
			}
		});


		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (commentRequestResult == 0) {
							Toasty.success(getContext(), "获取评论成功").show();
							showCommentInfoToTop();
						} else if (commentRequestResult == 1) {
							Toasty.error(getContext(), "数据库无更新").show();
						} else {
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if(lectureRequestResult==0){
							showCommentInfoToTop();
						}
						break;
					case 3:
						break;
				}
				return true;
			}
		});
		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new CommentAdapter(commentList,lectureList,getContext());

		listView.setAdapter(mAdapter);

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				/*
				* 处理逻辑同推荐页面
				* */

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

		autoRefresh();

		return rootview;
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CommentDB comment= commentList.get(position);
		int lectureID=comment.getCommentlecureId();
		Intent intent=new Intent(getActivity(),LectureDetailActivity.class);
		intent.putExtra("lecture_id",lectureID);
		startActivity(intent);
	}

	@Override
	public void itemClick(View v) {
		int position;
		position = (Integer) v.getTag();

		CommentDB comment= commentList.get(position);

		switch (v.getId()){
			case R.id.comment_user_layout:
				Toast.makeText(getActivity(),"查看评论人资料",Toast.LENGTH_SHORT).show();
				break;
			case R.id.comment_lecture_layout:

				Intent intent=new Intent(getActivity(),LectureDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.comment_like_layout:

				if(islike==1){
					Toast.makeText(getActivity(),"取消点赞",Toast.LENGTH_SHORT).show();
					//评论点赞问题，待修复
					//comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like);
					//mAdapter.notifyDataSetChanged();
					islike=0;
					LikeThisComment();
				}else{
					Toast.makeText(getActivity(),"点赞",Toast.LENGTH_SHORT).show();
					//comment.setCommentLikersImage(R.drawable.ic_discovery_comment_like_selected);
					mAdapter.notifyDataSetChanged();
					islike=1;
					LikeThisComment();
				}
				break;
			default:
		}
	}

	private void CommentRequest(){

		//先从数据库查找是否有数据，按时间排列，加载前十条，没有则从服务器请求，并保存
		//
		/*
		 * 同时与服务器数据库更新时间比对，先发更新时间对比请求，有更新则保存到本地数据库*/

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					int lastCommentID=Utility.lastCommentinDB();

					Logger.d("lastCommentID "+lastCommentID);

					response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_PORT,lastCommentID);

					Logger.json(response);

					commentRequestResult= Utility.handleCommentResponse(response);

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

	private void LikeThisComment(){

		//先从数据库查找是否有数据，按时间排列，加载前十条，没有则从服务器请求，并保存
		//
		/*
		 * 同时与服务器数据库更新时间比对，先发更新时间对比请求，有更新则保存到本地数据库*/

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					response = HttpUtil.LikeThisComment(ConstantClass.ADDRESS, ConstantClass.LIKE_COMMENT_PORT,1,4,islike);

					Logger.json(response);

					//lectureRequestResult= Utility.handleLectureResponse(response,getContext());
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

		List<CommentDB> commentDBList= DataSupport.order("commentId desc").limit(10).offset(mAdapter.getCount()).find(CommentDB.class);

		if(commentDBList.size()<1){
			CommentRequest();
			return;
		}

		for(CommentDB commentDB:commentDBList){
			Log.d("test",Integer.toString(commentDB.getCommentId()));
			List<LectureDB> lectureDBList=DataSupport.where("lectureId=?",Integer.toString(commentDB.getCommentlecureId())).find(LectureDB.class);
			if(lectureDBList.size()!=0){
				for(LectureDB lectureDB:lectureDBList){
					lectureList.add(lectureDB);
					Log.d("test",Integer.toString(lectureDB.getLectureId()));
				}
			}else{
				LectureRequest();
				return;
			}
		}
		commentList.addAll(0,commentDBList);
		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
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

					int lastLecureID=Utility.lastLetureinDB();

					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);

					lectureRequestResult=Utility.handleLectureResponse(lectureresponse);

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
}

