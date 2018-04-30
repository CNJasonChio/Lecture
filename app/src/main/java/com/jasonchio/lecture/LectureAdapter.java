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

public class LectureAdapter extends BaseAdapter {

	private List<LectureDB> lectureDBList;
	private Context context;
	private ListView listView;
	private LectureDBDao mLectureDao;

	public LectureAdapter(Context context, ListView listView, List<LectureDB> lectureList, LectureDBDao mLectureDao){
		this.lectureDBList=lectureList;
		this.listView=listView;
		this.context=context;
		this.mLectureDao=mLectureDao;
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

/*	public void changeLectureWanted(int position, int isWanted){

		Message msg=Message.obtain();
		msg.arg1=position;
		if(isWanted==0){
			//viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like);
			msg.arg2=R.drawable.ic_lecture_likes;
		}else{
			//viewHolder.commentLikersImage.setImageResource(R.drawable.ic_discovery_comment_like_selected);
			msg.arg2=R.drawable.ic_myinfo_mywanted;
		}

		LectureDB lectureDB=lectureDBList.get(position);
		if(isWanted==1){
			lectureDB.setLecutreLikers(lectureDB.getLecutreLikers()+1);
			mLectureDao.update(lectureDB);
		}else {
			lectureDB.setLecutreLikers(lectureDB.getLecutreLikers()-1);
			mLectureDao.update(lectureDB);
		}
		lectureDBList.set(position,lectureDB);
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			updateItem(msg.arg1,msg.arg2);
		}
	};

	*//**、
	 * 刷新指定item
	 *
	 * @param index item在listview中的位置
	 *//*
	private void updateItem(int index,int drawable)
	{
		if (listView == null)
		{
			return;
		}

		// 获取当前可以看到的item位置
		int visiblePosition = listView.getFirstVisiblePosition();

		// 如添加headerview后 firstview就是hearderview
		// 所有索引+1 取第一个view
		// View view = listview.getChildAt(index - visiblePosition + 1);
		// 获取点击的view

		View view = listView.getChildAt(index - visiblePosition);

		ImageView likeImage=view.findViewById(R.id.lecture_wanted_image);

		likeImage.setImageResource(drawable);
	}*/
}
