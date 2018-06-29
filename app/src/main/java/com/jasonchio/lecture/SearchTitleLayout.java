package com.jasonchio.lecture;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by zhaoyaobang on 2018/2/22.
 */

public class SearchTitleLayout extends LinearLayout {

	private Button firstButton;

	private Button secondButton;

	public TextView titleText;

	public SearchTitleLayout(Context context) {
		super(context,null);
	}

	public SearchTitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);

		//引入布局
		LayoutInflater.from(context).inflate(R.layout.title,this);
		firstButton=(Button)findViewById(R.id.title_first_button);
		secondButton=(Button)findViewById(R.id.title_second_button);
		titleText=(TextView)findViewById(R.id.title_text);
	}

	//显示活的的标题
	public void setTitle(String title)
	{
		if(!TextUtils.isEmpty(title))
		{
			titleText.setText(title);
		}
	}

	public void setFirstButtonBackground(int firstButtonBackgroundResource){
		firstButton.setBackgroundResource(firstButtonBackgroundResource);
	}

	public void setSecondButtonBackground(int secondButtonBackgroundResource){
		secondButton.setBackgroundResource(secondButtonBackgroundResource);
	}

	public void setFirstButtonVisible(int visible){
		firstButton.setVisibility(visible);
	}

	public void setSecondButtonVisible(int visible){
		secondButton.setVisibility(visible);
	}

	public Button getFirstButton() {
		return firstButton;
	}

	public void setFirstButton(Button firstButton) {
		this.firstButton = firstButton;
	}

	public Button getSecondButton() {
		return secondButton;
	}

	public void setSecondButton(Button secondButton) {
		this.secondButton = secondButton;
	}


}
