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

public class FocuseFragment extends Fragment {

	private View rootview;

	SwipeToLoadLayout swipeToLoadLayout;

	LectureAdapter mAdapter;

	List <LectureDB> lecturelist = new ArrayList <>();

	String response;

	Handler handler;

	int myFocuseRequestResult = 0;

	int lectureRequestResult;

	ListView listView;

	DaoSession daoSession;

	UserDBDao mUserDao;

	LectureDBDao mLectureDao;

	Dialog requestLoadDialog;
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
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mLectureDao = daoSession.getLectureDBDao();
		mUserDao = daoSession.getUserDBDao();

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);

		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new LectureAdapter(getActivity(), listView,lecturelist,mLectureDao);

		listView.setAdapter(mAdapter);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (myFocuseRequestResult == 0) {
							showLectureInfoToTop();
						} else if (myFocuseRequestResult == 1) {
							Toasty.info(getContext(), "还没有关注的图书馆哟，先去找找自己感兴趣的图书馆吧！").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
						}
						break;
					case 2:
						if (lectureRequestResult == 0) {
							DialogUtils.closeDialog(requestLoadDialog);
							showLectureInfoToTop();
						}
				}
				return true;
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
				LectureDB lecture = lecturelist.get(position);
				Intent intent = new Intent(getActivity(), LectureDetailActivity.class);
				intent.putExtra("lecture_id", lecture.getLectureId());
				startActivity(intent);
			}
		});

		swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				showLectureInfoToTop();
				mAdapter.notifyDataSetChanged();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {

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

	private void MyFocuseRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//获取服务器返回数据
					//response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_PORT,ConstantClass.userOnline);
					response = HttpUtil.MyFocusedRequest(ConstantClass.ADDRESS, ConstantClass.MYFOCUSE_LIBRARY_REQUEST_COM, ConstantClass.userOnline);

					Logger.json(response);
					//解析和处理服务器返回的数据
					myFocuseRequestResult = Utility.handleFocuseLibraryResponse(response, mUserDao);

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

	private void LectureRequest() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					long lastLecureID = Utility.lastLetureinDB(mLectureDao);

					//response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);
					response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline,lastLecureID);

					Logger.json(response);

					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);

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

		String userFocuse = Utility.getUserFocuse(ConstantClass.userOnline, mUserDao);

		Logger.d(userFocuse);

		if (userFocuse == null || userFocuse.length() == 0) {
			MyFocuseRequest();
			Logger.d("数据库无用户关注的图书馆");
			return;
		}
		String[] focuseLibrary = Utility.getStrings(userFocuse);
		if (focuseLibrary.length == 0) {
			List <LectureDB> lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(10).orderDesc(LectureDBDao.Properties.LectureId).build().list();
			Logger.d("无关注的图书馆");
			if (lectureDBList == null || lectureDBList.isEmpty()) {
				requestLoadDialog= DialogUtils.createLoadingDialog(getContext(),"正在获取讲座");
				LectureRequest();
				return;
			} else {
				lecturelist.addAll(0, lectureDBList);
				listView.setSelection(0);
				mAdapter.notifyDataSetChanged();
			}
		} else {
			List <LectureDB> lectureDBList = new ArrayList <>();

			for (int i = 0; i < focuseLibrary.length; i++) {

				List <LectureDB> lectureDBS = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LecutreSource.eq(focuseLibrary[i])).build().list();
				if (lectureDBS.isEmpty()) {
					continue;
				} else {
					lectureDBList.addAll(lectureDBS);
				}
			}
			if (lectureDBList.isEmpty()) {
				lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(10).orderDesc(LectureDBDao.Properties.LectureId).build().list();
			}
			lecturelist.addAll(0, lectureDBList);
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}
	}
}