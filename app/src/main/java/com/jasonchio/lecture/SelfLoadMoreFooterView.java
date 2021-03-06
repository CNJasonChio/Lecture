package com.jasonchio.lecture;

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
		setText("已经到底了");
	}

	@Override
	public void onPrepare() {
		setText("");
	}

	@Override
	public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
		if (!isComplete) {
			if (yScrolled <= -getHeight()) {
				setText("放开我就给你新东西");
			} else {
				setText("再往上拉点儿");
			}
		} else {
			setText("");
		}
	}

	@Override
	public void onRelease() {
		setText("");
	}

	@Override
	public void onComplete() {
		setText("终于等到你，还好没放弃");
	}

	@Override
	public void onReset() {
		setText("");
	}
}
