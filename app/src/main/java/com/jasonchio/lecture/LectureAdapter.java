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

	public LectureAdapter(Context context, int lectureItemId, List<LectureDB> objects){
		super(context,lectureItemId,objects);
		this.lectureItemId= lectureItemId;
	}
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		LectureDB lecture=getItem(position);
		View view= LayoutInflater.from(getContext()).inflate(lectureItemId,parent,false);
		TextView lectureTitle=(TextView)view.findViewById(R.id.lecture_title_text);
		ImageView lectureImage=(ImageView)view.findViewById(R.id.lecture_source_image);
		TextView lectureContent=(TextView)view.findViewById(R.id.lecture_content_text);
		TextView lectureTime=(TextView)view.findViewById(R.id.lecture_time_text);
		TextView lectureSource=(TextView)view.findViewById(R.id.lecture_source_text);
		TextView lectureLikers=(TextView)view.findViewById(R.id.lecture_likers_text);

		lectureTitle.setText(lecture.getLectureTitle());
		/*讲座效果图，待修复
		lectureImage.setImageResource(lecture.getLectureImage());
		*/
		lectureContent.setText(lecture.getLectureContent());
		lectureTime.setText(lecture.getLectureTime());
		lectureSource.setText(lecture.getLecutreSource());
		lectureLikers.setText(String.valueOf(lecture.getLecutreLikers()));

		return view;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}
}
