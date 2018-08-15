package com.jasonchio.lecture;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import com.bumptech.glide.Glide;
import com.jasonchio.lecture.greendao.CommentDB;
import com.jasonchio.lecture.greendao.LectureDB;
import com.jasonchio.lecture.greendao.LectureDBDao;
import com.jasonchio.lecture.util.TimeUtil;
import com.jasonchio.lecture.util.Utility;
import com.orhanobut.logger.Logger;

import java.util.List;

import me.codeboy.android.aligntextview.AlignTextView;

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

public class LectureAdapter extends BaseAdapter {

	private List<LectureDB> lectureDBList;  //讲座列表

	private Context context;                //上下文

	public LectureAdapter(Context context,List<LectureDB> lectureList){
		this.lectureDBList=lectureList;
		this.context=context;
	}

	@Override
	public View getView(int position, View view,ViewGroup parent) {

		final ViewHolder viewHolder;
		LectureDB lecture=lectureDBList.get(position);

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
		viewHolder.lectureTime.setText(TimeUtil.getTimeFormatText(lecture.getLectureTime()));
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

		TextView lectureTitle;          //讲座标题
		ImageView lectureImage;         //讲座图片
		TextView lectureContent;        //讲座正文
		TextView lectureTime;           //讲座时间
		TextView lectureSource;         //讲座来源
		TextView lectureLikers;         //讲座收藏人数
		ImageView lectureWantedImage;   //讲座是否被收藏对应的图片

	}

	@Override
	public int getCount() {
		return lectureDBList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


}
