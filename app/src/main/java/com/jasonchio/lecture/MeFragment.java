package com.jasonchio.lecture;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
 * Created by zhaoyaobang on 2018/3/6.
 */

public class MeFragment extends Fragment{

	TitleLayout titleLayout;
	LinearLayout meLayout;
	RelativeLayout mywantedLayout;
	RelativeLayout myfocuseLayout;
	RelativeLayout mycommentLayout;
	Button titleFirstButton;
	Button titleSecondButton;

	private View rootview;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if(rootview == null){
			rootview=inflater.inflate(R.layout.fragment_me,null);
			Toast.makeText(getActivity(),"FragmentMe==onCreateView",Toast.LENGTH_SHORT ).show();
		}
		ViewGroup parent=(ViewGroup)rootview.getParent();
		if(parent!=null){
			parent.removeView(rootview);
		}

		titleLayout= (TitleLayout)rootview.findViewById(R.id.me_title_layout);
		meLayout=(LinearLayout)rootview.findViewById(R.id.me_myinfo_layout);
		mywantedLayout=(RelativeLayout)rootview.findViewById(R.id.me_mywanted_layout);
		myfocuseLayout=(RelativeLayout)rootview.findViewById(R.id.me_myfocuse_layout);
		mycommentLayout=(RelativeLayout)rootview.findViewById(R.id.me_mycomment_layout);
		titleFirstButton=titleLayout.getFirstButton();
		titleSecondButton=titleLayout.getSecondButton();

		titleLayout.setTitle("我的资料");
		titleLayout.setFirstButtonBackground(R.drawable.ic_title_home);
		titleLayout.setSecondButtonBackground(R.drawable.ic_title_message);

		meLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),MyInfoActivity.class);
				startActivity(intent);
			}
		});

		mywantedLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),MywantedActivity.class);
				startActivity(intent);
			}
		});

		myfocuseLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),MyFocuseActivity.class);
				startActivity(intent);
			}
		});

		mycommentLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),MycommentActivity.class);
				startActivity(intent);
			}
		});
		return rootview;
	}

}
