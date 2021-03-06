package com.jasonchio.lecture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.jasonchio.lecture.util.NetStateUtil;

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
 * Created by zhaoyaobang on 2018/6/20.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
	public NetChangeListener listener = BaseActivity.listener;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 如果相等的话就说明网络状态发生了变化
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			int netWorkState = NetStateUtil.getNetWorkState(context);
			// 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
			if (listener != null) {
				listener.onChangeListener(netWorkState);
			}
		}
	}

	// 自定义接口
	public interface NetChangeListener {
		void onChangeListener(int status);
	}

}
