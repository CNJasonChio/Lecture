package com.jasonchio.lecture;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

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

	List <LectureDB> lecturelist = new ArrayList <>();     //讲座列表

	ListView listView;                          //要显示的 listview

	int lectureRequestResult;                   //讲座请求结果

	Handler handler;                            // handler 对象

	DaoSession daoSession;                      //数据库操作对象

	LectureDBDao mLectureDao;                   //讲座表操作对象

	UserDBDao mUserDao;                         //用户表操作对象

	Dialog requestLoadDialog;                   //加载对话框

	int recommentOrderResult;                   //推荐讲座请求结果

	long lastViewedLectureID;                   //记录上次看到的位置

	boolean haveRequestedLecture;               //第一次使用软件时，是否已经做过讲座请求

	boolean loadMoreLectureToBottom;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if (rootview == null) {
			rootview = inflater.inflate(R.layout.fragment_recommend, null);
		}
		ViewGroup parent = (ViewGroup) rootview.getParent();
		if (parent != null) {
			parent.removeView(rootview);
		}

		//加载上次记录的位置
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("last_viewed_lecture", MODE_PRIVATE);
		lastViewedLectureID = sharedPreferences.getLong("last_viewed_lectureID", 0);
		Logger.d("record_lectureID"+lastViewedLectureID);


		//初始化控件
		initWidget();
		//初始化视图
		initView();

		//初始化响应事件
		initEvent();

		if(lecturelist.size()==0){
			autoRefresh();
		}

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
							DialogUtils.closeDialog(requestLoadDialog);
							if(loadMoreLectureToBottom==false){
								showNewLectureToTop();
							}else if(loadMoreLectureToBottom==true){
								showMoreLectureToBottom();
							}
						} else if (lectureRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.info(getContext(), "讲座信息暂无更新").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (recommentOrderResult == 0) {
							//showLectureInfoToTop();
							showNewLectureToTop();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
					case 3:
						Toasty.normal(getContext(),"你已经看完了所有最新的讲座了，试试上拉加载更多吧！").show();
						DialogUtils.closeDialog(requestLoadDialog);
						break;
					case 4:
						Toasty.normal(getContext(),"没有更多讲座了，试试下拉刷新一下吧！").show();
						DialogUtils.closeDialog(requestLoadDialog);
						break;
					case 5:
						DialogUtils.closeDialog(requestLoadDialog);
					default:
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
				//showLectureInfoToTop();
				showNewLectureToTop();
				swipeToLoadLayout.setRefreshing(false);
			}
		});

		swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				requestLoadDialog = DialogUtils.createLoadingDialog(getContext(), "正在加载");
				showMoreLectureToBottom();
				swipeToLoadLayout.setLoadingMore(false);
			}
		});

	}

	@Override
	public void fetchData() {

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//保存上次浏览的位置
		SharedPreferences.Editor editor = getActivity().getSharedPreferences("last_viewed_lecture", MODE_PRIVATE).edit();
		lastViewedLectureID=lecturelist.get(0).getLectureId();
		editor.putLong("last_viewed_lectureID",lastViewedLectureID);
		Logger.d("last_viewed_lectureID"+lastViewedLectureID);
		editor.apply();
	}

	//讲座信息请求
	private void LectureRequest(final int requestType, final long lectureID) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					//获取服务器返回的数据
					String response = HttpUtil.LectureRequest(ConstantClass.ADDRESS, ConstantClass.LECTURE_REQUEST_COM, ConstantClass.userOnline, lectureID,requestType);
					//解析和处理服务器返回的数据
					lectureRequestResult = Utility.handleLectureResponse(response, mLectureDao);
					//处理结果
					Message message=new Message();
					message.what=1;
					message.arg1=requestType;
					handler.sendMessageDelayed(message, 1500);
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

