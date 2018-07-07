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
 * Created by zhaoyaobang on 2018/6/30.
 */
public class DynamicsResult {
	/**
	 * state : 0
	 * total : 10
	 * data : [{"id":1,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试","createDate":"2018-07-02 04:59:02","comment_user_like":0,"replyTotal":3,"replyList":[{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":17,"commentId":1,"content":"测试","createDate":"2018-07-05 01:21:14","reply_user_like":0},{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":18,"commentId":3,"content":"《向往的生活》快要大结局了。。","createDate":"2018-07-04 10:23:32","reply_user_like":0},{"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","id":19,"commentId":1,"content":"测试","createDate":"2018-07-05 01:34:23","reply_user_like":0}]},{"id":2,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试","createDate":"2018-07-02 05:23:43","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":3,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试","createDate":"2018-07-02 05:25:49","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":4,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试一下","createDate":"2018-07-02 07:45:29","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":5,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试一下","createDate":"2018-07-02 08:06:08","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":11,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试一下","createDate":"2018-07-02 08:26:40","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":12,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试一下","createDate":"2018-07-02 08:48:39","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":14,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"测试一下","createDate":"2018-07-02 08:48:53","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":15,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"说的很好","createDate":"2018-07-02 08:59:34","comment_user_like":0,"replyTotal":0,"replyList":[]},{"id":16,"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","content":"时间","createDate":"2018-07-02 08:59:48","comment_user_like":0,"replyTotal":0,"replyList":[]}]
	 */

	private int state;
	private int total;
	private List <DataBean> data;

	public static DynamicsResult objectFromData(String str) {

		return new Gson().fromJson(str, DynamicsResult.class);
	}

