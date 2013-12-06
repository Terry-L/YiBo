package com.cattong.weibo.impl.sina;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.cattong.commons.LibException;
import com.cattong.commons.LibResultCode;
import com.cattong.commons.util.ParseUtil;
import com.cattong.weibo.entity.RateLimitStatus;

/**
 * SinaRateLimitStatusAdaptor
 *
 * @version
 * @author cattong.com
 * @time 2010-8-30 下午04:36:35
 */
class SinaRateLimitStatusAdaptor {

	/** 请求超限后的错误描述字符串 */
	public static final String ERROR = "Error: user requests out of rate limit!";

	/**
	 * 从JSON字符串创建RateLimitStatus对象
	 *
	 * @param jsonString
	 *            json字符串
	 * @return RateLimitStatus对象
	 * @throws LibException
	 */
	public static RateLimitStatus createRateLimitStatus(String jsonString) throws LibException {
		try {
			return createRateLimitStatus(new JSONObject(jsonString));
		} catch (JSONException e) {
			throw new LibException(LibResultCode.JSON_PARSE_ERROR);
		}
	}

	/**
	 * 从JSON对象创建RateLimitStatus对象，包级别访问控制
	 *
	 * @param json
	 *            JSON对象
	 * @return RateLimitStatus对象
	 * @throws LibException
	 */
	static RateLimitStatus createRateLimitStatus(JSONObject json) throws LibException {
		try {
			RateLimitStatus rateLimitStatus = new RateLimitStatus();
			rateLimitStatus.setHourlyLimit(ParseUtil.getInt("user_limit", json));
			rateLimitStatus.setRemainingHits(ParseUtil.getInt("remaining_user_hits", json));
			rateLimitStatus.setResetedAt(ParseUtil.getDate("reset_time", json, "yyyy-MM-dd HH:mm:ss"));
			rateLimitStatus.setResetTimeInSeconds(ParseUtil.getInt("reset_time_in_seconds", json));
			
			return rateLimitStatus;
		} catch (ParseException e) {
			throw new LibException(LibResultCode.DATE_PARSE_ERROR);
		}
	}

}
