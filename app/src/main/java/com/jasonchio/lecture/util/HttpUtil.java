package com.jasonchio.lecture.util;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


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

	//注册请求
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

		Logger.d("开始发送注册请求");

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
	public static String LectureRequest(String address, int port, int lectureID) throws IOException, JSONException {

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
	public static String CommentRequest(String address, int port,int commentID) throws IOException, JSONException {

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
	public static String UserInfoRequest(String address, int port, int userID) throws IOException, JSONException {

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
	public static String changeUserInfo(String address, int port,int userID, String userName, String userPhone, String userSex, String userSchool, String userBirthday) throws IOException, JSONException {

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

	//请求图书馆信息
	public static String LibraryRequest(String address, int port, String libraryName) throws JSONException, IOException {

		Log.d("testLibrary", "LibraryRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("libraryName", libraryName);

		String test = testjson.toString();

		Log.d("testLibrary", test);
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
			Log.d("testLibrary", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testLibrary", "获取服务器数据完毕");
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
	public static String SendPosition(String address, int port, int userId, double userLongitude, double userLatitude) throws IOException, JSONException {
		Log.d("testSendPosition", "SendPosition");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("userLongitude", userLongitude);
		testjson.put("userLatitude", userLatitude);

		String test = testjson.toString();
		Log.d("testSendPosition", test);

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
			Log.d("testSendPosition", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testSendPosition", "获取服务器数据完毕");
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
	public static String AddLectureWantedRequest(String address, int port, int userId, int lectureId, boolean iswanted) throws IOException, JSONException {
		Log.d("testAddLecWant", "AddLedtureWantedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("lectureID", lectureId);
		testjson.put("iswanted", iswanted);

		String test = testjson.toString();
		Log.d("testAddLecWant", test);

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
			Log.d("testAddLecWant", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testAddLecWant", "获取服务器数据完毕");
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
	public static String AddLibraryFocusedRequest(String address, int port, int userId, int lectureId, boolean isfocused) throws IOException, JSONException {

		Log.d("testAddLibFocuse", "AddLibraryFocusedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);
		testjson.put("lectureID", lectureId);
		testjson.put("isfocused", isfocused);

		String test = testjson.toString();
		Log.d("testAddLibFocuse", test);

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
			Log.d("testAddLibFocuse", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testAddLibFocuse", "获取服务器数据完毕");
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

		Log.d("testSearchLec", "SearchLectureRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("searchKey", searchKey);


		String test = testjson.toString();
		Log.d("testSearchLec", test);

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
			Log.d("testSearchLec", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testSearchLec", "获取服务器数据完毕");
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
	public static String AddCommentRequest(String address, int port, String commentContent, int userId, int lectureId, String commentTime) throws IOException, JSONException {

		Log.d("testAddComment", "AddCommentRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("commentContent", commentContent);
		testjson.put("userID", userId);
		testjson.put("lectureID", lectureId);
		testjson.put("commentTime", commentTime);

		String test = testjson.toString();
		Log.d("testAddComment", test);

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
			Log.d("testAddComment", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testAddLibFocuse", "获取服务器数据完毕");
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

		Log.d("testMyWanted", "MyWantedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();
		Log.d("testMyWanted", test);

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
			Log.d("testMyWanted", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testMyWanted", "获取服务器数据完毕");
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
	public static String MyFocusedRequest(String address, int port, int userId) throws JSONException, IOException {

		Log.d("testMyFocused", "MyFocusedRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();
		Log.d("testMyWanted", test);

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
			Log.d("testMyWanted", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testMyWanted", "获取服务器数据完毕");
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
	public static String MycommentRequest(String address, int port, int userId) throws IOException, JSONException {

		Log.d("testMycomment", "MycommentRequest");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userID", userId);

		String test = testjson.toString();
		Log.d("testMycomment", test);

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
			Log.d("testMycomment", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testMycomment", "获取服务器数据完毕");
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
	public static String LikeThisComment(String address, int port, int commentId, boolean islike) throws IOException, JSONException {

		Log.d("testLikeComment", "LikeThisComment");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("commentID", commentId);
		testjson.put("isLike", islike);

		String test = testjson.toString();
		Log.d("testLikeComment", test);

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
			Log.d("testLikeComment", "给服务器发送数据完毕");
			//接收服务器返回的数据
			//创建输入流对象InputStream
			inputStream = socket.getInputStream();
			//创建输入流读取器对象 并传入输入流对象
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			//通过输入流读取器对象 接收服务器发送过来的数据
			response = bufferedReader.readLine();
			Log.d("testLikeComment", "获取服务器数据完毕");
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
	public static String changeUserHead(String address, int port, String userHead) throws IOException, JSONException {


		Log.d("testchangeInfo", "changeUserInfo");

		JSONObject testjson = new JSONObject();

		String response;

		testjson.put("userHead", userHead);

		String test = testjson.toString();

		Log.d("testchangeInfo", test);

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
			Log.d("testchangeInfo", "给服务器发送数据完毕");
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

}