	public static DynamicsResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), DynamicsResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <DynamicsResult> arrayDynamicsResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <DynamicsResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <DynamicsResult> arrayDynamicsResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <DynamicsResult>>() {
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List <DataBean> getData() {
		return data;
	}

	public void setData(List <DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * id : 1
		 * nickName :  讲座萌新
		 * userLogo : http://119.29.93.31:2000/user/7.jpg
		 * content : 测试
		 * createDate : 2018-07-02 04:59:02
		 * comment_user_like : 0
		 * replyTotal : 3
		 * replyList : [{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":17,"commentId":1,"content":"测试","createDate":"2018-07-05 01:21:14","reply_user_like":0},{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":18,"commentId":3,"content":"《向往的生活》快要大结局了。。","createDate":"2018-07-04 10:23:32","reply_user_like":0},{"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","id":19,"commentId":1,"content":"测试","createDate":"2018-07-05 01:34:23","reply_user_like":0}]
		 */

		private int id;
		private String nickName;
		private String userLogo;
		private String content;
		private String createDate;
		private int comment_user_like;
		private int replyTotal;
		private List <ReplyListBean> replyList;

		public DataBean(String nickName, String content, String createDate) {
			this.nickName = nickName;
			this.content = content;
			this.createDate = createDate;
		}

		public static DataBean objectFromData(String str) {

			return new Gson().fromJson(str, DataBean.class);
		}

		public static DataBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List <DataBean> arrayDataBeanFromData(String str) {

			Type listType = new TypeToken <ArrayList <DataBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List <DataBean> arrayDataBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken <ArrayList <DataBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getUserLogo() {
			return userLogo;
		}

		public void setUserLogo(String userLogo) {
			this.userLogo = userLogo;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public int getComment_user_like() {
			return comment_user_like;
		}

		public void setComment_user_like(int comment_user_like) {
			this.comment_user_like = comment_user_like;
		}

		public int getReplyTotal() {
			return replyTotal;
		}

		public void setReplyTotal(int replyTotal) {
			this.replyTotal = replyTotal;
		}

		public List <ReplyListBean> getReplyList() {
			return replyList;
		}

		public void setReplyList(List <ReplyListBean> replyList) {
			this.replyList = replyList;
		}

		public static class ReplyListBean {
			/**
			 * nickName : 最帅的开发邦
			 * userLogo : http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg
			 * id : 17
			 * commentId : 1
			 * content : 测试
			 * createDate : 2018-07-05 01:21:14
			 * reply_user_like : 0
			 */

			private String nickName;
			private String userLogo;
			private int id;
			private int commentId;
			private String content;
			private String createDate;
			private int reply_user_like;

			public ReplyListBean(String nickName, String content) {
				this.nickName = nickName;
				this.content = content;
			}

			public static ReplyListBean objectFromData(String str) {

				return new Gson().fromJson(str, ReplyListBean.class);
			}

			public static ReplyListBean objectFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);

					return new Gson().fromJson(jsonObject.getString(str), ReplyListBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			public static List <ReplyListBean> arrayReplyListBeanFromData(String str) {

				Type listType = new TypeToken <ArrayList <ReplyListBean>>() {
				}.getType();

				return new Gson().fromJson(str, listType);
			}

			public static List <ReplyListBean> arrayReplyListBeanFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);
					Type listType = new TypeToken <ArrayList <ReplyListBean>>() {
					}.getType();

					return new Gson().fromJson(jsonObject.getString(str), listType);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return new ArrayList();


			}

			public String getNickName() {
				return nickName;
			}

			public void setNickName(String nickName) {
				this.nickName = nickName;
			}

			public String getUserLogo() {
				return userLogo;
			}

			public void setUserLogo(String userLogo) {
				this.userLogo = userLogo;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public int getCommentId() {
				return commentId;
			}

			public void setCommentId(int commentId) {
				this.commentId = commentId;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public String getCreateDate() {
				return createDate;
			}

			public void setCreateDate(String createDate) {
				this.createDate = createDate;
			}

			public int getReply_user_like() {
				return reply_user_like;
			}

			public void setReply_user_like(int reply_user_like) {
				this.reply_user_like = reply_user_like;
			}
		}
	}
}
/*	private int state;
	private int total;
	private List <CommentDetailBean> commentDetail;

	public static DynamicsResult objectFromData(String str) {

		return new Gson().fromJson(str, DynamicsResult.class);
	}

	public static DynamicsResult objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), DynamicsResult.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List <DynamicsResult> arrayDynamicsResultFromData(String str) {

		Type listType = new TypeToken <ArrayList <DynamicsResult>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List <DynamicsResult> arrayDynamicsResultFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken <ArrayList <DynamicsResult>>() {
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List <CommentDetailBean> getCommentDetailList() {
		return commentDetail;
	}

	public void setCommentDetailList(List <CommentDetailBean> commentDetail) {
		this.commentDetail = commentDetail;
	}

	public static class CommentDetailBean {
		*//**
 * id : 1
 * nickName :  讲座萌新
 * userLogo : http://119.29.93.31:2000/user/7.jpg
 * content : 测试
 * createDate : 2018-07-02 04:59:02
 * comment_user_like : 0
 * replyTotal : 3
 * replyDetail : [{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":17,"commentId":1,"content":"测试","createDate":"2018-07-05 01:21:14","reply_user_like":0},{"nickName":"最帅的开发邦","userLogo":"http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg","id":18,"commentId":3,"content":"《向往的生活》快要大结局了。。","createDate":"2018-07-04 10:23:32","reply_user_like":0},{"nickName":" 讲座萌新","userLogo":"http://119.29.93.31:2000/user/7.jpg","id":19,"commentId":1,"content":"测试","createDate":"2018-07-05 01:34:23","reply_user_like":0}]
 * <p>
 * nickName : 最帅的开发邦
 * userLogo : http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg
 * id : 17
 * commentId : 1
 * content : 测试
 * createDate : 2018-07-05 01:21:14
 * reply_user_like : 0
 *//*

		private int id;
		private String nickName;
		private String userLogo;
		private String content;
		private String createDate;
		private int comment_user_like;
		private int replyTotal;
		private List <ReplyDetailBean> replyDetail;


		public CommentDetailBean(String nickName, String content, String createDate) {
			this.nickName = nickName;
			this.content = content;
			this.createDate = createDate;
		}

		public static CommentDetailBean objectFromData(String str) {

			return new Gson().fromJson(str, CommentDetailBean.class);
		}

		public static CommentDetailBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), CommentDetailBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List <CommentDetailBean> arrayCommentDetailBeanFromData(String str) {

			Type listType = new TypeToken <ArrayList <CommentDetailBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List <CommentDetailBean> arrayCommentDetailBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken <ArrayList <CommentDetailBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getUserLogo() {
			return userLogo;
		}

		public void setUserLogo(String userLogo) {
			this.userLogo = userLogo;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public int getComment_user_like() {
			return comment_user_like;
		}

		public void setComment_user_like(int comment_user_like) {
			this.comment_user_like = comment_user_like;
		}

		public int getReplyTotal() {
			return replyTotal;
		}

		public void setReplyTotal(int replyTotal) {
			this.replyTotal = replyTotal;
		}

		public List <ReplyDetailBean> getReplyList() {
			return replyDetail;
		}

		public void setReplyList(List <ReplyDetailBean> replyDetail) {
			this.replyDetail = replyDetail;
		}

		public static class ReplyDetailBean {
			*//**
 * nickName : 最帅的开发邦
 * userLogo : http://119.29.93.31:2000/user/62018-06-02-23-25-03,time.localtime(time.time).jpg
 * id : 17
 * commentId : 1
 * content : 测试
 * createDate : 2018-07-05 01:21:14
 * reply_user_like : 0
 *//*

			private String nickName;
			private String userLogo;
			private int id;
			private int commentId;
			private String content;
			private String createDate;
			private int reply_user_like;

			public ReplyDetailBean(String nickName, String content) {
				this.nickName = nickName;
				this.content = content;
			}

			public static ReplyDetailBean objectFromData(String str) {

				return new Gson().fromJson(str, ReplyDetailBean.class);
			}

			public static ReplyDetailBean objectFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);

					return new Gson().fromJson(jsonObject.getString(str), ReplyDetailBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			public static List <ReplyDetailBean> arrayReplyDetailBeanFromData(String str) {

				Type listType = new TypeToken <ArrayList <ReplyDetailBean>>() {
				}.getType();

				return new Gson().fromJson(str, listType);
			}

			public static List <ReplyDetailBean> arrayReplyDetailBeanFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);
					Type listType = new TypeToken <ArrayList <ReplyDetailBean>>() {
					}.getType();

					return new Gson().fromJson(jsonObject.getString(str), listType);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return new ArrayList();


			}

			public String getNickName() {
				return nickName;
			}

			public void setNickName(String nickName) {
				this.nickName = nickName;
			}

			public String getUserLogo() {
				return userLogo;
			}

			public void setUserLogo(String userLogo) {
				this.userLogo = userLogo;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public int getCommentId() {
				return commentId;
			}

			public void setCommentId(int commentId) {
				this.commentId = commentId;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public String getCreateDate() {
				return createDate;
			}

			public void setCreateDate(String createDate) {
				this.createDate = createDate;
			}

			public int getReply_user_like() {
				return reply_user_like;
			}

			public void setReply_user_like(int reply_user_like) {
				this.reply_user_like = reply_user_like;
			}
		}*/


