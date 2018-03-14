package com.jasonchio.lecture;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MyInfoActivity extends AppCompatActivity {

	String[] sexArray=new String[]{"蓝孩纸","吕孩纸","不告诉他们"};

	RelativeLayout photoLayout;

	RelativeLayout nameLayout;

	RelativeLayout sexLayout;

	RelativeLayout schoolLayout;

	RelativeLayout birthdayLayout;

	ImageView photoImage;

	TextView nameText;

	TextView sexText;

	TextView schoolText;

	TextView birthdayText;

	TitleLayout titleLayout;

	Button titleFirstButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);

		//隐藏自带标题栏
		if (Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}

		titleLayout=(TitleLayout)findViewById(R.id.myinfo_title_layout);
		photoLayout=(RelativeLayout)findViewById(R.id.myinfo_profilephoto_layout);
		nameLayout=(RelativeLayout)findViewById(R.id.myinfo_name_layout);
		sexLayout=(RelativeLayout)findViewById(R.id.myinfo_sex_layout);
		schoolLayout=(RelativeLayout)findViewById(R.id.myinfo_school_layout);
		birthdayLayout=(RelativeLayout)findViewById(R.id.myinfo_birthday_layout);

		photoImage=(ImageView)findViewById(R.id.myinfo_photo_image);
		nameText=(TextView)findViewById(R.id.myinfo_name_text);
		sexText=(TextView)findViewById(R.id.myinfo_sex_text);
		schoolText=(TextView)findViewById(R.id.myinfo_school_text);
		birthdayText=(TextView)findViewById(R.id.myinfo_birthday_text);
		titleFirstButton=titleLayout.getFirstButton();

		titleLayout.setSecondButtonVisible(View.GONE);
		titleLayout.setTitle("我的资料");

		photoLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MyInfoActivity.this,"clicked photo_layout",Toast.LENGTH_SHORT).show();
			}
		});
		nameLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCreateNameDialog();
			}
		});
		sexLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSexChooseDialog();
			}
		});
		schoolLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSetSchool();
			}
		});
		birthdayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar nowdate = Calendar.getInstance();
				final int mYear = nowdate.get(Calendar.YEAR);
				final int mMonth = nowdate.get(Calendar.MONTH);
				final int mDay = nowdate.get(Calendar.DAY_OF_MONTH);
				//调用DatePickerDialog
				new DatePickerDialog(MyInfoActivity.this, onDateSetListener, mYear, mMonth, mDay).show();
			}
		});

		titleFirstButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 日期选择器对话框监听
	 */
	private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;

			String days;
			days = new StringBuffer().append(mYear).append("年").append(mMonth).append("月").append(mDay).append("日").toString();
			birthdayText.setText(days);
		}
	};

	private void onSetSchool() {
		// 使用LayoutInflater来加载dialog_setname.xml布局
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View nameView = layoutInflater.inflate(R.layout.myinfo_dialog_setname, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// 使用setView()方法将布局显示到dialog
		alertDialogBuilder.setView(nameView)
				.setTitle("请输入您的学校")
				.setMessage("例：武汉理工大学、华中科技大学");

		final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);

		// 设置Dialog按钮
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 获取edittext的内容,显示到textview
								schoolText.setText(userInput.getText());
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void onCreateNameDialog() {
		// 使用LayoutInflater来加载dialog_setname.xml布局
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View nameView = layoutInflater.inflate(R.layout.myinfo_dialog_setname, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		// 使用setView()方法将布局显示到dialog
		alertDialogBuilder.setView(nameView)
				.setTitle("请输入昵称");

		final EditText userInput = (EditText) nameView.findViewById(R.id.changename_edit);

		// 设置Dialog按钮
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// 获取edittext的内容,显示到textview
								nameText.setText(userInput.getText());
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void showSexChooseDialog() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
		builder3.setSingleChoiceItems(sexArray, 0, new DialogInterface.OnClickListener() {// 2默认的选中

			@Override
			public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
				// showToast(which+"");
				sexText.setText(sexArray[which]);
				dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
			}
		});
		builder3.show();// 让弹出框显示
	}

}
