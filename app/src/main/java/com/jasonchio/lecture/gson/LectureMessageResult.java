package com.jasonchio.lecture.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
 * Created by zhaoyaobang on 2018/7/2.
 */
public class LectureMessageResult {


	/**
	 * state : 0
	 * messageList : [{"userName":"小猪p7","userHead":"http://119.29.93.31:2000/user/4.jpg","messageId":1,"content":"讲座很棒，受益匪浅呢！！","good_amount":0,"userLike":0},{"userName":" 讲座萌新","userHead":null,"messageId":13,"content":"医学的系统论与整合观","good_amount":1,"userLike":1},{"userName":" 讲座萌新","userHead":null,"messageId":16,"content":"测试一下","good_amount":0,"userLike":0},{"userName":" 讲座萌新","userHead":null,"messageId":17,"content":"测试一下","good_amount":0,"userLike":0}]
	 */

	private int state;
	private List <MessageListBean> messageList;

	public static LectureMessageResult objectFromData(String str) {

		return new Gson().fromJson(str, LectureMessageResult.class);
	}

	public static LectureMessageResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), LectureMessageResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <LectureMessageResult> arrayLectureMessageResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <LectureMessageResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <LectureMessageResult> arrayLectureMessageResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <LectureMessageResult>>() {
			}.getType();

			return new Gson().fromJson(jsonObject.getString(str), listType);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList();


	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List <MessageListBean> getMessageList() {
		return messageList;
	}

	public void setMessageList(List <MessageListBean> messageList) {
		this.messageList = messageList;
	}

	public static class MessageListBean {
		/**
		 * userName : 小猪p7
		 * userHead : http://119.29.93.31:2000/user/4.jpg
		 * messageId : 1
		 * content : 讲座很棒，受益匪浅呢！！
		 * good_amount : 0
		 * userLike : 0
		 */

		private String userName;
		private String userHead;
		private int messageId;
		private String content;
		private int good_amount;
		private int userLike;

		public static MessageListBean objectFromData(String str) {

			return new Gson().fromJson(str, MessageListBean.class);
		}

		public static MessageListBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), MessageListBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List <MessageListBean> arrayMessageListBeanFromData(String str) {

			Type listType = new TypeToken <ArrayList <MessageListBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List <MessageListBean> arrayMessageListBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken <ArrayList <MessageListBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserHead() {
			return userHead;
		}

		public void setUserHead(String userHead) {
			this.userHead = userHead;
		}

		public int getMessageId() {
			return messageId;
		}

		public void setMessageId(int messageId) {
			this.messageId = messageId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getGood_amount() {
			return good_amount;
		}

		public void setGood_amount(int good_amount) {
			this.good_amount = good_amount;
		}

		public int getUserLike() {
			return userLike;
		}

		public void setUserLike(int userLike) {
			this.userLike = userLike;
		}
	}
}
