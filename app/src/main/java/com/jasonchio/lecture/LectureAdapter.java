package com.jasonchio.lecture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.LectureDB;
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

public class LectureAdapter extends ArrayAdapter<LectureDB> {

	private int lectureItemId;
	private Context context;

	public LectureAdapter(Context context, int lectureItemId, List<LectureDB> objects){
		super(context,lectureItemId,objects);
		this.lectureItemId= lectureItemId;
		this.context=context;
	}
	@Override
	public View getView(int position, View view,ViewGroup parent) {

		final ViewHolder viewHolder;
		LectureDB lecture=getItem(position);

		if(view==null){
			viewHolder = new ViewHolder();
			view= LayoutInflater.from(context).inflate(R.layout.lecure_listitem, null);
			viewHolder.lectureTitle=(TextView)view.findViewById(R.id.lecture_title_text);
			viewHolder.lectureImage=(ImageView)view.findViewById(R.id.lecture_source_image);
			viewHolder.lectureWantedImage=(ImageView)view.findViewById(R.id.lecture_wanted_image);
			viewHolder.lectureContent=(TextView)view.findViewById(R.id.lecture_content_text);
			viewHolder.lectureTime=(TextView)view.findViewById(R.id.lecture_time_text);
			viewHolder.lectureSource=(TextView)view.findViewById(R.id.lecture_source_text);
			viewHolder.lectureLikers=(TextView)view.findViewById(R.id.lecture_likers_text);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.lectureTitle.setText(lecture.getLectureTitle());

		if(lecture.getLectureImage()!=null){
			Glide.with(context).load(lecture.getLectureImage()).into(viewHolder.lectureImage);
		}
		viewHolder.lectureContent.setText(lecture.getLectureContent());
		viewHolder.lectureTime.setText(lecture.getLectureTime());
		if(lecture.getLecutreSource()==null || lecture.getLecutreSource().length()==0) {
			viewHolder.lectureSource.setText("暂无来源名称");
		}else{
			viewHolder.lectureSource.setText(lecture.getLecutreSource());
		}
		viewHolder.lectureLikers.setText(String.valueOf(lecture.getLecutreLikers()));

		if(lecture.getIsWanted()==1){
			viewHolder.lectureWantedImage.setImageResource(R.drawable.ic_myinfo_mywanted);
		}else{
			viewHolder.lectureWantedImage.setImageResource(R.drawable.ic_lecture_likes);
		}
		return view;
	}

	public final static class ViewHolder {

		TextView lectureTitle;
		ImageView lectureImage;
		TextView lectureContent;
		TextView lectureTime;
		TextView lectureSource;
		TextView lectureLikers;
		ImageView lectureWantedImage;

	}
	@Override
	public int getCount() {
		return super.getCount();
	}
}
