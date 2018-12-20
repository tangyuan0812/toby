package com.nmys.story.webmagic;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class Test2a {
	private static  CyclicBarrier cyclicBarrier;

	public static void main(String[] args) {
		/*cyclicBarrier = new CyclicBarrier(10);
		for (int i = 0; i < 10; i++) {
			new RunnableTask().start();
		}*/
		
		for(;;) {
			try {
				String httpGet = httpGet("https://crawldata.app/api/douyin/v2/feed", 3000);
				List<Map> strToArray = DataUtils
						.strToArray(JSONPath.eval(JSONObject.parse(httpGet), "$.data.aweme_list").toString());
				for (Map map : strToArray) {
					try {
						List<String> strToArrayInString = DataUtils.strToArrayInString(
								JSONPath.eval(JSONObject.parse(map.get("video").toString()), "$.play_addr.url_list")
										.toString());
						Testa.download(strToArrayInString.get(0));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				Thread.sleep(300000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static class RunnableTask extends Thread {

		@Override
		public void run() {
			try {
				String httpGet = httpGet("https://crawldata.app/api/douyin/v2/feed", 3000);
				List<Map> strToArray = DataUtils
						.strToArray(JSONPath.eval(JSONObject.parse(httpGet), "$.data.aweme_list").toString());
				for (Map map : strToArray) {
					try {
						List<String> strToArrayInString = DataUtils.strToArrayInString(
								JSONPath.eval(JSONObject.parse(map.get("video").toString()), "$.play_addr.url_list")
										.toString());
						Testa.download(strToArrayInString.get(0));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String httpGet(String url, int timeOut) {

		try {
			URL u = new URL(url);
			if ("https".equalsIgnoreCase(u.getProtocol())) {
				SslUtils.ignoreSsl();
			}
			URLConnection conn = u.openConnection();
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			return IOUtils.toString(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;

	}

}
