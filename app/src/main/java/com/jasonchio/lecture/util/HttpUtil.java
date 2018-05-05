package com.jasonchio.lecture.util;


import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.jasonchio.lecture.gson.CommonStateResult;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;


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
 * Created by zhaoyaobang on 2018/3/4.
 */

public class HttpUtil {

	/*//注册请求
	public static String SigninRequest(String address, int port, String userPhone, String userPwd) throws IOException, JSONException {

		Logger.d("开始发送注册请求");

		//向服务器发送的json数据
		JSONObject sendJson = new JSONObject();

		sendJson.put("userPhone", userPhone);
		sendJson.put("userPwd", userPwd);

		//服务器返回的json数据
		String response;

		//向服务器发送的json数据对应的字符串
		String jsonStrSend = sendJson.toString();
		Logger.json(jsonStrSend);

		//创建Socket对象 & 指定服务端的IP及端口号
		Socket socket = new Socket(address, port);

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(jsonStrSend.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("向服务器发送数据完毕");
			//接收服务器返回的数据
			Logger.d("开始获取服务器数据");
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			Logger.e(e, "UnknownHostException");
		} catch (IOException e) {
			Logger.e(e, "IOException");
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//找回密码请求
	public static String FindPwdRequest(String address, int port, String userPhone, String newPwd) throws IOException, JSONException {

		Logger.d("开始发送修改密码请求");

		//向服务器发送的json数据
		JSONObject sendJson = new JSONObject();
		sendJson.put("userPhone", userPhone);
		sendJson.put("newPwd", newPwd);

		//服务器返回的json数据
		String response;

		//向服务器发送的json数据对应的字符串
		String jsonStrSend = sendJson.toString();

		//创建Socket对象 & 指定服务端的IP及端口号
		Socket socket = new Socket(address, port);

		InputStream inputStream = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream;

		try {
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(jsonStrSend.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("向服务器发送数据完毕");

			//接收服务器返回的数据
			Logger.d("开始获取服务器数据");
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			Logger.e(e, "UnknownHostException");
		} catch (IOException e) {
			Logger.e(e, "IOException");
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//登录请求
	public static String LoginRequest(String address, int port, String userPhone, String userPwd) throws IOException, JSONException {

		Logger.d("LoginRequest started");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userPhone", userPhone);
		testjson.put("userPwd", userPwd);

		String test = testjson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//请求讲座数据
	public static String LectureRequest(String address, int port, long lectureID) throws IOException, JSONException {

		Logger.d("LectureRequset started");

		String response;

		Socket socket;

		JSONObject testjson = new JSONObject();

		testjson.put("lectureID", lectureID);

		String test = testjson.toString();

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		//创建Socket对象 & 指定服务端的IP及端口号
		socket = new Socket(address, port);

		try {

			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);

			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//请求评论信息
	public static String CommentRequest(String address, int port,long commentID) throws IOException, JSONException {

		Logger.d("CommentzsRequset started");

		String response;

		Socket socket;

		JSONObject testjson = new JSONObject();

		testjson.put("commentID", commentID);

		String test = testjson.toString();

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		//创建Socket对象 & 指定服务端的IP及端口号
		socket = new Socket(address, port);

		try {

			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);

			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//请求用户信息
	public static String UserInfoRequest(String address, int port,long userID) throws IOException, JSONException {

		Logger.d("UserInfoRequest started");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userID);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Log.d("UserTest", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.json(response);
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//修改用户个人信息
	public static String changeUserInfo(String address, int port,long userID, String userName, String userPhone, String userSex, String userSchool, String userBirthday) throws IOException, JSONException {

		Logger.d("changeUserInfo");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID",userID);
		testjson.put("userName", userName);
		testjson.put("userPhone", userPhone);
		testjson.put("userSex", userSex);
		testjson.put("userSchool", userSchool);
		testjson.put("userBirthday", userBirthday);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");

			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testchangeInfo", "获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//搜索讲座
	public static String SearchLectureRequest(String address, int port, String searchKey) throws IOException, JSONException {


		Logger.d("SearchLectureRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("searchKey", searchKey);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("接收服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的想看”
	public static String MyWantedRequest(String address, int port, int userId) throws IOException, JSONException {

		Logger.d("MyWantedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的关注”
	public static String MyFocusedRequest(String address, int port, long userId) throws JSONException, IOException {

		Logger.d("MyFocusedRequest");
		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的点评”
	public static String MycommentRequest(String address, int port, long userId) throws IOException, JSONException {

		Logger.d("MycommentRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//添加讲座至“我的想看”（或取消）
	public static String AddLectureWantedRequest(String address, int port, int userId, int lectureId, int iswanted) throws IOException, JSONException {


		Logger.d("AddLedtureWantedRequest");
		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("lectureID", lectureId);
		testjson.put("iswanted", iswanted);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//添加图书馆至“我的关注”（或取消）
	public static String AddLibraryFocusedRequest(String address, int port, long userId, long libraryId, int isfocused) throws IOException, JSONException {

		Logger.d("AddLibraryFocusedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("libraryID", libraryId);
		testjson.put("isfocused", isfocused);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			Logger.d( "开始给服务器发送数据");
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d( "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//请求图书馆信息
	public static String LibraryRequest(String address, int port, String libraryName) throws JSONException, IOException {

		Logger.d("LibraryRequest");
		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("libraryName", libraryName);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//提交用户位置信息
	public static String SendPosition(String address, int port, long userId, double userLongtitude, double userLatitude) throws IOException, JSONException {

		Logger.d("SendPosition");
		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("userLongtitude", userLongtitude);
		testjson.put("userLatitude", userLatitude);

		String test = testjson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//评论讲座
	public static String AddCommentRequest(String address, int port, String commentContent, long userId, int lectureId, String commentTime) throws IOException, JSONException {

		Logger.d("AddCommentRequest");
		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("commentContent", commentContent);
		testjson.put("userID", userId);
		testjson.put("lectureID", lectureId);
		testjson.put("commentTime", commentTime);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//给评论点赞
	public static String LikeThisComment(String address, int port, long commentId, long userID,int islike) throws IOException, JSONException {

		Logger.d("LikeThisComment");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("commentID", commentId);
		testjson.put("islike",islike);
		testjson.put("userID",userID);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//修改用户头像
	public static String changeUserHead(String address, int port,long userID,Bitmap userHead,int size) throws IOException, JSONException {

		Logger.d("changeUserInfo");

		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		userHead.compress(Bitmap.CompressFormat.PNG,100,bout);

		String response;

		JSONObject testjson = new JSONObject();

		testjson.put("userID",userID);
		testjson.put("size",size);

		String test = testjson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, port);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			outputStream.write(bout.toByteArray());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	public static void ContentRequest(String address, Callback callback){
		OkHttpClient client=new OkHttpClient();
		Request request= new Request.Builder().url(address).build();
		client.newCall(request).enqueue(callback);
	}*/

