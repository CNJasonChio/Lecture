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
import android.widget.ListView;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
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

public class RecommentFragment extends Fragment {

	private View rootview;

	SwipeToLoadLayout swipeToLoadLayout;

	LectureAdapter mAdapter;

	List<LectureDB> lecturelist = new ArrayList<>();

	ListView listView;

	String response;

	int lectureRequestResult;

	Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	                         Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_recommend, null);
			Toast.makeText(getActivity(), "FragmentRecommend==onCreateView", Toast.LENGTH_SHORT).show();
		}
		ViewGroup parent = (ViewGroup) rootview.getParent();
		if (parent != null) {
			parent.removeView(rootview);
		}

		swipeToLoadLayout = (SwipeToLoadLayout) rootview.findViewById(R.id.swipeToLoadLayout);

		listView = (ListView) rootview.findViewById(R.id.swipe_target);

		mAdapter = new LectureAdapter(getActivity(), R.layout.lecure_listitem, lecturelist);

		listView.setAdapter(mAdapter);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (lectureRequestResult == 0) {
							Toasty.success(getContext(), "获取讲座成功").show();
							showLectureInfoToTop();
						} else if (lectureRequestResult == 1) {
							Toasty.error(getContext(), "讲座信息暂无更新").show();
						} else {
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

		//先从数据库查找是否有数据，按时间排列，加载前十条，没有则从服务器请求，并保存
		//showLectureInfo();
		/*
		 * 同时与服务器数据库更新时间比对，先发更新时间对比请求，有更新则保存到本地数据库*/

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					int lastLecureID=Utility.lastLetureinDB();

					Logger.d("lastLecureID"+lastLecureID);

					response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_PORT, lastLecureID);

					Logger.json(response);

					lectureRequestResult = Utility.handleLectureResponse(response);

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

		List<LectureDB> lectureDBList= DataSupport.order("lectureId desc").limit(10).offset(mAdapter.getCount()).find(LectureDB.class);

		Logger.d(mAdapter.getCount());
		if(lectureDBList.size()<1){
			LectureRequest();
			return;
		}
		lecturelist.addAll(0,lectureDBList);

		listView.setSelection(0);
		mAdapter.notifyDataSetChanged();
	}

}
