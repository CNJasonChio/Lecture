package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	TextView aboutText;             //关于界面的正文

	TitleLayout titleLayout;        //关于界面的标题栏

	Button titleFirstButton;        //标题栏的第一个按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		//初始化控件
		initWidget();
		//初始化视图
		initView();
		//初始化点击响应事件
		initEvent();
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("关于");
		aboutText.setText("\t整合武汉各大发布源的发布讲座信息，内容均来自官网并附原网页链接，可以放心查看。\n" + "\t人性化讲座收藏、图书馆关注功能，让你更方便留意喜爱的讲座信息。");
	}

	@Override
	void initWidget() {
		aboutText = (TextView) findViewById(R.id.about_content_text);
		titleLayout = (TitleLayout) findViewById(R.id.about_title_layout);
		titleFirstButton = (Button) findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			//标题栏的第一个按钮
			case R.id.title_first_button:
				finish();
				break;
			default:
		}
	}
}

