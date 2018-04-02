package com.jasonchio.lecture;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasonchio.lecture.database.LibraryDB;

import java.util.List;

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

public class FocuseLibraryAdapter extends ArrayAdapter<LibraryDB> {

	private int libraryItemId;

	public FocuseLibraryAdapter(Context context, int lectureItemId, List<LibraryDB> objects){
		super(context,lectureItemId,objects);
		this.libraryItemId= lectureItemId;
	}
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		LibraryDB library=getItem(position);

		View view= LayoutInflater.from(getContext()).inflate(libraryItemId,parent,false);

		TextView libraryName=(TextView)view.findViewById(R.id.myfocuse_text);

		libraryName.setText(library.getLibraryName());

		return view;
	}
}
