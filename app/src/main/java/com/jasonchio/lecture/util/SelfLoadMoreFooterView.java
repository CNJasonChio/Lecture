package com.jasonchio.lecture.util;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

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
 * Created by zhaoyaobang on 2018/3/10.
 */

public class SelfLoadMoreFooterView extends AppCompatTextView implements SwipeTrigger, SwipeLoadMoreTrigger {
	public SelfLoadMoreFooterView(Context context) {
		super(context);
	}

	public SelfLoadMoreFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onLoadMore() {
		setText("下面还有呢，马上就来");
	}

	@Override
	public void onPrepare() {
		setText("");
	}

	@Override
	public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
		if (!isComplete) {
			if (yScrolled <= -getHeight()) {
				setText("放开我就有新东西了");
			} else {
				setText("想看新东西就往上拉");
			}
		} else {
			setText("我走啦，北北");
		}
	}

	@Override
	public void onRelease() {
		setText("LOADING MORE");
	}

	@Override
	public void onComplete() {
		setText("嘿嘿，来了您那");
	}

	@Override
	public void onReset() {
		setText("");
	}
}