	//注册请求
	public static String SigninRequest(String address, int com, String userPhone, String userPwd) throws IOException, JSONException {

		Logger.d("开始发送注册请求");

		//向服务器发送的json数据
		JSONObject sendJson = new JSONObject();
		sendJson.put("commond", com);
		sendJson.put("userPhone", userPhone);
		sendJson.put("userPwd", userPwd);

		//服务器返回的json数据
		String response;

		//向服务器发送的json数据对应的字符串
		String jsonStrSend = sendJson.toString();
		Logger.json(jsonStrSend);

		//创建Socket对象 & 指定服务端的IP及端口号
		Socket socket = new Socket(address, 2001);

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(jsonStrSend.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("向服务器发送数据完毕");
			//接收服务器返回的数据
			Logger.d("开始获取服务器数据");
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			Logger.e(e, "UnknownHostException");
		} catch (IOException e) {
			Logger.e(e, "IOException");
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//找回密码请求
	public static String FindPwdRequest(String address, int com, String userPhone, String newPwd) throws IOException, JSONException {

		Logger.d("开始发送修改密码请求");

		//向服务器发送的json数据
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", com);
		sendJson.put("userPhone", userPhone);
		sendJson.put("newPwd", newPwd);

		//服务器返回的json数据
		String response;

		//向服务器发送的json数据对应的字符串
		String jsonStrSend = sendJson.toString();

		//创建Socket对象 & 指定服务端的IP及端口号
		Socket socket = new Socket(address, 2001);

		InputStream inputStream = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		OutputStream outputStream;

		try {
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(jsonStrSend.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("向服务器发送数据完毕");

			//接收服务器返回的数据
			Logger.d("开始获取服务器数据");
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			Logger.e(e, "UnknownHostException");
		} catch (IOException e) {
			Logger.e(e, "IOException");
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//登录请求
	public static String LoginRequest(String address, int com, String userPhone, String userPwd) throws IOException, JSONException {

		Logger.d("LoginRequest started");

		JSONObject sendJson = new JSONObject();

		String response;
		sendJson.put("command", com);
		sendJson.put("userPhone", userPhone);
		sendJson.put("userPwd", userPwd);

		String test = sendJson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//请求讲座数据
	public static String LectureRequest(String address, int com, long userID, long lectureID) throws IOException, JSONException {

		Logger.d("LectureRequset started");

		String response;

		Socket socket;

		JSONObject sendJson = new JSONObject();
		sendJson.put("command", com);
		sendJson.put("userID", userID);
		sendJson.put("lectureID", lectureID);

		String test = sendJson.toString();

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		//创建Socket对象 & 指定服务端的IP及端口号
		socket = new Socket(address, 2001);

		try {

			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);

			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//请求评论信息
	public static String CommentRequest(String address, int com, long userID, long commentID) throws IOException, JSONException {

		Logger.d("CommentRequset started");

		String response;

		Socket socket;

		JSONObject sendJson = new JSONObject();

		sendJson.put("command", com);
		sendJson.put("userID", userID);
		sendJson.put("commentID", commentID);

		String test = sendJson.toString();

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		//创建Socket对象 & 指定服务端的IP及端口号
		socket = new Socket(address, 2001);

		try {

			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();

			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream, "utf-8");
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);

			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//请求用户信息
	public static String UserInfoRequest(String address, int com, long userID) throws IOException, JSONException {

		Logger.d("UserInfoRequest started");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userID);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Log.d("UserTest", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.json(response);
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//修改用户个人信息
	public static String changeUserInfo(String address, int com, long userID, String userName, String userPhone, String userSex, String userSchool, String userBirthday) throws IOException, JSONException {

		Logger.d("changeUserInfo");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userID);
		sendJson.put("userName", userName);
		sendJson.put("userPhone", userPhone);
		sendJson.put("userSex", userSex);
		sendJson.put("userSchool", userSchool);
		sendJson.put("userBirthday", userBirthday);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");

			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testchangeInfo", "获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//搜索讲座
	public static String SearchLectureRequest(String address, int com, String searchKey) throws IOException, JSONException {


		Logger.d("SearchLectureRequest");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("searchKey", searchKey);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("接收服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的想看”
	public static String MyWantedRequest(String address, int com, long userId) throws IOException, JSONException {

		Logger.d("MyWantedRequest");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的关注”
	public static String MyFocusedRequest(String address, int com, long userId) throws JSONException, IOException {

		Logger.d("MyFocusedRequest");
		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//获取“我的点评”
	public static String MycommentRequest(String address, int com, long userId) throws IOException, JSONException {

		Logger.d("MycommentRequest");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//添加讲座至“我的想看”（或取消）
	public static String AddLectureWantedRequest(String address, int com, long userId, long lectureId, int iswanted) throws IOException, JSONException {


		Logger.d("AddLedtureWantedRequest");
		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);
		sendJson.put("lectureID", lectureId);
		sendJson.put("iswanted", iswanted);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//添加图书馆至“我的关注”（或取消）
	public static String AddLibraryFocusedRequest(String address, int com, long userId, String libraryName, int isfocused) throws IOException, JSONException {

		Logger.d("AddLibraryFocusedRequest");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);
		sendJson.put("libraryName", libraryName);
		sendJson.put("isfocused", isfocused);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			Logger.d("开始给服务器发送数据");
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//请求图书馆信息
	public static String LibraryRequest(String address, int com, long userID, String libraryName) throws JSONException, IOException {

		Logger.d("LibraryRequest");
		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userID);
		sendJson.put("libraryName", libraryName);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.d(response);
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//提交用户位置信息
	public static String SendPosition(String address, int com, long userId, double userLongtitude, double userLatitude) throws IOException, JSONException {

		Logger.d("SendPosition");
		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("userID", userId);
		sendJson.put("userLongtitude", userLongtitude);
		sendJson.put("userLatitude", userLatitude);

		String test = sendJson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//评论讲座
	public static String AddCommentRequest(String address, int com, String commentContent, long userId, long lectureId, String commentTime) throws IOException, JSONException {

		Logger.d("AddCommentRequest");
		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("commentContent", commentContent);
		sendJson.put("userID", userId);
		sendJson.put("lectureID", lectureId);
		sendJson.put("commentTime", commentTime);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//给评论点赞
	public static String LikeThisComment(String address, int com, long commentId, long userID, int islike) throws IOException, JSONException {

		Logger.d("LikeThisComment");

		JSONObject sendJson = new JSONObject();

		String response;

		sendJson.put("command", com);
		sendJson.put("commentID", commentId);
		sendJson.put("islike", islike);
		sendJson.put("userID", userID);

		String test = sendJson.toString();

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//修改用户头像
	public static String changeUserHead(String address, int com, long userID, Bitmap userHead, int size) throws IOException, JSONException {

		Logger.d("changeUserHead");

		String response;

		JSONObject sendJson = new JSONObject();

		sendJson.put("command", com);
		sendJson.put("userID", userID);
		sendJson.put("size", size);

		String test = sendJson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			Logger.d("给服务器发送头像信息数据完毕");
			//关闭输出流
			socket.shutdownOutput();

			Logger.d("给服务器发送头像数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");

			Logger.d(response);

			response=sendUserHeadFile(response, userHead);

			return response;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//正文请求
	public static void ContentRequest(String address, Callback callback) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(address).build();
		client.newCall(request).enqueue(callback);
	}

	//推荐讲座请求
	public static String RecommentRequest(String address, int com, long userID) throws JSONException, IOException {

		Logger.d("推荐讲座请求");

		//向服务器发送的json数据
		JSONObject sendJson = new JSONObject();
		sendJson.put("command", com);
		sendJson.put("userID", userID);

		//服务器返回的json数据
		String response;

		//向服务器发送的json数据对应的字符串
		String jsonStrSend = sendJson.toString();
		Logger.json(jsonStrSend);

		//创建Socket对象 & 指定服务端的IP及端口号
		Socket socket = new Socket(address, 2001);

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			Logger.d("开始向服务器发送数据");
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(jsonStrSend.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("向服务器发送数据完毕");
			//接收服务器返回的数据
			Logger.d("开始获取服务器数据");
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Logger.d("获取服务器数据完毕");
			return response;
		} catch (UnknownHostException e) {
			Logger.e(e, "UnknownHostException");
		} catch (IOException e) {
			Logger.e(e, "IOException");
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
			socket.close();
		}
		return null;
	}

	//检查更新
	public static String UpdateRequest(String address, int com, String version) throws JSONException, IOException {

		Logger.d("UpdateRequest started");

		JSONObject sendJson = new JSONObject();

		String response;
		sendJson.put("command", com);
		sendJson.put("version", version);

		String test = sendJson.toString();

		Logger.json(test);

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		try {
			Logger.d("开始发送");
			//创建Socket对象 & 指定服务端的IP及端口号
			socket = new Socket(address, 2001);
			//创建输出流对象outputStream
			outputStream = socket.getOutputStream();
			//写入要发送给服务器的数据
			outputStream.write(test.getBytes());
			//发送数据到服务端
			outputStream.flush();
			//关闭输出流
			socket.shutdownOutput();
			Logger.d("发送完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();

			Logger.json(response);
			return response;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Logger.d(e);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
			reader.close();
			inputStream.close();
		}
		return null;
	}

	//向服务器发送头像
	public static String sendUserHeadFile(String changeUserHeadResut, Bitmap userHead) throws IOException {

		Logger.d("sendUserHeadFile");

		String response = null;

		Gson gson = new Gson();

		CommonStateResult result = gson.fromJson(changeUserHeadResut, CommonStateResult.class);

		int state = -1;

		Socket socket;

		InputStream inputStream = null;

		InputStreamReader reader = null;

		BufferedReader bufferedReader = null;

		OutputStream outputStream;

		if (result != null) {
			state = result.getState();
			if (state == 0) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				userHead.compress(Bitmap.CompressFormat.PNG, 100, bout);
				try {
					//创建Socket对象 & 指定服务端的IP及端口号
					socket = new Socket(ConstantClass.ADDRESS, 2002);
					//创建输出流对象outputStream
					outputStream = socket.getOutputStream();
					//写入要发送给服务器的数据
					outputStream.write(bout.toByteArray());
					//发送数据到服务端
					outputStream.flush();
					Logger.d("给服务器发送头像文件完毕");
					//关闭输出流
					socket.shutdownOutput();

					//接收服务器返回的数据
					//创建输入流对象InputStream
					inputStream = socket.getInputStream();
					//创建输入流读取器对象 并传入输入流对象
					reader = new InputStreamReader(inputStream);
					bufferedReader = new BufferedReader(reader);
					//通过输入流读取器对象 接收服务器发送过来的数据
					response = bufferedReader.readLine();
					Logger.d("获取服务器数据完毕");

					Logger.d(response);
					return response;

				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					bufferedReader.close();
					reader.close();
					inputStream.close();
				}
			}
		}
		return response;
	}
}
