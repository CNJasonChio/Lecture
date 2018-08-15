package com.jasonchio.lecture;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jasonchio.lecture.greendao.DynamicsDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.util.ConstantClass;
import com.jasonchio.lecture.util.DialogUtils;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import me.codeboy.android.aligntextview.AlignTextView;
import me.codeboy.android.aligntextview.CBAlignTextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;

public class AboutActivity extends BaseActivity {

	AlignTextView aboutText;             //关于界面的正文

	TitleLayout titleLayout;        //关于界面的标题栏

	Button titleFirstButton;        //标题栏的第一个按钮

	String aboutContent;

	Handler handler;
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

		getAboutContent(ConstantClass.ABOUT_ADDRESS,handler);
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("关于");
	}

	@Override
	void initWidget() {
		aboutText = (AlignTextView) findViewById(R.id.about_content_text);
		titleLayout = (TitleLayout) findViewById(R.id.about_title_layout);
		titleFirstButton = (Button) findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						aboutContent=msg.obj.toString();
						aboutText.setText(aboutContent);
						break;
					default:
				}
				return true;
			}
		});
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

	public static void getAboutContent(String address, final Handler handler) {
		ContentRequest(address, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Logger.d("content获取失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Message message=new Message();
				message.what=1;
				message.obj=response.body().string();
				handler.sendMessage(message);
			}
		});
	}
}