package com.jasonchio.lecture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.jasonchio.lecture.greendao.LibraryDB;
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

public class FocuseLibraryAdapter extends BaseAdapter implements View.OnClickListener {

	private List<String> libraryList;
	private Context context;
	private FocuseLibraryAdapter.InnerItemOnclickListener listener;

	public FocuseLibraryAdapter(List<String> list,Context context){
		this.libraryList =list;
		this.context=context;
	}

	@Override
	public int getCount() {
		return libraryList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final FocuseLibraryAdapter.ViewHolder viewHolder;
		final String libraryName= libraryList.get(position);

		if (view == null) {
			viewHolder = new FocuseLibraryAdapter.ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.myfoucse_listitem,
					null);
			viewHolder.addCancelButton=(Button)view.findViewById(R.id.myfocuse_add_cancel_button);
			viewHolder.myFocuseLibrary=(TextView)view.findViewById(R.id.myfocuse_text);
			view.setTag(viewHolder);
		} else {
			viewHolder = (FocuseLibraryAdapter.ViewHolder) view.getTag();
		}
		viewHolder.addCancelButton.setOnClickListener(this);
		viewHolder.addCancelButton.setTag(position);
		viewHolder.myFocuseLibrary.setOnClickListener(this);
		viewHolder.myFocuseLibrary.setTag(position);

		viewHolder.myFocuseLibrary.setText(libraryName);

		return view;
	}

	public final static class ViewHolder {
		Button addCancelButton;
		TextView myFocuseLibrary;
	}

	interface InnerItemOnclickListener {
		void itemClick(View v);
	}

	public void setOnInnerItemOnClickListener(FocuseLibraryAdapter.InnerItemOnclickListener listener){
		this.listener=listener;
	}

	@Override
	public void onClick(View v) {
		listener.itemClick(v);
	}
}
