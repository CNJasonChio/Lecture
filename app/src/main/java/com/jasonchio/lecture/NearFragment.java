package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.greendao.UserDB;
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
 * Created by zhaoyaobang on 2018/3/7.
 */

public class NearFragment extends BaseFragment {

	private View rootview;                      //根视图

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	LectureAdapter mAdapter;                    //讲座适配器

	List <LectureDB> lecturelist = new ArrayList <>();      //讲座列表

	ListView listView;                          //要显示的 listview

	int lectureRequestResult;                   // 讲座请求结果

	Handler handler;                            // handler duixiang

	DaoSession daoSession;                      // 数据库操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	LectureDBDao mLectureDao;                   //讲座表操作对象

	Dialog requestLoadDialog;                   //加载对话框

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_near, null);
		}
		ViewGroup parent = (ViewGroup) rootview.getParent();
		if (parent != null) {
			parent.removeView(rootview);
		}

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应事件
		initEvent();

		//autoRefresh();

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

	//讲座请求
	private void LectureRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获得数据库中最后一条讲座
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回数据
					String response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID);
					//解析和处理服务器返回数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
					//处理结果
					handler.sendEmptyMessage(1);
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
	private void showLectureInfoToTop() {

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		List <LectureDB> lectureDBList;
		if(user.getUserLocation()!=null){
			lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).where(LectureDBDao.Properties.LectureDistrict.like("%"+user.getUserLocation()+"%")).build().list();
			if(lectureDBList.isEmpty()){
				requestLoadDialog = DialogUtils.createLoadingDialog(getContext(), "正在获取讲座");
				LectureRequest();
				return;
			}
		}else{
			Toasty.info(getContext(),"还没有您的位置信息，点击左上角开始定位").show();
			lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).build().list();
			//如果数据库中没有待显示的讲座，则向服务器请求
			if (lectureDBList.size() < 1) {
				requestLoadDialog = DialogUtils.createLoadingDialog(getContext(), "正在获取讲座");
				LectureRequest();
				return;
			}
		}

		lecturelist.addAll(0, lectureDBList);
		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	void initView() {

	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		listView = (ListView) rootview.findViewById(R.id.swipe_target);
		mAdapter = new LectureAdapter(getActivity(),lecturelist);
		listView.setAdapter(mAdapter);
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao=daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				LectureDB lecture = lecturelist.get(position);
				Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
				intent.putExtra("lecture_id", (int) lecture.getLectureId());
				startActivity(intent);
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				/*
				 * 处理逻辑同推荐页面，加筛选条件（位置），先提交用户位置，接受返回的数据
				 * */
				showLectureInfoToTop();
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
						if (lectureRequestResult == 0) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.success(getContext(), "获取讲座成功").show();
							showLectureInfoToTop();
						} else if (lectureRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "讲座信息暂无更新").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {

	}
}

