package com.jasonchio.lecture.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
 * Created by zhaoyaobang on 2018/6/16.
 */


public class NetUtil {
	/**
	 * 没有网络
	 */
	private static final int NETWORK_NONE = -1;
	/**
	 * 移动网络
	 */
	private static final int NETWORK_MOBILE = 0;
	/**
	 * 无线网络
	 */
	private static final int NETWORK_WIFI = 1;

	private NetUtil() {
	}

	/**
	 * 获取网络状态
	 *
	 * @param context
	 * @return one of TYPE_NONE, TYPE_MOBILE, TYPE_WIFI
	 * @permission android.permission.ACCESS_NETWORK_STATE
	 */
	public static int getNetWorkState(Context context) {
		//得到连接管理器对象
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		//如果网络连接，判断该网络类型
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
			if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
				return NETWORK_WIFI;//wifi
			} else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
				return NETWORK_MOBILE;//mobile
			}
		} else {
			//网络异常
			return NETWORK_NONE;
		}
		return NETWORK_NONE;
	}
}

