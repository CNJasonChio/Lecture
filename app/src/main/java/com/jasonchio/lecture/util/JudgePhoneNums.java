package com.jasonchio.lecture.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
 * Created by zhaoyaobang on 2018/2/27.
 */

public class JudgePhoneNums {
	/**
	 * 判断手机号码是否合理
	 *
	 * @param phoneNums
	 */
	public boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		return false;
	}

	private static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			Log.d("!!!!!!!!!","phone empty");
			return false;
		} else {
			if(str.length() == length)
				return true;
			else{
				Log.d("!!!!!!!!!","phone length wrong");
				return false;
			}
		}
	}

	private static boolean isMobileNO(String mobileNums) {
		String telRegex = "[1][345789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else{
			if(mobileNums.matches(telRegex)){
				return true;
			}else{
				Log.d("!!!!!!!","phone number is wrong");
				return false;
			}
		}

	}
}
