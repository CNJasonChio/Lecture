package com.jasonchio.lecture;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.ConstantClass;
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
 * Created by zhaoyaobang on 2018/3/8.
 */


public class HomeFragment extends BaseFragment {

	RecommentFragment fragmentRecommend;    //推荐 fragment

	FocuseFragment fragmentFoucse;          //关注fragment

	NearFragment fragmentNear;              //附近 fragment

	Button titleSecondButton;               //标题栏的第二个按钮

	Button titleFirstButton;                //标题栏的第一个按钮

	PagerSlidingTabStrip tabStrip;          //导航栏

	DisplayMetrics displayMetrics;          //显示单位

	TitleLayout titleLayout;                //标题栏

	Handler handler;                        //handler

	boolean sendPosOk = false;                //发送位置信息完成

	double longtituide;                     //用户经度
	double latituide;                       //用户纬度

	String location;                        //用户所在区县

	View rootView;                          //根视图

	DaoSession daoSession;                  //数据库操作对象

	UserDBDao mUserDao;                     //用户表操作对象

	LocationClient locationClient;          //定位

	Rationale mRationale;                   //请求权限被拒绝多次后的提示对象

	int sendPositionResult = 0;               //向服务器发送用户位置的结果

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public void fetchData() {

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_home, null);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化响应事件
		initEvent();

		return rootView;
	}

	//设置导航栏属性
	private void setTabValue() {
		// 设置Tab是自动填充满屏幕的
		tabStrip.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabStrip.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));
		tabStrip.setUnderlineColor(Color.argb(255, 254, 205, 127));
		// 设置Tab Indicator的高度
		tabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics));// 4
		// 设置Tab标题文字的大小
		tabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, displayMetrics)); // 16
		// 设置Tab Indicator的颜色
		tabStrip.setIndicatorColor(Color.parseColor("#FA9D25"));// #45c01a
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		//tabStrip.setSelectedTextColor(Color.parseColor("#45c01a"));// #45c01a
		// 取消点击Tab时的背景色
		tabStrip.setTabBackground(0);
	}

	@Override
	void initView() {
		titleLayout.setTitle("首页");
		titleLayout.setSecondButtonBackground(R.drawable.ic_title_search);
		titleLayout.setFirstButtonBackground(R.drawable.ic_title_location);
		displayMetrics = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);// 设置缓存页面，当前页面的相邻两个页面都会被缓存
		tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		setTabValue();
		tabStrip.setViewPager(pager);
	}

	@Override
	void initWidget() {
		titleLayout = (TitleLayout) rootView.findViewById(R.id.home_title_layout);
		titleSecondButton = titleLayout.getSecondButton();
		titleFirstButton = titleLayout.getFirstButton();
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mUserDao = daoSession.getUserDBDao();
	}

	@Override
	void initEvent() {
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						if (sendPositionResult == 0) {
							if (sendPosOk == false) {
								saveUserPosition();
								sendPosOk = true;
							}
						} else {
							Toasty.error(getContext(), "定位失败，请稍候再试或联系开发者").show();
						}
						break;
						default:
				}
				return true;
			}
		});

		titleSecondButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), SearchActivity.class);
				startActivity(intent);
			}
		});

		titleFirstButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
					askforPermisson();
				} else {
					getPosition();
				}

			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	//导航栏适配器
	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		private final String[] titles = {"推荐", "关注", "附近"};

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:

					if (null == fragmentRecommend) {
						fragmentRecommend = new RecommentFragment();
					}

					return fragmentRecommend;

				case 1:

					if (null == fragmentFoucse) {
						fragmentFoucse = new FocuseFragment();
					}

					return fragmentFoucse;
				case 2:

					if (null == fragmentNear) {
						fragmentNear = new NearFragment();
					}
					fragmentRecommend = new RecommentFragment();
					return fragmentNear;
				default:
					return null;
			}
		}

	}

	//获取用户位置
	private void getPosition() {
		locationClient = new LocationClient(getContext());
		locationClient.registerLocationListener(new HomeFragment.MyLocationListener());
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setIsNeedAddress(true);
		locationClient.setLocOption(locationClientOption);
		locationClient.start();
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

	//向服务器发送位置信息
	private void SendPosition() {
		sendPosOk = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String response = HttpUtil.SendPosition(ConstantClass.ADDRESS, ConstantClass.SEND_POSITION_COM, ConstantClass.userOnline, longtituide, latituide);

					sendPositionResult = Utility.handleCommonResponse(response);

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

	//保存用户的位置信息
	private void saveUserPosition() {
		UserDB userDB = mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
		userDB.setUserLocation(location);
		Logger.d(location);
		userDB.setUserLongitude(longtituide);
		userDB.setUserLatitude(latituide);
		mUserDao.update(userDB);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationClient != null) {
			locationClient.stop();
		}
	}
}
