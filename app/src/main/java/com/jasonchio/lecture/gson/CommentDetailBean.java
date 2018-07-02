package com.jasonchio.lecture.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommentDetailBean {
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String imgId;
    private int replyTotal;
    private String createDate;
    private int isLikeOrNot;
    private int likeNum;
    private List <ReplyDetailBean> replyList;

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

    public static List <CommentDetailBean> arrayTlistBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<CommentDetailBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List <CommentDetailBean> arrayTlistBeanFromData(String str, String key) {

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

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public int getReplyTotal() {
        return replyTotal;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List <ReplyDetailBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List <ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }

    public int getIsLikeOrNot() {
        return isLikeOrNot;
    }

    public void setIsLikeOrNot(int isLikeOrNot) {
        this.isLikeOrNot = isLikeOrNot;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
