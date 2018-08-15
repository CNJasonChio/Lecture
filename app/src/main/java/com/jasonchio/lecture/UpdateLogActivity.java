package com.jasonchio.lecture;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jasonchio.lecture.util.ConstantClass;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import me.codeboy.android.aligntextview.AlignTextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jasonchio.lecture.util.HttpUtil.ContentRequest;

public class UpdateLogActivity extends BaseActivity {
	
	AlignTextView updateLogText;
	TitleLayout titleLayout;
	Button titleFirstButton;

	Handler handler;

	String updateLogContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_log);

		initWidget();

		initView();

		initEvent();

		getUpdateLogContent(ConstantClass.UPDATELOG_ADDRESS,handler);
	}

	@Override
	void initView() {
		HideSysTitle();
		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("更新日志");
	}

	@Override
	void initWidget() {
		updateLogText =(AlignTextView) findViewById(R.id.update_log_content_text);
		titleLayout=(TitleLayout)findViewById(R.id.update_log_title_layout);
		titleFirstButton=(Button)findViewById(R.id.title_first_button);
	}

	@Override
	void initEvent() {
		titleFirstButton.setOnClickListener(this);

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						updateLogContent =msg.obj.toString();
						updateLogText.setText(updateLogContent);
						break;
					default:
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.title_first_button:
				finish();
				break;
			default:
		}
	}

	public static void getUpdateLogContent(String address, final Handler handler) {
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
