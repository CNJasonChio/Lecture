package com.jasonchio.lecture;

import android.app.Dialog;
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
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
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

public class RecommentFragment extends BaseFragment {

	View rootview;                              //根视图

	SwipeToLoadLayout swipeToLoadLayout;        //刷新布局

	LectureAdapter mAdapter;                    //讲座适配器

	List <LectureDB> lecturelist = new ArrayList < >();     //讲座列表

	ListView listView;                          //要显示的 listview

	int lectureRequestResult;                   //讲座请求结果

	Handler handler;                            // handler 对象

	DaoSession daoSession;                      //数据库操作对象

	LectureDBDao mLectureDao;                   //讲座表操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	Dialog requestLoadDialog;                   //加载对话框

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_recommend, null);
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
		//自动刷新
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

	//讲座信息请求
	private void LectureRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取数据库中最后一条讲座 id
					long lastLecureID = Utility.lastLetureinDB(mLectureDao);
					//获取服务器返回的数据
					String response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID);
					//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
					//处理结果
					handler.sendEmptyMessageDelayed(1, 1000);
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

	//将讲座显示到界面中
	private void showLectureInfoToTop() {

		//推荐讲座序列
		String[] recommentOrder;
		List <LectureDB> lectureDBS = new ArrayList <>();
		//获得当前用户的对象
		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		if (user != null) {
			//从数据库获得用户的推荐讲座序列
			String temp = user.getRecommentLectureOrder();
			//如果无推荐讲座序列，就每次从数据库顺序取7条讲座显示
			if (temp == null || temp == "") {
				List <LectureDB> lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).build().list();
				//如果数据库中无讲座待显示，则向服务器请求新的讲座
				if (lectureDBList.isEmpty()) {
					LectureRequest();
					return;
				} else {
					//否则将数据库中已有讲座添加到显示列表中
					lecturelist.clear();
					mAdapter.notifyDataSetChanged();
					lectureDBS = mLectureDao.queryBuilder().orderDesc(LectureDBDao.Properties.LectureId).build().list();
				}
				lecturelist.addAll(0, lectureDBS);
				listView.setSelection(0);
				mAdapter.notifyDataSetChanged();
			} else {
				//如果有推荐讲座序列，则按推荐讲座序列，每次显示7条讲座

				//将推荐讲座序列转换为字符串数组，方便操作
				recommentOrder = Utility.getStrings(temp);

				if (mAdapter.getCount() == recommentOrder.length) {
					Toasty.info(getContext(), "讲座信息暂无更新").show();
					return;
				}
				//按推荐序列依次添加讲座，
				for (int i = mAdapter.getCount(); i < mAdapter.getCount() + 7 && i < recommentOrder.length; i += 7) {

					//如果待显示的讲座不足7条，则全部显示
					if (i + 7 > recommentOrder.length) {
						for (int j = i; j < recommentOrder.length; j++) {
							LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(Long.valueOf(recommentOrder[j]))).build().unique();
							//若该条讲座不在数据库中，则向服务器请求
							if (lectureDB == null) {
								LectureRequest();
								return;
							} else {
								lectureDBS.add(lectureDB);
							}
						}
					} else {
						//如果待显示的讲座大于7条，则只新增7条到界面中

						//依次添加讲座
						for (int j = 0; j < 7; j++) {
							LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(Long.valueOf(recommentOrder[i + j]))).build().unique();
							if (lectureDB == null) {
								//若该条讲座不在数据库中，则向服务器请求
								LectureRequest();
								return;
							} else {
								lectureDBS.add(lectureDB);
							}

						}
					}
				}

				lecturelist.addAll(0, lectureDBS);
				listView.setSelection(0);
				mAdapter.notifyDataSetChanged();
				DialogUtils.closeDialog(requestLoadDialog);
			}
		}
	}

	@Override
	void initView() {

	}

	@Override
	void initWidget() {
		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);
		listView = (ListView) rootview.findViewById(R.id.swipe_target);
		mAdapter = new LectureAdapter(getActivity(), lecturelist);
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();
		listView.setAdapter(mAdapter);
	}

	@Override
	void initEvent() {
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (lectureRequestResult == 0) {

							Logger.d("获取讲座成功");
							showLectureInfoToTop();
							Toasty.success(getContext(), "获取讲座成功").show();
						} else if (lectureRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.info(getContext(), "讲座信息暂无更新").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});

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

				requestLoadDialog = DialogUtils.createLoadingDialog(getContext(), "正在加载");

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

	}

	@Override
	public void onClick(View v) {

	}
}
