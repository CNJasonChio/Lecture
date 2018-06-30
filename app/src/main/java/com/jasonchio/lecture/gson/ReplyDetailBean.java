package com.jasonchio.lecture.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moos on 2018/4/20.
 */

public class ReplyDetailBean {
        /**
         * nickName : 沐風
         * userLogo : http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png
         * id : 40
         * commentId : 42
         * content : 时间总是在不经意中擦肩而过,不留一点痕迹.
         * status : 01
         * createDate : 一个小时前
         */

        private String nickName;
        private String userLogo;
        private int id;
        private String commentId;
        private String content;
        private String status;
        private String createDate;


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

        public static List<ReplyDetailBean> arrayReplyListBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<ReplyDetailBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List <ReplyDetailBean> arrayReplyListBeanFromData(String str, String key) {

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

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

}
