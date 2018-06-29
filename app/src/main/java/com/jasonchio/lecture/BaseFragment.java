package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;

import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.HttpUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.IOException;

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
 * Created by zhaoyaobang on 2018/3/17.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

	protected boolean isViewInitiated;
	protected boolean isVisibleToUser;
	protected boolean isDataInitiated;


	//初始化视图
	abstract void initView();

	//初始化控件
	abstract void initWidget();

	//初始化事件
	abstract void initEvent();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		isViewInitiated = true;
		prepareFetchData();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isVisibleToUser = isVisibleToUser;
		prepareFetchData();
	}

	public abstract void fetchData();

	public boolean prepareFetchData() {
		return prepareFetchData(false);
	}

	public boolean prepareFetchData(boolean forceUpdate) {
		if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
			fetchData();
			isDataInitiated = true;
			return true;
		}
		return false;
	}

	public boolean isVisibleToUser() {
		return isVisibleToUser;
	}

	public void setVisibleToUser(boolean visibleToUser) {
		isVisibleToUser = visibleToUser;
	}


}