/*	//将讲座显示到界面中
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
					LectureRequest(ConstantClass.REQUEST_NEW,0);
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
					RecommentLectureRequest();
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
								//获取数据库中最后一条讲座 id
								long lastLecureID = Utility.lastLetureinDB(mLectureDao);
								LectureRequest(ConstantClass.REQUEST_NEW,lastLecureID);
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
								//获取数据库中最后一条讲座 id
								long lastLecureID = Utility.lastLetureinDB(mLectureDao);
								LectureRequest(ConstantClass.REQUEST_NEW,lastLecureID);

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
			}
		}
		DialogUtils.closeDialog(requestLoadDialog);
	}*/

	private void RecommentLectureRequest() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String recommentResponse = HttpUtil.RecommentRequest(ConstantClass.ADDRESS, ConstantClass.RECOMMENT_COM, ConstantClass.userOnline);

					recommentOrderResult = Utility.handleRecommentLectureResponse(recommentResponse, mUserDao);

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

	public void showNewLectureToTop() {
		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		String temp = user.getRecommentLectureOrder();
		Logger.d("讲座的推荐顺序为："+temp);
		if(temp==null){
			RecommentLectureRequest();
			return;
		}
		String[] recommentOrder = new String[0];
		if (temp != null && !temp.isEmpty()) {
			recommentOrder = Utility.getStrings(temp);
		}
		Logger.d("上次浏览到 "+lastViewedLectureID);
		//如果是没有浏览记录并且没有本次使用请求过讲座
		if (lastViewedLectureID == 0 && haveRequestedLecture==false) {
			haveRequestedLecture=true;
			Logger.d("没有浏览记录并且没有本次使用请求过讲座");
			loadMoreLectureToBottom=false;
			LectureRequest(ConstantClass.REQUEST_FIRST,0);
			return;

		} else if(haveRequestedLecture==true &&lastViewedLectureID==0){
			//如果没有浏览记录但已经请求了讲座
			Logger.d("没有浏览记录但已经请求了讲座");
			lecturelist.addAll(mLectureDao.loadAll());
			lastViewedLectureID=lecturelist.get(0).getLectureId();
		}else {
			//如果有浏览记录，判断是否已经有讲座被展示
			int position = getLecturePosInRecommentList(lastViewedLectureID);
			Logger.d("上次看到的讲座在推荐顺序表中的位置为 "+position);
			Logger.d("lectureListd的长度为 "+lecturelist.size());
			//如果有讲座被展示，就从展示的第一个讲座在推荐顺序中的位置开始，继续展示七个推荐顺序更靠前的讲座
			if(lecturelist.size()!=0){
				//每次加载七条讲座，如果不够七条就加载全部（i-上一次浏览到的讲座的ID在推荐讲座列表中的位置，i--表示加载推荐顺序靠前的讲座
				// consts记录推进的次数，每次最多推进七条）
				if(position==0){
					handler.sendEmptyMessage(3);
					return;
				}
				for (int i = position-1, consts = 1; i >= 0 && consts <= 7; i--, consts++) {
					LectureDB lectureDB = mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(recommentOrder[i])).build().unique();
					//如果数据库中没有该条讲座，请求返回最新讲座
					if (lectureDB == null) {
						loadMoreLectureToBottom=false;
						LectureRequest(ConstantClass.REQUEST_OLD,Long.parseLong(recommentOrder[i+1]));
						return;
					} else {
						Logger.d("加载第"+consts+"个更新的讲座，ID为 "+lectureDB.getLectureId());
						//将数据库查到的讲座添加到讲座列表中
						lecturelist.add(0, lectureDB);
					}
				}
				lastViewedLectureID = lecturelist.get(0).getLectureId();
			}else{
				//如果没有讲座被展示，就从上次浏览到的讲座在推荐顺序中的位置开始，添加七个上次浏览过的讲座
				for(int i=position,consts=1;i<recommentOrder.length&&consts<=7;i++,consts++){
					LectureDB lectureDB=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(recommentOrder[i])).build().unique();
					if (lectureDB == null) {
						loadMoreLectureToBottom=false;
						LectureRequest(ConstantClass.REQUEST_NEW,Long.parseLong(recommentOrder[i]));
						return;
					} else {
						Logger.d("加载第"+consts+"个之前看过的讲座，ID为 "+lectureDB.getLectureId());
						//将数据库查到的讲座添加到讲座列表中
						lecturelist.add(lectureDB);
					}
				}
			}
		}
		handler.sendEmptyMessage(5);
		mAdapter.notifyDataSetChanged();
	}

	public void showMoreLectureToBottom(){
		if (lecturelist.size()==0){
			showNewLectureToTop();
			return;
		}else{
			UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
			String temp = user.getRecommentLectureOrder();
			Logger.d("讲座的推荐顺序为："+temp);

			String[] recommentOrder = new String[0];
			if (temp != null && !temp.isEmpty()) {
				recommentOrder = Utility.getStrings(temp);
			}

			long lastLectureInList=lecturelist.get(lecturelist.size()-1).getLectureId();
			int viewedPosition=lecturelist.size()-1;
			int position=getLecturePosInRecommentList(lastLectureInList);
			if(position==recommentOrder.length-1){
				handler.sendEmptyMessage(4);
				return;
			}
			Logger.d("最后一个讲座在推荐列表中的位置为："+position);
			for(int i=position+1,consts=1;i<recommentOrder.length&&consts<=7;i++,consts++){
				LectureDB lectureDB=mLectureDao.queryBuilder().where(LectureDBDao.Properties.LectureId.eq(recommentOrder[i])).build().unique();
				if (lectureDB == null) {
					Logger.d("向服务器请求ID为："+recommentOrder[i]+"的讲座");
					loadMoreLectureToBottom=true;
					LectureRequest(ConstantClass.REQUEST_NEW,Long.parseLong(recommentOrder[i-1]));
					return;
				} else {
					Logger.d("加载第"+consts+"个更多的讲座，ID为 "+lectureDB.getLectureId());
					//将数据库查到的讲座添加到讲座列表中
					lecturelist.add(lectureDB);
				}
			}
			handler.sendEmptyMessage(5);
			mAdapter.notifyDataSetChanged();
			listView.setSelection(viewedPosition);
		}
	}

	public int getLecturePosInRecommentList(long lectureID) {
		int position = -1;
		UserDB user = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		String temp = user.getRecommentLectureOrder();
		if (temp != null && temp.length()!=0) {
			String[] recommentOrder = Utility.getStrings(temp);

			for (int i = 0; i < recommentOrder.length; i++) {
				if (String.valueOf(lectureID).equals(recommentOrder[i])) {
					position = i;
					break;
				}
			}
		}
		return position;
	}
}
