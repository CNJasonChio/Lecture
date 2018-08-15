package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
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

public class FocuseFragment extends BaseFragment {

	View rootview;                          //根视图

	SwipeToLoadLayout swipeToLoadLayout;    //刷新布局

	LectureAdapter mAdapter;                //讲座适配器

	List <LectureDB> lecturelist = new ArrayList <>();      //讲座列表

	Handler handler;                        //Handler对象

	int myFocuseRequestResult;             //我的关注请求服务器返回结果

	int lectureRequestResult;               //讲座数据请求服务器返回结果

	ListView listView;                      //要显示的 listview

	DaoSession daoSession;                  //数据库操作对象

	UserDBDao mUserDao;                     //用户表操作对象

	LectureDBDao mLectureDao;               //讲座表操作对象

	Dialog requestLoadDialog;               //加载对话框

	boolean isFirstLoad;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_focuse, null);
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

		return rootview;
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

	//“我的关注”请求
	private void MyFocuseRequest() {
		//开启新线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					String response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_COM, ConstantClass.userOnline);
					//解析和处理服务器返回的数据
					myFocuseRequestResult = Utility.handleFocuseLibraryResponse(response, mUserDao);
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

	//讲座信息请求
	private void LectureRequest() {
		//开启新线程请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//得到讲座表中最后一条讲座的 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回数据
					String response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID,ConstantClass.REQUEST_FIRST);
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
	private void showLectureInfoToTop() {

		//获得用户关注的讲座来源
		String userFocuse = Utility.getUserFocuse(ConstantClass.userOnline, mUserDao);

		//如果本地没有用户的关注信息，就向服务器发送请求
		if (userFocuse == null || userFocuse.length() == 0) {
			MyFocuseRequest();
			return;
		}

		//若本地有用户的关注信息，将其解析成字符串数组以便处理
		String[] focuseLibrary = Utility.getStrings(userFocuse);
		//如果获取到了用户关注的讲座信息来源
		if (focuseLibrary.length != 0) {

			List <LectureDB> lectureDBList = new ArrayList <>();
			//按用户关注的讲座信息来源，逐个查询符合条件的讲座
			for (int i = 0; i < focuseLibrary.length; i++) {
				List <LectureDB> lectureDBS = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).where(LectureDBDao.Properties.LecutreSource.eq(focuseLibrary[i])).build().list();
				//如果数据库中没有该来源的讲座，就查询下一来源的讲座
				if (lectureDBS.isEmpty()) {
					continue;
				} else {
					lectureDBList.addAll(lectureDBS);
				}
			}
			//如果数据库中没有符合条件的讲座，就进行讲座请求
			if (lectureDBList.isEmpty()) {
				LectureRequest();
				return;
			}
			DialogUtils.closeDialog(requestLoadDialog);
			//否则添加到要显示的讲座列表中
			lecturelist.addAll(0, lectureDBList);
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	void initView() {
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();
	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		listView = (ListView) rootview.findViewById(R.id.swipe_target);
		mAdapter = new LectureAdapter(getActivity(), lecturelist);
		listView.setAdapter(mAdapter);
		isFirstLoad=true;
	}

	@Override
	void initEvent() {
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (myFocuseRequestResult == 0) {
							showLectureInfoToTop();
						} else if (myFocuseRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "获取关注的讲座来源失败，请稍候再试").show();
						} else if (myFocuseRequestResult == 3) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.info(getContext(), "还没有关注的图书馆哟，先去找找自己感兴趣的讲座来源吧！").show();
							if(!lecturelist.isEmpty()){
								lecturelist.clear();
								mAdapter.notifyDataSetChanged();
							}
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
						}
						break;
					case 2:
						if (lectureRequestResult == 0) {
							showLectureInfoToTop();
						}else if (lectureRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "没有更多关注发布源的讲座推荐了").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
				}
				return true;
			}
		});

		//item 点击监听器
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

		{
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				LectureDB lecture = lecturelist.get(position);
				Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
				intent.putExtra("lecture_id", (int) lecture.getLectureId());
				startActivity(intent);
			}
		});

		//上拉加载更多监听器
		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				requestLoadDialog=DialogUtils.createLoadingDialog(getContext(),"正在加载");
				showLectureInfoToTop();
				swipeToLoadLayout.setRefreshing(false);

			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void fetchData() {
		//自动刷新
		if(lecturelist.size()==0){
			autoRefresh();
		}
	}
}