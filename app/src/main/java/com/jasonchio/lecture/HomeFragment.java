package com.jasonchio.lecture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.baidu.location.LocationClient;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.yanzhenjie.permission.Rationale;

import q.rorbin.badgeview.QBadgeView;

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

	EditText titleSearchEdit;

	ImageView titleSearchImage;

	ImageView titleNoticeImage;

	PagerSlidingTabStrip tabStrip;          //导航栏滑动

	DisplayMetrics displayMetrics;          //显示单位

	SearchTitleLayout titleLayout;          //标题栏

	Handler handler;                        //handler

	boolean sendPosOk = false;              //发送位置信息完成

	double longtituide;                     //用户经度
	double latituide;                       //用户纬度

	String location;                        //用户所在区县

	View rootView;                          //根视图

	DaoSession daoSession;                  //数据库操作对象

	UserDBDao mUserDao;                     //用户表操作对象

	LocationClient locationClient;          //定位

	Rationale mRationale;                   //请求权限被拒绝多次后的提示对象

	int sendPositionResult = 0;              //向服务器发送用户位置的结果

	QBadgeView badgeView;                   //消息数量对象

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
		displayMetrics = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);// 设置缓存页面，当前页面的相邻两个页面都会被缓存
		tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		setTabValue();
		tabStrip.setViewPager(pager);

		titleSearchEdit.setFocusable(false);
		titleSearchEdit.setFocusableInTouchMode(false);
	}

	@Override
	void initWidget() {
		titleLayout = (SearchTitleLayout) rootView.findViewById(R.id.home_title_layout);
		titleNoticeImage=rootView.findViewById(R.id.search_title_notice_image);
		titleNoticeImage.setVisibility(View.GONE);
		titleSearchImage=rootView.findViewById(R.id.search_title_search_image);
		titleSearchEdit=rootView.findViewById(R.id.title_search_edit);
		daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();
		mUserDao = daoSession.getUserDBDao();
		//badgeView= (QBadgeView) new QBadgeView(getContext()).bindTarget(titleNoticeImage).setBadgeNumber(12).setBadgePadding(3,true);
	}

	@Override
	void initEvent() {
		/*handler = new Handler(new Handler.Callback() {
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
		});*/

		titleSearchImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), SearchActivity.class);
				startActivity(intent);
			}
		});

		titleNoticeImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getContext(),NoticeActivity.class);
				startActivity(intent);
			}
		});

		titleSearchEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), SearchActivity.class);
				startActivity(intent);
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

/*	//获取用户位置
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
	}*/

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationClient != null) {
			locationClient.stop();
		}
	}
}
