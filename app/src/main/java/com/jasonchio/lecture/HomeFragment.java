package com.jasonchio.lecture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

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


public class HomeFragment extends Fragment {

	private RecommentFragment fragmentRecommend;
	private FocuseFragment fragmentFoucse;
	private NearFragment fragmentNear;
	private Button titleSecondButton;
	private Button titleFirstButton;
	private PagerSlidingTabStrip tabStrip;

	private DisplayMetrics displayMetrics;

	TitleLayout titleLayout;

	public LocationClient locationClient;

	Handler handler;

	double longtituide;
	double latituide;

	int sendPositionResult=0;

	String response;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_home, null);

		initView(view);

		handler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what){
					case 1:
						if (sendPositionResult == 0) {
							Toasty.success(getContext(), "定位成功").show();

						} else if (sendPositionResult == 1) {
							Toasty.error(getContext(), "数据库无更新").show();
						} else {
							Toasty.error(getContext(), "服务器出错，请稍候再试").show();
						}
						break;
				}
				return true;
			}
		});

		getPosition();

		return view;
	}

	private void initView(View view) {

		titleLayout = (TitleLayout) view.findViewById(R.id.home_title_layout);
		titleLayout.setTitle("首页");
		titleLayout.setSecondButtonBackground(R.drawable.ic_title_search);
		titleSecondButton = titleLayout.getSecondButton();
		titleFirstButton = titleLayout.getFirstButton();
		titleLayout.setFirstButtonBackground(R.drawable.ic_title_location);

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
				getPosition();
			}
		});
		displayMetrics = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);// 设置缓存页面，当前页面的相邻两个页面都会被缓存
		tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		setTabValue();
		tabStrip.setViewPager(pager);
	}

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

	private void SendPosition() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					response = HttpUtil.SendPosition(ConstantClass.ADDRESS, ConstantClass.SEND_POSITION_PORT, ConstantClass.userOnline, longtituide, latituide);

					Logger.json(response);

					sendPositionResult= Utility.handleCommonResponse(response);

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

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			Logger.d("获取位置信息");
			longtituide = bdLocation.getLongitude();
			latituide = bdLocation.getLatitude();
			SendPosition();
		}
	}

	private void getPosition(){
		locationClient = new LocationClient(getContext());
		locationClient.registerLocationListener(new MyLocationListener());
		locationClient.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(locationClient!=null){
			locationClient.stop();
		}
	}
}
