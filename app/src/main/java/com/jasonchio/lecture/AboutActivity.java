package com.jasonchio.lecture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {

	TextView aboutText;
	TitleLayout titleLayout;
	Button titleFirstButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		initWidget();

		initView();

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
			case R.id.title_first_button:
				finish();
				break;
			default:
		}
	}
}

