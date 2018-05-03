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

public class RecommentFragment extends Fragment {

	private View rootview;

	SwipeToLoadLayout swipeToLoadLayout;

	LectureAdapter mAdapter;

	List <LectureDB> lecturelist = new ArrayList <>();

	ListView listView;

	String response;

	int lectureRequestResult;

	Handler handler;

	DaoSession daoSession;

	LectureDBDao mLectureDao;

	UserDBDao mUserDao;

	Dialog requestLoadDialog;

	int offset = 0;

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

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);

		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new LectureAdapter(getActivity(), listView, lecturelist, mLectureDao);

		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();

		listView.setAdapter(mAdapter);

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

	private void LectureRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID = Utility.lastLetureinDB(mLectureDao);

					Logger.d("lastLecureID" + lastLecureID);

					//response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lastLecureID);

					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);

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

		String[] recommentOrder;
		List <LectureDB> lectureDBS = new ArrayList <>();

		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		if (user != null) {
			String temp = user.getRecommentLectureOrder();
			if (temp == null || temp == "") {
				List <LectureDB> lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).build().list();
				if (lectureDBList.isEmpty()) {
					Logger.d("正在获取讲座");
					LectureRequest();
					return;
				} else {
					lecturelist.clear();
					mAdapter.notifyDataSetChanged();
					lectureDBS = mLectureDao.queryBuilder().orderDesc(LectureDBDao.Properties.LectureId).build().list();
				}
				lecturelist.addAll(0, lectureDBS);
				listView.setSelection(0);
				mAdapter.notifyDataSetChanged();
			} else {

				recommentOrder = Utility.getStrings(temp);

				if (mAdapter.getCount() == recommentOrder.length) {
					Toasty.info(getContext(), "讲座信息暂无更新").show();
					return;
				}

				for (int i = mAdapter.getCount(); i < mAdapter.getCount() + 7 && i < recommentOrder.length; i += 7) {
					Logger.d(i);
					if (i + 7 > recommentOrder.length) {
						for (int j = i; j < recommentOrder.length; j++) {
							LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(Long.valueOf(recommentOrder[j]))).build().unique();

							if (lectureDB == null) {
								LectureRequest();
								return;
							} else {
								lectureDBS.add(lectureDB);
							}
						}
					} else {
						for (int j = 0; j < 7; j++) {
							LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(Long.valueOf(recommentOrder[i + j]))).build().unique();
							if (lectureDB == null) {
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

}
