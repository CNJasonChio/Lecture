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
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.DaoSession;
import com.jasonchio.lecture.greendao.UserDB;
import com.jasonchio.lecture.greendao.UserDBDao;
import com.jasonchio.lecture.util.CircleImageView;
import com.jasonchio.lecture.util.ConstantClass;
import com.orhanobut.logger.Logger;

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

public class  MeFragment extends Fragment{

	TitleLayout titleLayout;
	LinearLayout meLayout;
	RelativeLayout mywantedLayout;
	RelativeLayout myfocuseLayout;
	RelativeLayout mycommentLayout;
	Button titleFirstButton;
	TextView userName;
	CircleImageView userheadImage;
	DaoSession daoSession;
	UserDBDao mUserDao;

	private View rootview;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		//在fragment onCreateView 里缓存View，防止每次onCreateView 的时候重绘View
		if(rootview == null){
			rootview=inflater.inflate(R.layout.fragment_me,null);
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
		userName=(TextView)rootview.findViewById(R.id.me_username_text);
		userheadImage=(CircleImageView)rootview.findViewById(R.id.me_userhead_image);
		titleFirstButton=titleLayout.getFirstButton();
		daoSession=((MyApplication)getActivity().getApplication()).getDaoSession();
		mUserDao=daoSession.getUserDBDao();

		titleLayout.setTitle("我的资料");
		titleLayout.setFirstButtonBackground(R.drawable.ic_title_settings);
		titleLayout.setSecondButtonVisible(View.GONE);

		UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();

		if(user==null){
			Logger.d("user is null");
		}
		if(user.getUserName()==null){
			userName.setText("讲座萌新");
		}else {
			userName.setText(user.getUserName());
		}
		if(user.getUserPhotoUrl()==null){
			userheadImage.setImageResource(R.drawable.ic_defult_userhead);
		}else{
			Glide.with(getActivity()).load(user.getUserPhotoUrl()).into(userheadImage);
		}
		titleFirstButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),SettingActivity.class);
				startActivity(intent);
			}
		});
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

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			UserDB user=mUserDao.queryBuilder().where(UserDBDao.Properties.UserId.eq(ConstantClass.userOnline)).build().unique();
			String userHead=user.getUserPhotoUrl();
			if(userHead.length()!=0 && userHead!=null){
				Glide.with(getActivity()).load(userHead).into(userheadImage);
			}else{
				userheadImage.setImageResource(R.mipmap.ic_launcher);
			}
			userName.setText(user.getUserName());
		}
	}

}
