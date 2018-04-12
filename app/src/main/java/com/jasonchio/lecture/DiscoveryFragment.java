package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.jasonchio.lecture.greendao.CommentDB;
import com.jasonchio.lecture.greendao.CommentDBDao;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;
import static com.mob.tools.utils.DeviceHelper.getApplication;

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

	ListView listView;

	String response;

	List<CommentDB> commentList =new ArrayList<>();

	List<LectureDB> lectureList=new ArrayList<>();

	Handler handler;

	int commentRequestResult;

	int lectureRequestResult;

	int likeThisCommentRequest;

	DaoSession daoSession;

	LectureDBDao mLectureDao;

	CommentDBDao mCommentDao;

	UserDBDao mUserDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if(rootview == null){
			rootview=inflater.inflate(R.layout.fragment_discovery,null);
		}
		ViewGroup parent=(ViewGroup)rootview.getParent();
		if(parent!=null){
			parent.removeView(rootview);
		}

		daoSession = ((MyApplication)getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mCommentDao=daoSession.getCommentDBDao();
		mUserDao=daoSession.getUserDBDao();

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
							Toasty.info(getContext(),"暂无更新").show();
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

		mAdapter = new CommentAdapter(listView,commentList,lectureList,mUserDao,mCommentDao,getContext());

		listView.setAdapter(mAdapter);

		mAdapter.setOnInnerItemOnClickListener(this);
		listView.setOnItemClickListener(this);

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
		long lectureID=comment.getCommentlecureId();
		Intent intent=new Intent(getActivity(),LectureDetailActivity.class);
		intent.putExtra("lecture_id",(int) lectureID);
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
				if(comment.getIsLike()==1){
					Toast.makeText(getActivity(),"取消点赞",Toast.LENGTH_SHORT).show();
					mAdapter.changeCommentLike(position,0);
					LikeThisComment(comment.getCommentId(),0);
				}else{
					Toast.makeText(getActivity(),"点赞",Toast.LENGTH_SHORT).show();
					LikeThisComment(comment.getCommentId(),1);
					mAdapter.changeCommentLike(position,1);
				}
				break;
			default:
		}
	}

	private void CommentRequest(){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastCommentID=Utility.lastCommentinDB(mCommentDao);

					Logger.d("lastCommentID "+lastCommentID);

					//response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_PORT,lastCommentID);
					response = HttpUtil.CommentRequest(ConstantClass.ADDRESS, ConstantClass.COMMENT_REQUEST_COM,ConstantClass.userOnline,lastCommentID);
					Logger.json(response);
					commentRequestResult= Utility.handleCommentResponse(response,mCommentDao);

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

	private void LikeThisComment(final long commentID,final int islike){

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//response = HttpUtil.LikeThisComment(ConstantClass.ADDRESS, ConstantClass.LIKE_COMMENT_PORT,commentID,ConstantClass.userOnline,islike);
					response = HttpUtil.LikeThisComment(ConstantClass.ADDRESS, ConstantClass.LIKE_COMMENT_COM,commentID,ConstantClass.userOnline,islike);

					Logger.json(response);

					likeThisCommentRequest= Utility.handleLikeChangeResponse(response,commentID,mCommentDao,islike);

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

		List<CommentDB> commentDBList=mCommentDao.queryBuilder().offset(mAdapter.getCount()).limit(10).orderDesc(CommentDBDao.Properties.CommentId).build().list();

		if(commentDBList ==null || commentDBList.isEmpty()){
			CommentRequest();
			return;
		}

		for(CommentDB commentDB:commentDBList){
			LectureDB lecture=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(commentDB.getCommentlecureId())).build().unique();
			if(lecture!=null){
				lectureList.add(lecture);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID=Utility.lastLetureinDB(mLectureDao);

					//String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					String lectureresponse = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM,  ConstantClass.userOnline,lastLecureID);

					lectureRequestResult=Utility.handleLectureResponse(lectureresponse,mLectureDao);

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

