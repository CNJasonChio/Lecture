package com.jasonchio.lecture;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.SettingService;

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

	double longtituide;                     //用户经度
	double latituide;                       //用户纬度

	String location;                        //用户所在区县

	boolean sendPosOk = false;                //发送位置信息完成

	int sendPositionResult = 0;               //向服务器发送用户位置的结果

	Rationale mRationale;                   //请求权限被拒绝多次后的提示对象

	LocationClient locationClient;          //定位

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
		List <LectureDB> lectureDBList = null;
		if(user.getUserLocation()!=null){
			lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).where(LectureDBDao.Properties.LectureDistrict.like("%"+user.getUserLocation()+"%")).build().list();
			if(lectureDBList.isEmpty()){
				LectureRequest();
				return;
			}
		}else{
			Toasty.info(getContext(),"还没有您的位置信息，点击左上角开始定位").show();
			/*lectureDBList = mLectureDao.queryBuilder().offset(mAdapter.getCount()).limit(7).orderDesc(LectureDBDao.Properties.LectureId).build().list();
			//如果数据库中没有待显示的讲座，则向服务器请求
			if (lectureDBList.size() < 1) {
				LectureRequest();
				return;
			}*/
			return;
		}
		DialogUtils.closeDialog(requestLoadDialog);
		if(lectureDBList==null){
			Toasty.error(getContext(),"暂无附近讲座推荐").show();
		}else{
			lecturelist.addAll(0, lectureDBList);
			listView.setSelection(0);
			mAdapter.notifyDataSetChanged();
		}
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
				requestLoadDialog=DialogUtils.createLoadingDialog(getContext(),"正在加载");
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
							showLectureInfoToTop();
						} else if (lectureRequestResult == 1) {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "没有更多附近讲座推荐了").show();
						} else {
							DialogUtils.closeDialog(requestLoadDialog);
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
					case 2:
						if (sendPositionResult == 0) {
							if (sendPosOk == false) {
								Toasty.success(getContext(), "定位成功").show();
								saveUserPosition();
								if(getUserVisibleHint()){
									autoRefresh();
								}
								sendPosOk = true;
							}
						} else {
							Toasty.error(getContext(), "定位失败，请稍候再试或联系开发者").show();
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

	@Override
	public void fetchData() {
		//检查权限
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
			askforPermisson();
		} else {
			getPosition();
		}
	}

	//申请权限
	private void askforPermisson() {
		AndPermission.with(this).permission(Permission.Group.LOCATION).rationale(mRationale).onGranted(new Action() {
			@Override
			public void onAction(List <String> permissions) {
				getPosition();
			}
		}).onDenied(new Action() {

			@Override
			public void onAction(List <String> permissions) {
				Toasty.error(getContext(), "获取定位权限失败");
				if (AndPermission.hasAlwaysDeniedPermission(getContext(), permissions)) {
					// 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。
					final SettingService settingService = AndPermission.permissionSetting(getContext());
					android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
					builder.setTitle("权限请求");
					builder.setMessage("小的需要您的定位权限来为您推荐附近的讲座，不然小主可能跑很远才能看到想看的讲座");
					builder.setCancelable(true);
					builder.setPositiveButton("准了", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 如果用户同意去设置：
							settingService.execute();
						}
					});
					builder.setNegativeButton("还是不准", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 如果用户不同意去设置：
							settingService.cancel();
						}
					});
					builder.show();
				}

			}
		}).start();

		mRationale = new Rationale() {
			@Override
			public void showRationale(Context context, List <String> permissions, final RequestExecutor executor) {
				// 这里使用一个Dialog询问用户是否继续授权。
				android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
				builder.setTitle("权限请求");
				builder.setMessage("小的需要您的定位权限来为您推荐附近的讲座，不然小主可能跑很远才能看到想看的讲座");
				builder.setCancelable(true);
				builder.setPositiveButton("准了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果用户继续：
						executor.execute();
					}
				});
				builder.setNegativeButton("还是不准", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 如果用户中断：
						executor.cancel();
					}
				});
				builder.show();

			}
		};
	}

	//保存用户的位置信息
	private void saveUserPosition() {
		UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		userDB.setUserLocation(location);
		Logger.d(location);
		userDB.setUserLongitude(longtituide);
		userDB.setUserLatitude(latituide);
		mUserDao.update(userDB);
	}

	//向服务器发送位置信息
	private void SendPosition() {
		sendPosOk = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String response = HttpUtil.SendPosition(ConstantClass.ADDRESS, ConstantClass.SEND_POSITION_COM, ConstantClass.userOnline, longtituide, latituide);

					sendPositionResult = Utility.handleCommonResponse(response);

					handler.sendEmptyMessage(2);

				} catch (IOException e) {
					Logger.d("连接失败，IO error");
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	//定位监听器
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			Logger.d("获取位置信息");
			longtituide = bdLocation.getLongitude();
			latituide = bdLocation.getLatitude();
			location = bdLocation.getDistrict();
			SendPosition();
		}
	}

	//获取用户位置
	private void getPosition() {
		locationClient = new LocationClient(getContext());
		locationClient.registerLocationListener(new MyLocationListener());
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setIsNeedAddress(true);
		locationClient.setLocOption(locationClientOption);
		locationClient.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationClient != null) {
			locationClient.stop();
		}
	}
}

